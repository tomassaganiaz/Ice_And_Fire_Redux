package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.client.model.ModelGorgon;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerGorgonEyes;
import com.github.Redux.iceandfire.entity.EntityGorgon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Gorgon */

@SideOnly(Side.CLIENT)
public class RenderGorgon extends RenderLiving<EntityGorgon> {

	private static final ResourceLocation PASSIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_passive.png");
	private static final ResourceLocation AGRESSIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_active.png");
	private static final ResourceLocation DEAD_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_decapitated.png");

	public RenderGorgon(RenderManager renderManager) {
		super(renderManager, new ModelGorgon(), 0.6F);
		this.layerRenderers.add(new LayerGorgonEyes(this));
	}

	@Override
	public void preRenderCallback(EntityGorgon entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(0.85F, 0.85F, 0.85F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGorgon gorgon) {
		if (gorgon.getAnimation() == EntityGorgon.ANIMATION_SCARE) {
			return AGRESSIVE_TEXTURE;
		} else if (gorgon.deathTime > 0) {
			return DEAD_TEXTURE;
		} else {
			return PASSIVE_TEXTURE;
		}
	}


}