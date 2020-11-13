package com.buildersbackpacks.gui.container;

import com.buildersbackpacks.gui.container.slot.BlockSlot;
import com.buildersbackpacks.inventory.InventoryBuildersBackpack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBuildersBackpack extends Container{

	private InventoryBuildersBackpack ibb;
	private int mySlot;
	private int myPlayerIndex;
	private ItemStack myStack;
	
	public ContainerBuildersBackpack(int id, PlayerInventory player) {
		this(id, player, InventoryBuildersBackpack.fromItemStack(player.player.getHeldItemMainhand()));
	}
	
	public ContainerBuildersBackpack(int id, PlayerInventory player, InventoryBuildersBackpack ibb) {
		super(ContainerInit.BUILDERS_BACKPACK, id);
		this.ibb = ibb;
		myStack = player.player.getHeldItemMainhand();
		myPlayerIndex = player.currentItem;
		initializeSlots(player);
	}
	
	protected void initializeSlots(PlayerInventory playerInv) {
		int slotIndex = 0;
		
    	for (int k = 0; k < slotsPerRow(); ++k) {
    		for (int j = 0; j < numRows(); ++j) {            
            	this.addSlot(new BlockSlot(ibb, k + j * slotsPerRow(), 8 + k * 18, 27 + j * 18));
            	slotIndex++;
            }
        }

        int i = (numRows() - 4) * 18;
        //player inventory
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInv, j1 + l * 9 + 9, 34 + j1 * 18, 124 + l * 18 + i));
                slotIndex++;
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
        	if (i1 == playerInv.currentItem)
        		mySlot = slotIndex;
            this.addSlot(new Slot(playerInv, i1, 34 + i1 * 18, 182 + i));
            slotIndex++;
        }
    }
	
	protected int slotsPerRow() {
    	return 12;
    }

	protected int numRows() {
		return 1;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
	
	@Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        ibb.writeToItemStack(myStack);
	}

	@Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {    	
        if (slotId == mySlot || (clickTypeIn == ClickType.SWAP && dragType == myPlayerIndex))
        	return ItemStack.EMPTY;

        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

	
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < numRows() * slotsPerRow()) {
                if (!this.mergeItemStack(itemstack1, numRows() * slotsPerRow(), this.inventorySlots
                        .size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, numRows() * slotsPerRow(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
