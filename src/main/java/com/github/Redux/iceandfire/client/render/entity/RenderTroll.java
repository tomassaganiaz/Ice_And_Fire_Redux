package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.model.ModelTroll;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerTrollEyes;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerTrollStone;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerTrollWeapon;
import com.github.Redux.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Troll */

@SideOnly(Side.CLIENT)
public class RenderTroll extends RenderLiving<EntityTroll> implements ICustomStoneLayer {

	public RenderTroll(RenderManager renderManager) {
		super(renderManager, new ModelTroll(), 0.9F);
		this.layerRenderers.add(new LayerTrollWeapon(this));
		this.layerRenderers.add(new LayerTrollEyes(this));
	}

	@Override
	public void preRenderCallback(EntityTroll entitylivingbaseIn, float partialTickTime) { }

	@Override
	protected ResourceLocation getEntityTexture(EntityTroll troll) {
		return troll.getType().TEXTURE;
	}

	@Override
	public LayerRenderer<EntityLivingBase> getStoneLayer(RenderLiving<?> render) {
		return new LayerTrollStone(render);
	}
}