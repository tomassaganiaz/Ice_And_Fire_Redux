package com.github.Redux.iceandfire.client.render.tile;

import com.github.Redux.iceandfire.client.model.ModelTrollWeapon;
import com.github.Redux.iceandfire.item.ItemTrollWeapon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** IceAndFireTEISR — Ice And Fire TEISR */

@SideOnly(Side.CLIENT)
public class IceAndFireTEISR extends TileEntityItemStackRenderer {

	private static final ModelBase MODEL = new ModelTrollWeapon();

	@Override
	public void renderByItem(ItemStack itemStackIn) {
		if (itemStackIn.getItem() instanceof ItemTrollWeapon) {
			ItemTrollWeapon weaponItem = (ItemTrollWeapon) itemStackIn.getItem();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, -0.75F, 0.5F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(weaponItem.weapon.TEXTURE);
			MODEL.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}
	}

}
