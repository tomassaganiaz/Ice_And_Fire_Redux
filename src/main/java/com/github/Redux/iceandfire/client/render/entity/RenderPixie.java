package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.model.ModelPixie;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerPixieGlow;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerPixieItem;
import com.github.Redux.iceandfire.entity.EntityPixie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Pixie */

@SideOnly(Side.CLIENT)
public class RenderPixie extends RenderLiving<EntityPixie> {

	public static final ModelPixie PIXIE_MODEL = new ModelPixie();
	public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_0.png");
	public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_1.png");
	public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_2.png");
	public static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_3.png");
	public static final ResourceLocation TEXTURE_4 = new ResourceLocation("iceandfire:textures/models/pixie/pixie_4.png");

	public RenderPixie(RenderManager renderManager) {
		super(renderManager, PIXIE_MODEL, 0.2F);
		this.layerRenderers.add(new LayerPixieItem(this));
		this.layerRenderers.add(new LayerPixieGlow(this));
	}

	@Override
	public void preRenderCallback(EntityPixie entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(0.55F, 0.55F, 0.55F);
		if (entitylivingbaseIn.isSitting()) {
			GlStateManager.translate(0F, 0.5F, 0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPixie pixie) {
		switch (pixie.getColor()) {
			default: return TEXTURE_0;
			case 1: return TEXTURE_1;
			case 2: return TEXTURE_2;
			case 3: return TEXTURE_3;
			case 4: return TEXTURE_4;
		}
	}


}