package com.buildersbackpacks.gui;


import com.buildersbackpacks.gui.container.ContainerInit;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class GuiInit {
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		ScreenManager.register(ContainerInit.BUILDERS_BACKPACK, GuiBuildersBackpack::new);
	}
}
