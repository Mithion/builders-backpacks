package com.buildersbackpacks.item;

import com.buildersbackpacks.gui.container.provider.NamedContainerBackpack;
import com.buildersbackpacks.inventory.InventoryBuildersBackpack;
import com.buildersbackpacks.network.BackpackSlotChangeMessage;
import com.buildersbackpacks.network.NetworkInit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemBuildersBackpack extends Item {

	public static final String KEY_INDEX = "index";	
	
	public ItemBuildersBackpack() {
		super(new Properties().maxStackSize(1).group(ItemGroup.TOOLS));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isSneaking()) {
			if (!worldIn.isRemote)
				NetworkHooks.openGui((ServerPlayerEntity)playerIn, new NamedContainerBackpack());
			return ActionResult.resultSuccess(playerIn.getHeldItemMainhand());
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
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
			ActionResultType result = selected.getItem().onItemUse(delegated);				
			
			if (result.isSuccessOrConsume()) {
				//store changes
				if (!context.getWorld().isRemote()) {
					ibb.setInventorySlotContents(curIndex, selected);
					ibb.writeToItemStack(backPackStack);
				}
			}
		}
		
		return ActionResultType.SUCCESS;
	}
	
	
	public static int getActiveIndex(ItemStack backPackStack) {
		CompoundNBT tag = backPackStack.getOrCreateTag();
		if (!tag.contains(KEY_INDEX))
			return 0;
		
		return tag.getInt(KEY_INDEX);
	}
	
	public static void setActiveIndex(ItemStack backPackStack, int index, boolean remote) {
		CompoundNBT tag = backPackStack.getOrCreateTag();
		tag.putInt(KEY_INDEX, index);
		backPackStack.setTag(tag);
		
		if (remote) {
			sendActiveIndexChange(index);
			Minecraft m = Minecraft.getInstance();
			m.ingameGUI.remainingHighlightTicks = 20;
		}
	}
	
	public static void sendActiveIndexChange(int newIndex) {
		NetworkInit.network.sendTo(
				new BackpackSlotChangeMessage(newIndex),
				Minecraft.getInstance().getConnection().getNetworkManager(),
				NetworkDirection.PLAY_TO_SERVER
			);
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		TranslationTextComponent base = (TranslationTextComponent) super.getDisplayName(stack);
		int index = getActiveIndex(stack);
		InventoryBuildersBackpack ibb = InventoryBuildersBackpack.fromItemStack(stack);
		ItemStack selected = ibb.getStackInSlot(index);
		base.append(new StringTextComponent(": "));
		if (!selected.isEmpty()) {			
			base.append(selected.getDisplayName());
			base.append(new StringTextComponent(String.format(" (x%d)", selected.getCount())));
		}else {
			base.append(new TranslationTextComponent("gui.buildersbackpacks.backpack.empty"));
		}
		
		return base;
	}
}
