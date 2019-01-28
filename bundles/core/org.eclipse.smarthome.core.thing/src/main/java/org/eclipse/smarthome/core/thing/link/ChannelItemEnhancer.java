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
package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.items.GenericItem;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.thing.internal.ChannelItemProvider;

/**
 * Enhance any {@link GenericItem} the {@link ChannelItemProvider} did create before adding it to the
 * {@link ItemRegistry}. Also provide a callback to the {@link ChannelItemProvider} to update any {@link GenericItem}.
 *
 * @author Henning Treu - Initial contribution
 * @author Andre Fuechsel - Refactored, changed the name and added callback mechanism
 *
 */
public interface ChannelItemEnhancer {

    /**
     * Do any post processing, like adding tags and other modifications to the item before it gets added to the
     * {@link ItemRegistry}. The call should be delegated a {@link ChannelItemPostProcessor}.
     *
     * @param item the {@link GenericItem} to post process.
     */
    void postProcessItem(GenericItem item);

    /**
     * Sets a {@link ChannelItemUpdateCallback} that can be used to trigger an update of an {@link Item}
     * provided by the {@link ChannelItemProvider}.
     *
     * @param callback the {@link ChannelItemUpdateCallback} to be registered.
     */
    void setUpdateCallback(ChannelItemUpdateCallback callback);

    /**
     * Removes the {@link ChannelItemUpdateCallback} that can be used to trigger an update of an {@link Item}
     * provided by the {@link ChannelItemProvider}.
     *
     * @param callback the {@link ChannelItemUpdateCallback} to be registered.
     */
    void removeUpdateCallback(ChannelItemUpdateCallback callback);
}
