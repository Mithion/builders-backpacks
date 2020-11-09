package com.buildersbackpacks.gui.container;

import com.buildersbackpacks.inventory.InventoryBuildersBackpack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

public class ContainerBuildersBackpack extends Container{

	private InventoryBuildersBackpack ibb;
	
	public ContainerBuildersBackpack(int id, PlayerInventory player) {
		this(id, player, InventoryBuildersBackpack.fromItemStack(player.player.getHeldItemMainhand()));
	}
	
	public ContainerBuildersBackpack(int id, PlayerInventory player, InventoryBuildersBackpack ibb) {
		super(ContainerInit.BUILDERS_BACKPACK, id);
		this.ibb = ibb;
		initializeSlots(player);
	}
	
	protected void initializeSlots(PlayerInventory playerInv) {
    	for (int k = 0; k < slotsPerRow(); ++k) {
    		for (int j = 0; j < numRows(); ++j) {            
            	this.addSlot(new Slot(ibb, k + j * slotsPerRow(), 8 + k * 18, 13 + j * 18));
            }
        }

        int i = (numRows() - 4) * 18;
        //player inventory
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInv, j1 + l * 9 + 9, 8 + j1 * 18, 94 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, 152 + i));
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

}
