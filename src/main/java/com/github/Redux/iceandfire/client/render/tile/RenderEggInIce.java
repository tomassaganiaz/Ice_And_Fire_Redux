package com.github.Redux.iceandfire.client.render.tile;

import com.github.Redux.iceandfire.client.model.ModelDragonEgg;
import com.github.Redux.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.Redux.iceandfire.enums.EnumDragonEgg;
import com.github.Redux.iceandfire.enums.EnumDragonType;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Egg In Ice */

@SideOnly(Side.CLIENT)
public class RenderEggInIce extends TileEntitySpecialRenderer<TileEntityEggInIce> {

	private static final ModelDragonEgg MODEL = new ModelDragonEgg();

	@Override
	public void render(TileEntityEggInIce te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.type != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y - 0.75D, z + 0.5D);
			EnumDragonEgg eggType = te.type.dragonType != EnumDragonType.ICE ? EnumDragonEgg.BLUE : te.type;
			this.bindTexture(RenderPodium.getEggTexture(eggType));
			MODEL.renderFrozen();
			GlStateManager.popMatrix();
		}
	}

}
