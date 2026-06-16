package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityCockatrice;
import net.minecraft.entity.ai.EntityAIFollowOwner;
/** CockatriceAIFollowOwner — Cockatrice AI Follow Owner */


public class CockatriceAIFollowOwner extends EntityAIFollowOwner {
    EntityCockatrice cockatrice;
    public CockatriceAIFollowOwner(EntityCockatrice cockatrice, double speed, float minDist, float maxDist) {
        super(cockatrice, speed, minDist, maxDist);
        this.cockatrice = cockatrice;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && cockatrice.getCommand() == 2;
    }
}
