package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.model.ModelStonePlayer;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerStonePlayerEntityCrack;
import com.github.Redux.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/** Renderizador de Stone Statue */

@SideOnly(Side.CLIENT)
public class RenderStoneStatue extends RenderLiving<EntityStoneStatue> {

	private static final ModelStonePlayer MODEL = new ModelStonePlayer(0, false);
	private static final ModelStonePlayer MODEL_SLIM = new ModelStonePlayer(0, true);

	public RenderStoneStatue(RenderManager renderManager) {
		super(renderManager, MODEL, 0.5F);
		this.layerRenderers.add(new LayerStonePlayerEntityCrack(this));
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelStonePlayer(0.5F, true);
				this.modelArmor = new ModelStonePlayer(1.0F, true);
			}
		};
		this.addLayer(layerbipedarmor);
	}

	@Override
	public ModelBase getMainModel() {
		return MODEL;
	}

	@Override
	protected void renderModel(EntityStoneStatue entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		boolean flag = !entity.isInvisible() || this.renderOutlines;
		boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);

		if (flag || flag1) {
			if (!this.bindEntityTexture(entity)) {
				return;
			}
			if (flag1) {
				GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
			if (entity.smallArms) {
				MODEL_SLIM.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			} else {
				MODEL.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			}
			if (flag1) {
				GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
		}
	}

	private String getStoneType(ModelBase model) {
		int sizeX = clampTexture(model.textureWidth);
		int sizeY = clampTexture(model.textureHeight);
		if(sizeX > sizeY && sizeX/2 != sizeY) sizeY = sizeX/2;
		if(sizeY > sizeX && sizeY/2 != sizeX) sizeX = sizeY/2;
		return sizeX <= 16 && sizeY <= 16 ? "textures/blocks/stone.png" : "iceandfire:textures/models/gorgon/stone" + sizeX + "x" + sizeY + ".png";
	}

	private int clampTexture(int i) {
		if(i >= 128) return 128;
		if(i >= 64) return 64;
		if(i >= 32) return 32;
		return 16;
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityStoneStatue entity) {
		return new ResourceLocation(getStoneType(entity.smallArms ? MODEL : MODEL_SLIM));
	}


}