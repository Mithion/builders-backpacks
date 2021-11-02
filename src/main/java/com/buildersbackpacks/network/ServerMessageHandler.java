package com.buildersbackpacks.network;

import java.util.function.Supplier;

import com.buildersbackpacks.BuildersBackpacks;
import com.buildersbackpacks.item.ItemBuildersBackpack;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerMessageHandler {
	private static <T extends BaseMessage> boolean validateBasics(T message, NetworkEvent.Context ctx) {		
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		
		if (sideReceived != LogicalSide.SERVER) {
			return false;
		}
		if (!message.isMessageValid()) {
			return false;
		}
		
		return true;
	}
	
	public static void handleBackpackSlotChangeMessage(final BackpackSlotChangeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		if (!validateBasics(message, ctx)) return;
		
		final ServerPlayerEntity sendingPlayer = ctx.getSender();
		if (sendingPlayer == null) {
		      return;
		}		
		
		ctx.enqueueWork(() -> {
			ItemStack stack = sendingPlayer.getMainHandItem();
			if (stack.getItem() == BuildersBackpacks.BUILDERS_BACKPACK) {
				ItemBuildersBackpack.setActiveIndex(stack, message.getIndex(), false);
			}
		});
	}
}
