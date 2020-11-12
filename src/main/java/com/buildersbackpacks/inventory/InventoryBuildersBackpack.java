package com.buildersbackpacks.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class InventoryBuildersBackpack extends Inventory{
	private static final int NUM_SLOTS = 12;
	private static final String KEY_INVENTORY = "inventory";
	
	public InventoryBuildersBackpack() {
		super(NUM_SLOTS);
	}
	
	@Override
	public int getSizeInventory() {
		return NUM_SLOTS;
	}
	
	public void writeToItemStack(ItemStack stack) {
		CompoundNBT stackTag = stack.getOrCreateTag();
		ListNBT list = this.write();
		stackTag.put(KEY_INVENTORY, list);
	}	
	
	public static InventoryBuildersBackpack fromItemStack(ItemStack stack) {
		CompoundNBT stackTag = stack.getOrCreateTag();
		InventoryBuildersBackpack ibb = new InventoryBuildersBackpack();
		if (stackTag.contains(KEY_INVENTORY)) {
			ListNBT list = stackTag.getList(KEY_INVENTORY, 10);			
			ibb.read(list);
		}
		return ibb;
	}	
}
