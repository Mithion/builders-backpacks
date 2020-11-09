package com.buildersbackpacks.gui;

import com.buildersbackpacks.BuildersBackpacks;
import com.buildersbackpacks.gui.container.ContainerBuildersBackpack;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GuiBuildersBackpack extends ContainerScreen<ContainerBuildersBackpack>{
	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(BuildersBackpacks.MOD_ID, "textures/gui/builders_backpack.png");
	
	public GuiBuildersBackpack(ContainerBuildersBackpack book, PlayerInventory inv, ITextComponent comp) {
		super(book, inv, new StringTextComponent(""));
        this.xSize = 229;
        this.ySize = 152;
    }

	public ResourceLocation texture() {
		return GUI_TEXTURE;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {	}
	
	@Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX,
                                                   int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(texture());
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }
}
