package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityMyrmexBase;
import com.github.Redux.iceandfire.entity.EntityMyrmexQueen;
import net.minecraft.entity.ai.EntityAIBase;
/** MyrmexQueenAIWander — Myrmex Queen AI Wander */


public class MyrmexQueenAIWander extends MyrmexAIWander {

    public MyrmexQueenAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
    }

    public boolean shouldExecute(){
        return (myrmex.canSeeSky() || myrmex.getHive() == null) && super.shouldExecute();
    }
}
