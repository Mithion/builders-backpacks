package com.buildersbackpacks.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;

/**
 * 
 * @author gigaherz
 *
 */
public interface IRadialMenuHost extends IDrawingHelper
{
    Screen getScreen();

    FontRenderer getFontRenderer();

    ItemRenderer getItemRenderer();
}