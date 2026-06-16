package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.entity.projectile.EntityAmphithereArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Amphithere Arrow */

@SideOnly(Side.CLIENT)
public class RenderAmphithereArrow extends RenderBaseArrow<EntityAmphithereArrow> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/amphithere_arrow.png");

	public RenderAmphithereArrow(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAmphithereArrow entity) {
		return TEXTURE;
	}
}