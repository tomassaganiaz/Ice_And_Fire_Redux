package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.entity.projectile.EntitySeaSerpentArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Sea Serpent Arrow */

@SideOnly(Side.CLIENT)
public class RenderSeaSerpentArrow extends RenderBaseArrow<EntitySeaSerpentArrow> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/sea_serpent_arrow.png");

	public RenderSeaSerpentArrow(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySeaSerpentArrow entity) {
		return TEXTURE;
	}
}