package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.entity.projectile.EntityHydraArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Hydra Arrow */

@SideOnly(Side.CLIENT)
public class RenderHydraArrow extends RenderBaseArrow<EntityHydraArrow> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/hydra_arrow.png");

	public RenderHydraArrow(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityHydraArrow entity) {
		return TEXTURE;
	}
}