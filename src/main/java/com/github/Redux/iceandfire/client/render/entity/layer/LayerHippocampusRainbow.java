package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.render.entity.RenderHippocampus;
import com.github.Redux.iceandfire.entity.EntityHippocampus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerHippocampusRainbow — Layer Hippocampus Rainbow */

@SideOnly(Side.CLIENT)
public class LayerHippocampusRainbow implements LayerRenderer<EntityHippocampus> {

	private final RenderHippocampus renderer;
	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/hippocampus/rainbow.png");
	private static final ResourceLocation TEXTURE_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/rainbow_blink.png");

	public LayerHippocampusRainbow(RenderHippocampus renderer) {
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityHippocampus entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){
		if (entitylivingbaseIn.hasCustomName() && entitylivingbaseIn.getCustomNameTag().toLowerCase().contains("rainbow")) {
			GlStateManager.pushMatrix();
			this.renderer.bindTexture(entitylivingbaseIn.isBlinking() ? TEXTURE_BLINK : TEXTURE);
			int i = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
			int j = EnumDyeColor.values().length;
			int k = i % j;
			int l = (i + 1) % j;
			float f = ((float)(entitylivingbaseIn.ticksExisted % 25) + partialTicks) / 25.0F;
			float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
			float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
			GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
			this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
