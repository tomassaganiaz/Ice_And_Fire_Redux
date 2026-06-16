package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
/** AI de Myrmex para look at trade player */


public class MyrmexAILookAtTradePlayer extends EntityAIWatchClosest {
    private final EntityMyrmexBase myrmex;

    public MyrmexAILookAtTradePlayer(EntityMyrmexBase myrmex) {
        super(myrmex, EntityPlayer.class, 8.0F);
        this.myrmex = myrmex;
    }

    public boolean shouldExecute() {
        if (this.myrmex.isTrading()) {
            this.closestEntity = this.myrmex.getCustomer();
            return true;
        } else {
            return false;
        }
    }
}
