package com.github.Redux.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityTameable;
/** HippocampusAIWander — Hippocampus AI Wander */


public class HippocampusAIWander extends EntityAIWander {

    public HippocampusAIWander(EntityCreature creatureIn, double speedIn) {
        super(creatureIn, speedIn);
    }

    public boolean shouldExecute(){
        return !(entity instanceof EntityTameable && ((EntityTameable) entity).isSitting()) && !this.entity.isInWater() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting(){
        return !(entity instanceof EntityTameable && ((EntityTameable) entity).isSitting()) && !this.entity.isInWater() && super.shouldContinueExecuting();
    }
}