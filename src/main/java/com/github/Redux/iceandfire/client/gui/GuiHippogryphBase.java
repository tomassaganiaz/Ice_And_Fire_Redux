package com.github.Redux.iceandfire.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Interfaz gráfica de Hippogryph Base */

@SideOnly(Side.CLIENT)
public abstract class GuiHippogryphBase<T extends EntityLivingBase> extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/hippogryph.png");
	private final IInventory playerInventory;
	private final IInventory entityInventory;
	protected final T entity;
	private float mousePosx;
	private float mousePosY;

	public GuiHippogryphBase(Container container, IInventory playerInv, IInventory entityInv, T entity) {
		super(container);
		this.playerInventory = playerInv;
		this.entityInventory = entityInv;
		this.entity = entity;
		this.allowUserInput = false;
	}

	protected abstract boolean isEntityChested();

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(this.entityInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		if (isEntityChested()) {
			this.drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, 5 * 18, 54);
		}
		GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, (float) (i + 51) - this.mousePosx, (float) (j + 75 - 50) - this.mousePosY, this.entity);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.mousePosx = (float) mouseX;
		this.mousePosY = (float) mouseY;
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}