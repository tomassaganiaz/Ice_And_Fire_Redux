package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.entity.util.IDeadMob;
import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.integration.LycanitesCompat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
/** Entidad Attack Validator */


public class EntityAttackValidator {

    private final ChainLightningConfig config;

    public EntityAttackValidator(ChainLightningConfig config) {
        this.config = config;
    }

    public boolean canHurt(EntityLivingBase target, Entity attacker) {
        if (target instanceof IDeadMob && ((IDeadMob) target).isMobDead()) {
            return false;
        }
        if (!target.attackable()) {
            return false;
        }
        if (target instanceof EntityLiving && ((EntityLiving)target).isAIDisabled()) {
            return false;
        }
        if (CompatLoadUtil.isLycanitesMobsLoaded()) {
            if (!LycanitesCompat.canHurt(target, attacker)) {
                return false;
            }
        }
        return target instanceof EntityLiving || target instanceof EntityPlayer;
    }

    public boolean isBlacklistedFromChaining(Entity entity) {
        ResourceLocation id = EntityList.getKey(entity);
        return id != null && config.getEntityBlacklist().contains(id);
    }
}
