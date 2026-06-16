package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.entity.projectile.EntityStymphalianArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Stymphalian Arrow */

@SideOnly(Side.CLIENT)
public class RenderStymphalianArrow extends RenderBaseArrow<EntityStymphalianArrow> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/stymphalian_arrow.png");

	public RenderStymphalianArrow(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityStymphalianArrow entity) {
		return TEXTURE;
	}
}