package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.render.entity.layer.LayerSeaSerpentAncient;
import com.github.Redux.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Sea Serpent */

@SideOnly(Side.CLIENT)
public class RenderSeaSerpent extends RenderLiving<EntitySeaSerpent> {

	private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_blue.png");
	private static final ResourceLocation TEXTURE_BLUE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_blue_blink.png");
	private static final ResourceLocation TEXTURE_BRONZE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_bronze.png");
	private static final ResourceLocation TEXTURE_BRONZE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_bronze_blink.png");
	private static final ResourceLocation TEXTURE_DARKBLUE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_darkblue.png");
	private static final ResourceLocation TEXTURE_DARKBLUE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_darkblue_blink.png");
	private static final ResourceLocation TEXTURE_GREEN = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_green.png");
	private static final ResourceLocation TEXTURE_GREEN_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_green_blink.png");
	private static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_purple.png");
	private static final ResourceLocation TEXTURE_PURPLE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_purple_blink.png");
	private static final ResourceLocation TEXTURE_RED = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_red.png");
	private static final ResourceLocation TEXTURE_RED_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_red_blink.png");
	private static final ResourceLocation TEXTURE_TEAL = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_teal.png");
	private static final ResourceLocation TEXTURE_TEAL_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_teal_blink.png");

	public RenderSeaSerpent(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 1.6F);
		this.layerRenderers.add(new LayerSeaSerpentAncient(this));
	}

	@Override
	protected void preRenderCallback(EntitySeaSerpent entity, float f) {
		this.shadowSize = entity.getSeaSerpentScale();
		GlStateManager.scale(shadowSize, shadowSize, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySeaSerpent serpent) {
		switch(serpent.getVariant()) {
			case 0: return serpent.isBlinking() ? TEXTURE_BLUE_BLINK : TEXTURE_BLUE;
			case 1: return serpent.isBlinking() ? TEXTURE_BRONZE_BLINK : TEXTURE_BRONZE;
			case 2: return serpent.isBlinking() ? TEXTURE_DARKBLUE_BLINK : TEXTURE_DARKBLUE;
			case 3: return serpent.isBlinking() ? TEXTURE_GREEN_BLINK : TEXTURE_GREEN;
			case 4: return serpent.isBlinking() ? TEXTURE_PURPLE_BLINK : TEXTURE_PURPLE;
			case 5: return serpent.isBlinking() ? TEXTURE_RED_BLINK : TEXTURE_RED;
			case 6: return serpent.isBlinking() ? TEXTURE_TEAL_BLINK : TEXTURE_TEAL;
			default: return TEXTURE_BLUE;
		}
	}


}