package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.model.ModelHippogryph;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippogriffArmor;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippogriffBridle;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippogriffChest;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerHippogriffSaddle;
import com.github.Redux.iceandfire.entity.EntityHippogryph;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/** Renderizador de Hippogryph */

@SideOnly(Side.CLIENT)
public class RenderHippogryph extends RenderLiving<EntityHippogryph> {

	public RenderHippogryph(RenderManager renderManager) {
		super(renderManager, new ModelHippogryph(), 0.8F);
		this.layerRenderers.add(new LayerHippogriffSaddle(this));
		this.layerRenderers.add(new LayerHippogriffBridle(this));
		this.layerRenderers.add(new LayerHippogriffChest(this));
		this.layerRenderers.add(new LayerHippogriffArmor(this));

	}

	protected void preRenderCallback(EntityHippogryph entity, float partialTickTime) {
		GlStateManager.scale(1.2F, 1.2F, 1.2F);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityHippogryph entity) {
		return entity.isBlinking() ? entity.getEnumVariant().TEXTURE_BLINK : entity.getEnumVariant().TEXTURE;
	}


}
