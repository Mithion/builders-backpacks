package com.buildersbackpacks.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.item.ItemStack;

/**
 * 
 * @author gigaherz
 *
 */
public interface IDrawingHelper
{
    void renderTooltip(MatrixStack matrixStack, ItemStack stack, int mouseX, int mouseY);
}