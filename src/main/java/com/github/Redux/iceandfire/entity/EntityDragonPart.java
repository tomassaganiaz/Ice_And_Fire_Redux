package com.github.Redux.iceandfire.entity;

import com.github.Redux.iceandfire.entity.util.EntityMultipartPart;
import net.minecraft.util.DamageSource;
/** Parte multipart de dragón (cuerpo, cola, ala) */


public class EntityDragonPart extends EntityMultipartPart {

    private final EntityDragonBase dragon;

    public EntityDragonPart(EntityDragonBase dragon, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(dragon, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
        this.dragon = dragon;
        this.isImmuneToFire = dragon.isFireImmune();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if(source.getTrueSource() != null && source.getTrueSource() == dragon) return false;
        return super.attackEntityFrom(source, damage);
    }

    @Override
    public void collideWithNearbyEntities() {
    }
	
    @Override
    public boolean shouldNotExist(){
        return !this.dragon.isEntityAlive() && !this.dragon.isModelDead();
    }
}
