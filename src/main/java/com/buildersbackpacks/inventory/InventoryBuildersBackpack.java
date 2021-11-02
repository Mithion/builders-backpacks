package com.buildersbackpacks.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class InventoryBuildersBackpack extends Inventory {
	private static final int NUM_SLOTS = 12;
	private static final String KEY_INVENTORY = "inventory";

	public InventoryBuildersBackpack() {
		super(NUM_SLOTS);
	}

	@Override
	public int getContainerSize() {
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

	public void read(ListNBT p_70486_1_) {
		for (int i = 0; i < p_70486_1_.size(); ++i) {
			ItemStack itemstack = ItemStack.of(p_70486_1_.getCompound(i));
			this.setItem(i, itemstack);
		}

	}

	public ListNBT write() {
		ListNBT listnbt = new ListNBT();

		for (int i = 0; i < this.getContainerSize(); ++i) {
			ItemStack itemstack = this.getItem(i);			
			listnbt.add(itemstack.save(new CompoundNBT()));			
		}

		return listnbt;
	}
}
