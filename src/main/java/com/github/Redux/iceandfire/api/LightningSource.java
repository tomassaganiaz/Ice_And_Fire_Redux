package com.github.Redux.iceandfire.api;

import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
/** LightningSource — Lightning Source */


public class LightningSource {

    private EntityLivingBase source;

    public LightningSource(EntityLivingBase source) {
        this.source = source;
    }

    public void set(EntityLivingBase source) {
        this.source = source;
    }

    public EntityLivingBase get() {
        return source;
    }

    public boolean canChainTo(EntityLivingBase target, Entity attacker, EntityAttackValidator validator) {
        if (target instanceof EntityPlayer) {
            return false;
        }
        if (!validator.canHurt(target, attacker)) {
            return false;
        }
        if (validator.isBlacklistedFromChaining(target)) {
            return false;
        }
        if (target instanceof IEntityOwnable && ((IEntityOwnable) target).getOwner() instanceof EntityPlayer) {
            if (attacker == null) {
                return false;
            }
            if (target instanceof EntityLiving) {
                EntityLivingBase attackTarget = ((EntityLiving) target).getAttackTarget();
                EntityLivingBase revengeTarget = target.getRevengeTarget();
                if (!attacker.equals(attackTarget) && !attacker.equals(revengeTarget)) {
                    return false;
                }
            } else {
                EntityLivingBase revengeTarget = target.getRevengeTarget();
                if (!attacker.equals(revengeTarget)) {
                    return false;
                }
            }
        }
        return source.canEntityBeSeen(target);
    }

    public AxisAlignedBB getBoundingBox(int range) {
        return new AxisAlignedBB(
                source.posX - range,
                source.posY - range,
                source.posZ - range,
                source.posX + range,
                source.posY + range,
                source.posZ + range
        );
    }
}
