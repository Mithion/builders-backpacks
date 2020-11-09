package com.buildersbackpacks.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class DelegatedItemUseContext extends ItemUseContext {

	public DelegatedItemUseContext(World worldIn, PlayerEntity player, Hand handIn, ItemStack heldItem, BlockRayTraceResult rayTraceResultIn) {
		super(worldIn, player, handIn, heldItem, rayTraceResultIn);
	}
	
}
