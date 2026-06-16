package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerStonePlayerEntityCrack — Layer Stone Player Entity Crack */

@SideOnly(Side.CLIENT)
public class LayerStonePlayerEntityCrack implements LayerRenderer<EntityStoneStatue> {

	private static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png")};
	private final RenderLiving<EntityStoneStatue> renderer;

	public LayerStonePlayerEntityCrack(RenderLiving<EntityStoneStatue> renderer) {
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityStoneStatue stoneStatue, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		int breakCount = stoneStatue.getCrackAmount();
		if(breakCount > 0) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.depthFunc(514);
			GlStateManager.depthMask(false);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.scale((float)this.renderer.getMainModel().textureHeight / 16.0F, (float)this.renderer.getMainModel().textureWidth / 16.0F, 1.0F);
			GlStateManager.matrixMode(5888);
			this.renderer.bindTexture(DESTROY_STAGES[breakCount - 1]);
			this.renderer.getMainModel().render(stoneStatue, f, f1, f2, f3, f4, f5);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.depthMask(true);
			GlStateManager.depthFunc(515);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}
