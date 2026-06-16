package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.client.render.entity.RenderPixie;
import com.github.Redux.iceandfire.entity.EntityPixie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerPixieGlow — Layer Pixie Glow */

@SideOnly(Side.CLIENT)
public class LayerPixieGlow implements LayerRenderer<EntityPixie> {

	private final RenderLiving<EntityPixie> render;

	public LayerPixieGlow(RenderLiving<EntityPixie> renderIn) {
		this.render = renderIn;
	}

	@Override
	public void doRenderLayer(EntityPixie pixie, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(pixie);
		if (capability == null || !capability.isStoned()) {
			switch (pixie.getColor()) {
				default: this.render.bindTexture(RenderPixie.TEXTURE_0); break;
				case 1: this.render.bindTexture(RenderPixie.TEXTURE_1); break;
				case 2: this.render.bindTexture(RenderPixie.TEXTURE_2); break;
				case 3: this.render.bindTexture(RenderPixie.TEXTURE_3); break;
				case 4: this.render.bindTexture(RenderPixie.TEXTURE_4); break;
			}
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(!pixie.isInvisible());
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
			GlStateManager.enableLighting();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.render.getMainModel().render(pixie, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			this.render.setLightmap(pixie);
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
