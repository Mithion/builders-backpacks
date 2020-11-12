package com.buildersbackpacks.config;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class GeneralModConfig {
	public final IntValue reachIncrease;
	public final BooleanValue collectItems;

	public GeneralModConfig(ForgeConfigSpec.Builder builder) {
		reachIncrease = builder.comment("Increase the player's reach by this far when the builder's backpack is equipped.  Set to 0 to disable.").defineInRange("reachIncrease", 4, 0, 16);
		collectItems = builder.comment("When the player picks up an item, automatically merge it with items in the builder's bag if it already contains it.").define("collectItems", true);
	}
}
