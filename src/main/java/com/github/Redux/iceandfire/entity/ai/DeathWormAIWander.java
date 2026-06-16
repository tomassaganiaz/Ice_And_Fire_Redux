package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityDeathWorm;
import net.minecraft.entity.ai.EntityAIWander;
/** DeathWormAIWander — Death Worm AI Wander */


public class DeathWormAIWander extends EntityAIWander {

    private EntityDeathWorm worm;
    public DeathWormAIWander(EntityDeathWorm creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.worm = creatureIn;
    }

    public boolean shouldExecute(){
        return !worm.isInSand() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting(){
        return !worm.isInSand() && super.shouldContinueExecuting();
    }
}