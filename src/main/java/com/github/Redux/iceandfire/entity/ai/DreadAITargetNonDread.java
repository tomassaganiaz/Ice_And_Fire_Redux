package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.entity.util.IDreadMob;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import javax.annotation.Nullable;
/** DreadAITargetNonDread — Dread AI Target Non Dread */


public class DreadAITargetNonDread extends EntityAINearestAttackableTarget<EntityLivingBase> {
    public DreadAITargetNonDread(EntityCreature entityIn, Class<EntityLivingBase> classTarget, Predicate<? super EntityLivingBase> targetSelector) {
        super(entityIn, classTarget, 0, IceAndFireConfig.ENTITY_SETTINGS.dreadTargetingCheckSight, false, targetSelector);
    }

    protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles) {
        if (super.isSuitableTarget(target, includeInvincibles)) {
            return !IDreadMob.isOnSameTeam(target) && DragonUtils.isAlive(target);
        }
        return false;
    }
}
