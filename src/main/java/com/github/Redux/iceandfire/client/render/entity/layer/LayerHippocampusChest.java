package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.render.entity.RenderHippocampus;
import com.github.Redux.iceandfire.entity.EntityHippocampus;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** LayerHippocampusChest — Layer Hippocampus Chest */

@SideOnly(Side.CLIENT)
public class LayerHippocampusChest implements LayerRenderer<EntityHippocampus> {

	private final RenderHippocampus renderer;
	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/hippocampus/chest.png");

	public LayerHippocampusChest(RenderHippocampus renderer) {
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if (entity.isChested()) {
			this.renderer.bindTexture(TEXTURE);
			this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
