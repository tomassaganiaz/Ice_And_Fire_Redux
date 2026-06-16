package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
/** AI de Myrmex para wander */


public class MyrmexAIWander extends EntityAIWanderAvoidWater {

    protected EntityMyrmexBase myrmex;

    public MyrmexAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
        this.myrmex = myrmex;
    }

    public boolean shouldExecute(){
        return myrmex.canMove() && myrmex.shouldWander() && super.shouldExecute();
    }
}
