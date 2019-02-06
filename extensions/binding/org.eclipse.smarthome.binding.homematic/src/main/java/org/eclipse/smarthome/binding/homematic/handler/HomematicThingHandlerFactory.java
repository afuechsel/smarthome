/**
 * Copyright (c) 2014,2019 Contributors to the Eclipse Foundation
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
package org.eclipse.smarthome.binding.homematic.handler;

import static org.eclipse.smarthome.binding.homematic.HomematicBindingConstants.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.binding.homematic.internal.type.HomematicTypeGenerator;
import org.eclipse.smarthome.binding.homematic.type.HomematicThingTypeExcluder;
import org.eclipse.smarthome.core.net.NetworkAddressService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.net.http.HttpClientFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * The {@link HomematicThingHandlerFactory} is responsible for creating thing and bridge handlers.
 *
 * @author Gerhard Riegler - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, immediate = true, configurationPid = "binding.homematic")
public class HomematicThingHandlerFactory extends BaseThingHandlerFactory {
    private HomematicTypeGenerator typeGenerator;
    private NetworkAddressService networkAddressService;
    private HttpClient httpClient;
    private final List<HomematicThingTypeExcluder> homematicThingTypeExcluders = new CopyOnWriteArrayList<>();

    @Reference
    protected void setTypeGenerator(HomematicTypeGenerator typeGenerator) {
        this.typeGenerator = typeGenerator;
    }

    protected void unsetTypeGenerator(HomematicTypeGenerator typeGenerator) {
        this.typeGenerator = null;
    }

    @Reference
    protected void setHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClient = httpClientFactory.getCommonHttpClient();
    }

    protected void unsetHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClient = null;
    }

    @Reference
    protected void setNetworkAddressService(NetworkAddressService networkAddressService) {
        this.networkAddressService = networkAddressService;
    }

    protected void unsetNetworkAddressService(NetworkAddressService networkAddressService) {
        this.networkAddressService = null;
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void addHomematicThingTypeExcluder(HomematicThingTypeExcluder homematicThingTypeExcluder) {
        if (homematicThingTypeExcluders != null) {
            homematicThingTypeExcluders.add(homematicThingTypeExcluder);
        }
    }

    protected void removeHomematicThingTypeExcluder(HomematicThingTypeExcluder homematicThingTypeExcluder) {
        if (homematicThingTypeExcluders != null) {
            homematicThingTypeExcluders.remove(homematicThingTypeExcluder);
        }
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        boolean matchesBindingId = BINDING_ID.equals(thingTypeUID.getBindingId());
        if (!matchesBindingId) {
            return false;
        }
        for (HomematicThingTypeExcluder homematicThingTypeExcluder : homematicThingTypeExcluders) {
            for (ThingTypeUID excludedThingType : homematicThingTypeExcluder.getExcludedThingTypes()) {
                if (thingTypeUID.equals(excludedThingType)) {
                    // clients that provide custom thing-types may inject custom ThingHandler
                    // therefore any provider of HomematicThingTypeExcluder must also provide a ThingHandlerFactory
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        if (THING_TYPE_BRIDGE.equals(thing.getThingTypeUID())) {
            return new HomematicBridgeHandler((Bridge) thing, typeGenerator,
                    networkAddressService.getPrimaryIpv4HostAddress(), httpClient);
        } else {
            return new HomematicThingHandler(thing);
        }
    }

}
