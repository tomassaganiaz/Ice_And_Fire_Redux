package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.render.entity.RenderHippogryph;
import com.github.Redux.iceandfire.entity.EntityHippogryph;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerHippogriffBridle — Layer Hippogriff Bridle */

@SideOnly(Side.CLIENT)
public class LayerHippogriffBridle implements LayerRenderer {

	private final RenderHippogryph renderer;
	private final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/hippogryph/bridle.png");

	public LayerHippogriffBridle(RenderHippogryph renderer) {
		this.renderer = renderer;
	}

	public void doRenderLayer(EntityHippogryph entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if (entity.isSaddled() && entity.getControllingPassenger() != null) {
			this.renderer.bindTexture(TEXTURE);
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
