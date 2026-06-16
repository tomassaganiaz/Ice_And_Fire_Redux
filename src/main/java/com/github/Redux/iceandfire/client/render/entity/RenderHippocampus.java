package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.model.ModelHippocampus;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippocampusArmor;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippocampusBridle;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippocampusChest;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippocampusRainbow;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippocampusSaddle;
import com.github.Redux.iceandfire.entity.EntityHippocampus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/** Renderizador de Hippocampus */

@SideOnly(Side.CLIENT)
public class RenderHippocampus extends RenderLiving<EntityHippocampus> {

	private static final ResourceLocation VARIANT_0 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_0.png");
	private static final ResourceLocation VARIANT_0_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_0_blinking.png");
	private static final ResourceLocation VARIANT_1 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_1.png");
	private static final ResourceLocation VARIANT_1_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_1_blinking.png");
	private static final ResourceLocation VARIANT_2 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_2.png");
	private static final ResourceLocation VARIANT_2_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_2_blinking.png");
	private static final ResourceLocation VARIANT_3 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_3.png");
	private static final ResourceLocation VARIANT_3_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_3_blinking.png");
	private static final ResourceLocation VARIANT_4 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_4.png");
	private static final ResourceLocation VARIANT_4_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_4_blinking.png");
	private static final ResourceLocation VARIANT_5 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_5.png");
	private static final ResourceLocation VARIANT_5_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_5_blinking.png");
	
	public RenderHippocampus(RenderManager renderManager) {
		super(renderManager, new ModelHippocampus(), 0.8F);
		this.layerRenderers.add(new LayerHippocampusSaddle(this));
		this.layerRenderers.add(new LayerHippocampusBridle(this));
		this.layerRenderers.add(new LayerHippocampusChest(this));
		this.layerRenderers.add(new LayerHippocampusRainbow(this));
		this.layerRenderers.add(new LayerHippocampusArmor(this));
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityHippocampus entity) {
		switch(entity.getVariant()){
			default:
				return entity.isBlinking() ? VARIANT_0_BLINK : VARIANT_0;
			case 1:
				return entity.isBlinking() ? VARIANT_1_BLINK : VARIANT_1;
			case 2:
				return entity.isBlinking() ? VARIANT_2_BLINK : VARIANT_2;
			case 3:
				return entity.isBlinking() ? VARIANT_3_BLINK : VARIANT_3;
			case 4:
				return entity.isBlinking() ? VARIANT_4_BLINK : VARIANT_4;
			case 5:
				return entity.isBlinking() ? VARIANT_5_BLINK : VARIANT_5;

		}
	}


}