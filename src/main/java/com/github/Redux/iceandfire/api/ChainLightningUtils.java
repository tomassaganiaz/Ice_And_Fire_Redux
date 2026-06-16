package com.github.Redux.iceandfire.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
/** ChainLightningUtils — Chain Lightning Utils */


public class ChainLightningUtils {

    private static final ChainLightningAttack ATTACK = new ChainLightningAttack();

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, Entity attacker) {
        ATTACK.execute(world, target, attacker);
    }

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, EntityLivingBase attacker) {
        ATTACK.execute(world, target, attacker);
    }

    public static void createChainLightningToTargetFromPlayer(EntityLivingBase target, EntityPlayer player) {
        ATTACK.executeFromPlayer(target, player);
    }
}
