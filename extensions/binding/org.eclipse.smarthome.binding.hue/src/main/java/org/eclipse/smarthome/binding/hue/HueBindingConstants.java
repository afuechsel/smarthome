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
package org.eclipse.smarthome.binding.hue;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link HueBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Kai Kreuzer - Initial contribution
 * @author Jochen Hiller - Added OSRAM Classic A60 RGBW
 * @author Markus Mazurczak - Added OSRAM PAR16 50
 * @author Andre Fuechsel - changed to generic thing types
 *
 */
public class HueBindingConstants {

    public static final String BINDING_ID = "hue";

    // List all Thing Type UIDs, related to the Hue Binding

    // bridge
    public static final ThingTypeUID THING_TYPE_BRIDGE = new ThingTypeUID(BINDING_ID, "bridge");

    // generic thing types
    public static final ThingTypeUID THING_TYPE_ON_OFF_LIGHT = new ThingTypeUID(BINDING_ID, "0000");
    public static final ThingTypeUID THING_TYPE_ON_OFF_PLUG = new ThingTypeUID(BINDING_ID, "0010");
    public static final ThingTypeUID THING_TYPE_COLOR_LIGHT = new ThingTypeUID(BINDING_ID, "0200");
    public static final ThingTypeUID THING_TYPE_COLOR_TEMPERATURE_LIGHT = new ThingTypeUID(BINDING_ID, "0220");
    public static final ThingTypeUID THING_TYPE_EXTENDED_COLOR_LIGHT = new ThingTypeUID(BINDING_ID, "0210");
    public static final ThingTypeUID THING_TYPE_DIMMABLE_LIGHT = new ThingTypeUID(BINDING_ID, "0100");
    public static final ThingTypeUID THING_TYPE_DIMMABLE_PLUG = new ThingTypeUID(BINDING_ID, "0110");

    // List all channels
    public static final String CHANNEL_COLORTEMPERATURE = "color_temperature";
    public static final String CHANNEL_COLOR = "color";
    public static final String CHANNEL_BRIGHTNESS = "brightness";
    public static final String CHANNEL_ALERT = "alert";
    public static final String CHANNEL_EFFECT = "effect";
    public static final String CHANNEL_SWITCH = "switch";

    // Bridge config properties
    public static final String HOST = "ipAddress";
    public static final String USER_NAME = "userName";
    public static final String POLLING_INTERVAL = "pollingInterval";

    // Light config properties
    public static final String LIGHT_ID = "lightId";
    public static final String LIGHT_UNIQUE_ID = "uniqueId";
    public static final String MODEL_ID = "modelId";

}
