package com.drynesscalculator;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("drynesscalculator")
public interface DrynessCalculatorConfig extends Config{
    @ConfigItem
            (
                    position = 1,
                    keyName = "booleanConfig",
                    name = "Boolean Checkbox",
                    description = "Simple example of a checkbox to store a booleans value."
            )
    default boolean booleanConfig() { return false; }
}
