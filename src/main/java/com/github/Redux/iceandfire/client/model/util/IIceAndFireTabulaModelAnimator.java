package com.github.Redux.iceandfire.client.model.util;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author gegy1000
 * @since 1.0.0
 */
/** IIceAndFireTabulaModelAnimator — I Ice And Fire Tabula Model Animator */

@SideOnly(Side.CLIENT)
public interface IIceAndFireTabulaModelAnimator<T extends Entity> {
    void init(IceAndFireTabulaModel model);
    void setRotationAngles(IceAndFireTabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}