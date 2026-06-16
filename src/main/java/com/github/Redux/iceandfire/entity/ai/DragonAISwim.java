package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
/** AI de dragón para swim */


public class DragonAISwim extends EntityAISwimming {

    EntityDragonBase dragon;

    public DragonAISwim(EntityDragonBase dragon) {
        super(dragon);
        this.dragon = dragon;
    }

    public boolean shouldExecute()
    {
        if (!dragon.isChild() && dragon.canMove()) {
            return false;
        }
        return super.shouldExecute();
    }
}
