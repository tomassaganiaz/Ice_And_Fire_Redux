package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
/** AI de ataque cuerpo a cuerpo para dragones */


public class DragonAIAttackMelee extends EntityAIAttackMelee {

    private final EntityDragonBase dragon;

    public DragonAIAttackMelee(EntityDragonBase dragon, double speedIn, boolean useLongMemory) {
        super(dragon, speedIn, useLongMemory);
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() {
        return this.dragon.canMove() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.dragon.canMove() && super.shouldExecute();
    }
}
