package com.buildersbackpacks.keybind;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class KeybindInit {
	public static final KeyBinding backpackUIOpen = new KeyBinding("key.buildersbackpackopen", 90, "key.categories.buildersbackpacks");
	
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(backpackUIOpen);
	}
}
