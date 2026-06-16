package com.github.Redux.iceandfire.entity;

import net.minecraft.entity.Entity;
/** Marca entidades que temen a animales/monstruos */


public interface IAnimalFear {
    boolean shouldAnimalsFear(Entity entity);
}
