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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for the {@link ChannelItemEnhancer}. This implementation registers
 * a {@link ChannelItemPostProcessor}, that is used to post process the items when calling
 * {@link #postProcessItem(GenericItem)}.
 *
 * @author Andre Fuechsel - Initial contribution
 *
 */
public abstract class AbstractChannelItemEnhancer implements ChannelItemEnhancer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ChannelItemPostProcessor postProcessor;

    /**
     * Registers a {@link ChannelItemPostProcessor} that is used by {@link #postProcessItem(GenericItem)}
     * to actually perform the post processing.
     *
     * @see #postProcessItem(GenericItem)
     * @param postProcessor
     */
    public final void registerPostProcessor(ChannelItemPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    /**
     * Unregisters a previously registered {@link ChannelItemPostProcessor}.
     */
    public final void unregisterPostProcessor() {
        this.postProcessor = null;
    }

    @Override
    public final void postProcessItem(GenericItem item) {
        if (postProcessor != null) {
            this.postProcessor.postProcessItem(item);
        } else {
            logger.error("Cannot do any item post processing there is no post processor registered.");
        }
    }
}
