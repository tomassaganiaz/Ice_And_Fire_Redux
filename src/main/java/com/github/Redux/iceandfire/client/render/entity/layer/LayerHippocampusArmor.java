package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.render.entity.RenderHippocampus;
import com.github.Redux.iceandfire.entity.EntityHippocampus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerHippocampusArmor — Layer Hippocampus Armor */

@SideOnly(Side.CLIENT)
public class LayerHippocampusArmor implements LayerRenderer<EntityHippocampus> {

	private final RenderHippocampus renderer;
	private static final ResourceLocation TEXTURE_DIAMOND = new ResourceLocation("iceandfire:textures/models/hippocampus/armor_diamond.png");
	private static final ResourceLocation TEXTURE_GOLD = new ResourceLocation("iceandfire:textures/models/hippocampus/armor_gold.png");
	private static final ResourceLocation TEXTURE_IRON = new ResourceLocation("iceandfire:textures/models/hippocampus/armor_iron.png");

	public LayerHippocampusArmor(RenderHippocampus renderer) {
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if (entity.getArmor() != 0) {
			GlStateManager.pushMatrix();
			switch(entity.getArmor()) {
				case 1:
					this.renderer.bindTexture(TEXTURE_IRON);
					break;
				case 2:
					this.renderer.bindTexture(TEXTURE_GOLD);
					break;
				case 3:
					this.renderer.bindTexture(TEXTURE_DIAMOND);
					break;
			}
			GlStateManager.color(1F, 1F, 1F);
			this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
