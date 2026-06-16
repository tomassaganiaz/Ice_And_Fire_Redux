package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.model.ModelStymphalianBird;
import com.github.Redux.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderizador de Stymphalian Bird */

@SideOnly(Side.CLIENT)
public class RenderStymphalianBird extends RenderLiving<EntityStymphalianBird> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(RenderManager renderManager) {
        super(renderManager, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public void preRenderCallback(EntityStymphalianBird entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(0.75F, 0.75F, 0.75F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityStymphalianBird cyclops) {
        return TEXTURE;
    }
}