package com.buildersbackpacks.gui.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class BlockSlot extends Slot{

	public BlockSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof BlockItem;
	}
}
