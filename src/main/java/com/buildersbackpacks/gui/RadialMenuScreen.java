package com.buildersbackpacks.gui;

import java.util.List;

import com.buildersbackpacks.events.ClientEventHandler;
import com.buildersbackpacks.inventory.InventoryBuildersBackpack;
import com.buildersbackpacks.item.ItemBuildersBackpack;
import com.buildersbackpacks.keybind.KeybindInit;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author gigaherz
 *
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class RadialMenuScreen extends Screen
{
    private ItemStack stackEquipped;
    private InventoryBuildersBackpack inventory;

    private boolean needsRecheckStacks = true;
    private final List<ItemStackRadialMenuItem> cachedMenuItems = Lists.newArrayList();    
    private final GenericRadialMenu menu;
    private Minecraft mc;

    public RadialMenuScreen()
    {
        super(new StringTextComponent("RADIAL MENU"));
        
        mc = Minecraft.getInstance();        
        
        this.stackEquipped = mc.player.getHeldItemMainhand();
        if (this.stackEquipped.getItem() instanceof ItemBuildersBackpack)
        {
        	inventory = InventoryBuildersBackpack.fromItemStack(this.stackEquipped);
        } else {
        	this.closeScreen();
        }
                
        menu = new GenericRadialMenu(Minecraft.getInstance(), new IRadialMenuHost()
        {
            @Override
            public void renderTooltip(MatrixStack matrixStack, ItemStack stack, int mouseX, int mouseY)
            {
                RadialMenuScreen.this.renderTooltip(matrixStack, stack, mouseX, mouseY);
            }

            @Override
            public Screen getScreen()
            {
                return RadialMenuScreen.this;
            }

            @Override
            public FontRenderer getFontRenderer()
            {
                return font;
            }

            @Override
            public ItemRenderer getItemRenderer()
            {
                return RadialMenuScreen.this.itemRenderer;
            }
        })
        {
            @Override
            public void onClickOutside()
            {
                close();
            }
        };
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.currentScreen instanceof RadialMenuScreen)
        {
            event.setCanceled(true);
        }
    }

    @Override // removed
    public void onClose()
    {
        super.onClose();
        ClientEventHandler.wipeOpen();
    }

    @Override // tick
    public void tick()
    {
        super.tick();

        menu.tick();

        if (menu.isClosed())
        {
            Minecraft.getInstance().displayGuiScreen(null);
            ClientEventHandler.wipeOpen();
        }
        if (!menu.isReady() || inventory == null)
        {
            return;
        }

        ItemStack inHand = minecraft.player.getHeldItemMainhand();
        if (!(inHand.getItem() instanceof ItemBuildersBackpack))
        {
            inventory = null;
        }
        else
        {
            ItemStack stack = inHand;
            if (stack.getCount() <= 0)
            {
                inventory = null;
                stackEquipped = null;
            }
            // Reference comparison intended
            else if (stackEquipped != stack)
            {
               menu.close();
            }
        }

        if (inventory == null)
        {
            Minecraft.getInstance().displayGuiScreen(null);
        }
        else if (!ClientEventHandler.isKeyDown(KeybindInit.backpackUIOpen))
        {        	
//            if (ConfigData.releaseToSwap)
//            {
                processClick(false);
//            }
//            else
//            {
//                menu.close();
//            }
        }
    }

    @Override // mouseReleased
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_)
    {
        processClick(true);
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    protected void processClick(boolean triggeredByMouse)
    {
        menu.clickItem();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (inventory == null)
            return;

        ItemStack inHand = minecraft.player.getHeldItemMainhand();
        if (!(inHand.getItem() instanceof ItemBuildersBackpack))
        	return;

        if (needsRecheckStacks)
        {
            cachedMenuItems.clear();                        
            for (int i = 0; i < inventory.getSizeInventory(); ++i)
            {
            	ItemStack inSlot = inventory.getStackInSlot(i);
            	final int index = i;
                ItemStackRadialMenuItem item = new ItemStackRadialMenuItem(menu, i, inSlot, new TranslationTextComponent("gui.buildersbackpacks.backpack.empty"))
                {
                    @Override
                    public boolean onClick()
                    {
                    	ItemBuildersBackpack.setActiveIndex(inHand, index, true);
                    	menu.close();
                    	return true;
                    }
                };                
                item.setVisible(true);
                cachedMenuItems.add(item);
            }

            menu.clear();
            menu.addAll(cachedMenuItems);

            needsRecheckStacks = false;
        }

        if (cachedMenuItems.stream().noneMatch(RadialMenuItem::isVisible))
        {
            menu.setCentralText(new TranslationTextComponent("gui.buildersbackpacks.backpack.empty"));
        }
        else
        {
            menu.setCentralText(null);
        }

        menu.draw(matrixStack, partialTicks, mouseX, mouseY);
    }

    @Override // isPauseScreen
    public boolean isPauseScreen()
    {
        return false;
    }
}