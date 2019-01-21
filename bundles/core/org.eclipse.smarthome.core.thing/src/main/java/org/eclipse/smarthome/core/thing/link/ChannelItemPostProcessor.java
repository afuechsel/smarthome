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

import org.eclipse.smarthome.core.items.GenericItem;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.thing.internal.ChannelItemProvider;

/**
 * Post process any {@link GenericItem} the {@link ChannelItemProvider} did create before adding it to the
 * {@link ItemRegistry}.
 *
 * @author Henning Treu - Initial contribution
 *
 */
public interface ChannelItemPostProcessor {

    /**
     * Do any post processing, like adding tags and other modifications to the item before it gets added to the
     * {@link ItemRegistry}.
     *
     * @param item the {@link GenericItem} to post process.
     */
    void postProcessItem(GenericItem item);

}
