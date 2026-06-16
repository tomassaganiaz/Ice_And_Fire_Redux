package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.entity.EntityGorgon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerGorgonEyes — Layer Gorgon Eyes */

@SideOnly(Side.CLIENT)
public class LayerGorgonEyes implements LayerRenderer<EntityGorgon> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_eyes.png");
	private final RenderLiving<EntityGorgon> render;

	public LayerGorgonEyes(RenderLiving<EntityGorgon> renderIn) {
		this.render = renderIn;
	}

	@Override
	public void doRenderLayer(EntityGorgon gorgon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (gorgon.getAnimation() == EntityGorgon.ANIMATION_SCARE || gorgon.getAnimation() == EntityGorgon.ANIMATION_HIT) {
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(gorgon);
			if (capability == null || !capability.isStoned()) {
				this.render.bindTexture(TEXTURE);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
				GlStateManager.disableLighting();
				GlStateManager.depthMask(!gorgon.isInvisible());
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
				GlStateManager.enableLighting();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.render.getMainModel().render(gorgon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
				this.render.setLightmap(gorgon);
				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
