package com.github.Redux.iceandfire.entity.util;

import com.github.Redux.iceandfire.IceAndFireConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
/** IDreadMob — I Dread Mob */


public interface IDreadMob {
    Entity getCommander();

    static boolean isOnSameTeam(Entity entityIn) {
        if (entityIn instanceof IDreadMob) {
            return true;
        }
        ResourceLocation id = EntityList.getKey(entityIn);
        return id != null && IceAndFireConfig.getDreadTargetingEntityBlacklist().contains(id);
    }
}
