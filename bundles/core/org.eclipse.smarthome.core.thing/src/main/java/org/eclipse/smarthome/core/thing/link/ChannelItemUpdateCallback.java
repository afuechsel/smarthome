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
package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.thing.internal.ChannelItemProvider;

/**
 * The {@link ChannelItemUpdateCallback} is used by the implementing class of {@link ChannelItemEnhancer}
 * to allow a notification of the {@link ChannelItemProvider} about any item updates.
 *
 * @author Andre Fuechsel - initial contribution
 *
 */
public interface ChannelItemUpdateCallback {

    /**
     * To be called when the given {@link Item} has been updated.
     *
     * @param item {@link Item} that has been updated
     */
    void updateItem(Item item);
}
