package com.buildersbackpacks.network;

import com.buildersbackpacks.BuildersBackpacks;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = BuildersBackpacks.MOD_ID, bus = Bus.MOD)
public class NetworkInit {

	static final String PROTOCOL_VERSION = "1";

	public static final SimpleChannel network = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(BuildersBackpacks.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		int packet_id = 1;
		network.registerMessage(packet_id++, BackpackSlotChangeMessage.class, BackpackSlotChangeMessage::encode,
				BackpackSlotChangeMessage::decode, ServerMessageHandler::handleBackpackSlotChangeMessage);
	}
}
