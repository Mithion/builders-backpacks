package com.buildersbackpacks.gui.container.provider;

import com.buildersbackpacks.BuildersBackpacks;
import com.buildersbackpacks.gui.container.ContainerBuildersBackpack;
import com.buildersbackpacks.inventory.InventoryBuildersBackpack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class NamedContainerBackpack implements INamedContainerProvider {

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new ContainerBuildersBackpack(id, playerInventory, InventoryBuildersBackpack.fromItemStack(player.getHeldItemMainhand()));
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent(BuildersBackpacks.MOD_ID + ".builders_backpack");
	}

}
