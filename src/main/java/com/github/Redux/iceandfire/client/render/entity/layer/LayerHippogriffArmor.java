package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.render.entity.RenderHippogryph;
import com.github.Redux.iceandfire.entity.EntityHippogryph;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerHippogriffArmor — Layer Hippogriff Armor */

@SideOnly(Side.CLIENT)
public class LayerHippogriffArmor implements LayerRenderer {

	private final RenderHippogryph renderer;
	private final ResourceLocation TEXTURE_DIAMOND = new ResourceLocation("iceandfire:textures/models/hippogryph/armor_diamond.png");
	private final ResourceLocation TEXTURE_GOLD = new ResourceLocation("iceandfire:textures/models/hippogryph/armor_gold.png");
	private final ResourceLocation TEXTURE_IRON = new ResourceLocation("iceandfire:textures/models/hippogryph/armor_iron.png");

	public LayerHippogriffArmor(RenderHippogryph renderer) {
		this.renderer = renderer;
	}

	public void doRenderLayer(EntityHippogryph entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if (entity.getArmor() != 0) {
			switch (entity.getArmor()) {
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
			this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
		this.doRenderLayer((EntityHippogryph) entity, f, f1, f2, f3, f4, f5, f6);
	}
}
