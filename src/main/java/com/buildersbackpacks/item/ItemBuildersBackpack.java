package com.buildersbackpacks.item;

import com.buildersbackpacks.inventory.InventoryBuildersBackpack;
import com.buildersbackpacks.network.BackpackSlotChangeMessage;
import com.buildersbackpacks.network.NetworkInit;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.fml.network.NetworkDirection;

public class ItemBuildersBackpack extends Item {

	public static final String KEY_INDEX = "index";	
	
	public ItemBuildersBackpack() {
		super(new Properties().maxStackSize(1).group(ItemGroup.TOOLS));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote()) {
			ItemStack backPackStack = context.getItem();
			InventoryBuildersBackpack ibb = InventoryBuildersBackpack.fromItemStack(backPackStack);
			int curIndex = getActiveIndex(backPackStack);
			if (curIndex < 0 || curIndex >= ibb.getSizeInventory())
				curIndex = 0;
			ItemStack selected = ibb.getStackInSlot(curIndex);			
			if (!selected.isEmpty()) {
				//delegate item use
				BlockRayTraceResult brtr = new BlockRayTraceResult(context.getHitVec(), context.getFace(), context.getPos(), context.isInside());
				DelegatedItemUseContext delegated = new DelegatedItemUseContext(context.getWorld(), context.getPlayer(), context.getHand(), selected, brtr);
				selected.getItem().onItemUse(delegated);
				
				//store changes
				ibb.setInventorySlotContents(curIndex, selected);
				ibb.writeToItemStack(backPackStack);
			}
		}
		
		return ActionResultType.SUCCESS;
	}
	
	
	public static int getActiveIndex(ItemStack backPackStack) {
		CompoundNBT tag = backPackStack.getOrCreateChildTag("backpack");
		if (!tag.contains(KEY_INDEX))
			return 0;
		
		return tag.getInt(KEY_INDEX);
	}
	
	public static void setActiveIndex(ItemStack backPackStack, int index, boolean remote) {
		CompoundNBT tag = backPackStack.getOrCreateTag();
		tag.putInt(KEY_INDEX, index);
		
		if (remote) {
			sendActiveIndexChange(index);
		}
	}
	
	public static void sendActiveIndexChange(int newIndex) {
		NetworkInit.network.sendTo(
				new BackpackSlotChangeMessage(newIndex),
				Minecraft.getInstance().getConnection().getNetworkManager(),
				NetworkDirection.PLAY_TO_SERVER
			);
	}
}
