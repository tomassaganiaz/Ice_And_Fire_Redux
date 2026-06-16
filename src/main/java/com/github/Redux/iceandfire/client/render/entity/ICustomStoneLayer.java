package com.github.Redux.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
/** ICustomStoneLayer — I Custom Stone Layer */


public interface ICustomStoneLayer {
    LayerRenderer<? extends EntityLivingBase> getStoneLayer(RenderLiving<?> render);
}