package com.buildersbackpacks.events;

import com.buildersbackpacks.BuildersBackpacks;
import com.buildersbackpacks.config.GeneralModConfig;
import com.buildersbackpacks.inventory.InventoryBuildersBackpack;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = BuildersBackpacks.MOD_ID, bus = Bus.FORGE)
public class CommonEventHandler {
	private static final AttributeModifier REACH_INCREASE = new AttributeModifier("builders_backpack_reach_increase", BuildersBackpacks.CONFIG.reachIncrease.get(), Operation.ADDITION);
	
	@SubscribeEvent
	public static void onPlayerPickupItem(EntityItemPickupEvent event) {
		if (!BuildersBackpacks.CONFIG.collectItems.get()) {
			return;
		}
		
		ItemStack pickedUpItem = event.getItem().getItem();
		Item item = pickedUpItem.getItem();
		int originalQty = pickedUpItem.getCount();
		if (!(item instanceof BlockItem)) {
			return;
		}
		
		boolean collectedItem = false;
		
		PlayerInventory inv = event.getPlayer().inventory;
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getItem() == BuildersBackpacks.BUILDERS_BACKPACK) {
				InventoryBuildersBackpack ibb = InventoryBuildersBackpack.fromItemStack(stack);
				int preCount = pickedUpItem.getCount();
				pickedUpItem = ibb.addItem(pickedUpItem);						
				if (preCount != pickedUpItem.getCount()) {
					collectedItem = true;
					ibb.writeToItemStack(stack);
				}
			}
		}		
		if (collectedItem) {
			event.setCanceled(true);
			if (!pickedUpItem.isEmpty()) {
				event.getItem().setItem(pickedUpItem);
				ForgeEventFactory.onItemPickup(event.getItem(), event.getPlayer());
			}else {
				event.getPlayer().onItemPickup(event.getItem(), originalQty);
				event.getItem().remove();
				
			}
		}
	}	
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event) {
		int reachIncrease = BuildersBackpacks.CONFIG.reachIncrease.get();
		if (reachIncrease == 0)
			return;
		
		ItemStack stack = event.player.getHeldItemMainhand();
		ModifiableAttributeInstance attr = event.player.getAttributeManager().createInstanceIfAbsent(ForgeMod.REACH_DISTANCE.get());
		
		if (stack.getItem() == BuildersBackpacks.BUILDERS_BACKPACK) {
			if (!attr.hasModifier(REACH_INCREASE))
				attr.applyPersistentModifier(REACH_INCREASE);
		}else {
			if (attr.hasModifier(REACH_INCREASE))
				attr.removeModifier(REACH_INCREASE);
		}
	}
}
