package com.github.Redux.iceandfire.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
/** IEffectTickable — I Effect Tickable */


public interface IEffectTickable {
    void tickUpdate(EntityLivingBase entity, World world);
    void tickTime();
    void tickData();
    boolean isDirty();
    void markClean();
}
