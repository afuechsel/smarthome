/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.config.discovery.internal;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultFlag;
import org.eclipse.smarthome.config.discovery.inbox.Inbox;
import org.eclipse.smarthome.config.discovery.inbox.InboxFilterCriteria;
import org.eclipse.smarthome.config.discovery.inbox.InboxListener;
import org.eclipse.smarthome.config.discovery.inbox.events.InboxAddedEvent;
import org.eclipse.smarthome.config.discovery.inbox.events.InboxRemovedEvent;
import org.eclipse.smarthome.config.discovery.inbox.events.InboxUpdatedEvent;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.thing.ManagedThingProvider;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.test.java.JavaOSGiTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboxOSGITest2 extends JavaOSGiTest {

    private final Logger logger = LoggerFactory.getLogger(InboxOSGITest2.class);

    class DiscoveryService1 extends AbstractDiscoveryService {
        public DiscoveryService1() {
            super(5);
        }

        @Override
        protected void startScan() {
        }
    }

    class DiscoveryService2 extends AbstractDiscoveryService {
        public DiscoveryService2() {
            super(5);
        }

        @Override
        protected void startScan() {
        }
    }

    private static final int DEFAULT_TTL = 60;

    private static final ThingTypeUID THING_TYPE_UID = new ThingTypeUID("bindingId", "thing");
    private static final ThingTypeUID BRIDGE_THING_TYPE_UID = new ThingTypeUID("bindingId", "bridge");

    private static final ThingUID BRIDGE_THING_UID = new ThingUID(BRIDGE_THING_TYPE_UID, "bridgeId");

    private static final DiscoveryResult BRIDGE = new DiscoveryResultImpl(BRIDGE_THING_TYPE_UID, BRIDGE_THING_UID, null,
            null, "Bridge", "bridge", DEFAULT_TTL);
    private static final DiscoveryResult THING1_WITH_BRIDGE = new DiscoveryResultImpl(THING_TYPE_UID,
            new ThingUID(THING_TYPE_UID, "id1"), BRIDGE_THING_UID, null, "Thing1", "thing1", DEFAULT_TTL);
    private static final DiscoveryResult THING2_WITH_BRIDGE = new DiscoveryResultImpl(THING_TYPE_UID,
            new ThingUID(THING_TYPE_UID, "id2"), BRIDGE_THING_UID, null, "Thing2", "thing2", DEFAULT_TTL);
    private static final DiscoveryResult THING_WITHOUT_BRIDGE = new DiscoveryResultImpl(THING_TYPE_UID,
            new ThingUID(THING_TYPE_UID, "id3"), null, null, "Thing3", "thing3", DEFAULT_TTL);
    private static final DiscoveryResult THING_WITH_OTHER_BRIDGE = new DiscoveryResultImpl(THING_TYPE_UID,
            new ThingUID(THING_TYPE_UID, "id4"), new ThingUID(THING_TYPE_UID, "id5"), null, "Thing4", "thing4",
            DEFAULT_TTL);

    private final Map<ThingUID, DiscoveryResult> discoveryResults = new HashMap<>();
    private final List<InboxListener> inboxListeners = new ArrayList<>();

    private PersistentInbox inbox;
    private ManagedThingProvider managedThingProvider;
    private ThingRegistry registry;

    @Before
    public void setUp() {
        registerVolatileStorageService();

        discoveryResults.clear();
        inboxListeners.clear();

        inbox = (PersistentInbox) getService(Inbox.class);
        managedThingProvider = getService(ManagedThingProvider.class);
        registry = getService(ThingRegistry.class);

        ComponentContext componentContextMock = mock(ComponentContext.class);
        when(componentContextMock.getBundleContext()).thenReturn(bundleContext);

        inbox.addThingHandlerFactory(new DummyThingHandlerFactory(componentContextMock));
    }

    @After
    public void cleanUp() {
        discoveryResults.forEach((thingUID, discoveryResult) -> inbox.remove(thingUID));
        inboxListeners.forEach(listener -> inbox.removeInboxListener(listener));
        discoveryResults.clear();
        inboxListeners.clear();
        registry.remove(BRIDGE_THING_UID);
        managedThingProvider.getAll().forEach(thing -> managedThingProvider.remove(thing.getUID()));
    }

    @Test
    public void assertThatRemoveRemovesAssociatedDiscoveryResultsFromInboxWhenBridgeIsRemoved() {
        List<Event> receivedEvents = new ArrayList<>();

        EventSubscriber inboxEventSubscriber = new EventSubscriber() {
            @Override
            public void receive(Event event) {
                receivedEvents.add(event);
            }

            @Override
            public Set<String> getSubscribedEventTypes() {
                return Stream.of(InboxAddedEvent.TYPE, InboxRemovedEvent.TYPE, InboxUpdatedEvent.TYPE).collect(toSet());
            }

            @Override
            public @Nullable EventFilter getEventFilter() {
                return null;
            }
        };

        registerService(inboxEventSubscriber);

        inbox.add(BRIDGE);
        inbox.add(THING1_WITH_BRIDGE);
        inbox.add(THING2_WITH_BRIDGE);
        inbox.add(THING_WITHOUT_BRIDGE);
        inbox.add(THING_WITH_OTHER_BRIDGE);

        for (Event event : receivedEvents) {
            logger.info("Event: type=" + event.getType() + ", topic=" + event.getTopic());
        }

        waitForAssert(() -> assertThat(receivedEvents.size(), is(5)));
        receivedEvents.clear();

        assertTrue(inbox.remove(BRIDGE.getThingUID()));
        assertTrue(inbox.get(new InboxFilterCriteria(BRIDGE.getThingUID(), DiscoveryResultFlag.NEW)).isEmpty());
        assertTrue(inbox.get(new InboxFilterCriteria(THING1_WITH_BRIDGE.getThingUID(), DiscoveryResultFlag.NEW))
                .isEmpty());
        assertTrue(inbox.get(new InboxFilterCriteria(THING2_WITH_BRIDGE.getThingUID(), DiscoveryResultFlag.NEW))
                .isEmpty());
        assertThat(inbox.get(new InboxFilterCriteria(DiscoveryResultFlag.NEW)),
                hasItems(THING_WITHOUT_BRIDGE, THING_WITH_OTHER_BRIDGE));
        waitForAssert(() -> {
            assertThat(receivedEvents.size(), is(3));
            for (Event event : receivedEvents) {
                assertThat(event, is(instanceOf(InboxRemovedEvent.class)));
            }
        });
    }

    @NonNullByDefault
    class DummyThingHandlerFactory extends BaseThingHandlerFactory {

        public DummyThingHandlerFactory(ComponentContext context) {
            super.activate(context);
        }

        @Override
        public boolean supportsThingType(ThingTypeUID thingTypeUID) {
            return true;
        }

        @Override
        protected @Nullable ThingHandler createHandler(Thing thing) {
            return null;
        }

        @Override
        public @Nullable Thing createThing(ThingTypeUID thingTypeUID, Configuration configuration,
                @Nullable ThingUID thingUID, @Nullable ThingUID bridgeUID) {
            if (thingUID != null) {
                return ThingBuilder.create(thingTypeUID, thingUID).withBridge(bridgeUID)
                        .withConfiguration(configuration).build();
            }
            return null;
        }
    }

}
