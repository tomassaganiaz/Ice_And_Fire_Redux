package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.entity.projectile.EntityStymphalianFeather;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Stymphalian Feather */

@SideOnly(Side.CLIENT)
public class RenderStymphalianFeather extends RenderBaseArrow<EntityStymphalianFeather> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/stymphalianbird/feather.png");

	public RenderStymphalianFeather(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityStymphalianFeather entity) {
		return TEXTURE;
	}
}