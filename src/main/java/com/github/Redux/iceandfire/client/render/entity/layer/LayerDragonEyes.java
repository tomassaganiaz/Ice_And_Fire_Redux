package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.render.entity.RenderDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonTextures;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerDragonEyes — Layer Dragon Eyes */

@SideOnly(Side.CLIENT)
public class LayerDragonEyes implements LayerRenderer<EntityDragonBase> {

	private final RenderLiving<EntityDragonBase> render;

	public LayerDragonEyes(RenderLiving<EntityDragonBase> renderIn) {
		this.render = renderIn;
	}

	@Override
	public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (dragon.shouldRenderEyes()) {
			this.render.bindTexture(EnumDragonTextures.getEyeTextureFromDragon(dragon));
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

			GlStateManager.depthMask(!dragon.isInvisible());

			int i = 61680;
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			this.render.getMainModel().render(dragon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			i = dragon.getBrightnessForRender();
			j = i % 65536;
			k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
			this.render.setLightmap(dragon);
			GlStateManager.disableBlend();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}
