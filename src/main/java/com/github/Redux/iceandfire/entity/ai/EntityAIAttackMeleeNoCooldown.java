package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityCockatrice;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
/** Entidad AIAttack Melee No Cooldown */


public class EntityAIAttackMeleeNoCooldown extends EntityAIAttackMelee {
    public EntityAIAttackMeleeNoCooldown(EntityCreature creature, double speed, boolean memory) {
        super(creature, speed, memory);
    }

    public void updateTask() {
        super.updateTask();
        this.attackTick = 0;
    }
}
