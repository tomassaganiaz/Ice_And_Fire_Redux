package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import com.github.Redux.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** LayerTrollStone — Layer Troll Stone */

@SideOnly(Side.CLIENT)
public class LayerTrollStone implements LayerRenderer<EntityLivingBase> {

	private final RenderLiving<?> renderer;

	public LayerTrollStone(RenderLiving<?> renderer) {
		this.renderer = renderer;
	}

	private static final Map<UUID, StonedEntityCache> STONED_ENTITY_CACHE = new HashMap<>();
	private static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{
			new ResourceLocation("textures/blocks/destroy_stage_0.png"),
			new ResourceLocation("textures/blocks/destroy_stage_1.png"),
			new ResourceLocation("textures/blocks/destroy_stage_2.png"),
			new ResourceLocation("textures/blocks/destroy_stage_3.png"),
			new ResourceLocation("textures/blocks/destroy_stage_4.png"),
			new ResourceLocation("textures/blocks/destroy_stage_5.png"),
			new ResourceLocation("textures/blocks/destroy_stage_6.png"),
			new ResourceLocation("textures/blocks/destroy_stage_7.png"),
			new ResourceLocation("textures/blocks/destroy_stage_8.png"),
			new ResourceLocation("textures/blocks/destroy_stage_9.png")};

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if(entitylivingbaseIn instanceof EntityTroll) {
			EntityTroll troll = (EntityTroll)entitylivingbaseIn;
			if((((IEntityLivingBaseRenderContext)troll).iceAndFire$getStoned())) {
				UUID uuid = troll.getUniqueID();
				StonedEntityCache cache = STONED_ENTITY_CACHE.get(uuid);
				if(cache == null && !STONED_ENTITY_CACHE.containsKey(uuid)) {
					cache = new StonedEntityCache(troll.getType().TEXTURE_STONE, f, f1, f2, f3, f4, f5);
					STONED_ENTITY_CACHE.put(uuid, cache);
				}

				if(cache != null) {
					GlStateManager.pushMatrix();
					GlStateManager.depthMask(true);
					GlStateManager.enableCull();
					this.renderer.bindTexture(cache.texture);
					this.renderer.getMainModel().render(troll, cache.limbSwing, cache.limbSwingAmount, cache.ageInTicks, cache.netHeadYaw, cache.headPitch, cache.scale);
					GlStateManager.disableCull();
					GlStateManager.popMatrix();

					int breakData = ((IEntityLivingBaseRenderContext)troll).iceAndFire$getStonedData();
					if(breakData > 0) {
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
						this.renderer.bindTexture(DESTROY_STAGES[breakData - 1]);
						this.renderer.getMainModel().render(troll, cache.limbSwing, cache.limbSwingAmount, cache.ageInTicks, cache.netHeadYaw, cache.headPitch, cache.scale);
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
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	private static class StonedEntityCache {

		public final ResourceLocation texture;
		public final float limbSwing;
		public final float limbSwingAmount;
		public final float ageInTicks;
		public final float netHeadYaw;
		public final float headPitch;
		public final float scale;

		public StonedEntityCache(
				ResourceLocation texture,
				float limbSwing,
				float limbSwingAmount,
				float ageInTicks,
				float netHeadYaw,
				float headPitch,
				float scale) {
			this.texture = texture;
			this.limbSwing = limbSwing;
			this.limbSwingAmount = limbSwingAmount;
			this.ageInTicks = ageInTicks;
			this.netHeadYaw = netHeadYaw;
			this.headPitch = headPitch;
			this.scale = scale;
		}
	}
}
