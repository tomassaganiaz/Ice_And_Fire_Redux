package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/** ChainLightningAttack — Chain Lightning Attack */


public class ChainLightningAttack {

    private final ChainLightningConfig config;
    private final ChainLightningEffectHandler effectHandler;
    private final LightningTargetFinder targetFinder;

    public ChainLightningAttack() {
        this.config = new ChainLightningConfig();
        this.effectHandler = new ChainLightningEffectHandler(config);
        this.targetFinder = new LightningTargetFinder(config);
    }

    public ChainLightningAttack(ChainLightningConfig config, ChainLightningEffectHandler effectHandler, LightningTargetFinder targetFinder) {
        this.config = config;
        this.effectHandler = effectHandler;
        this.targetFinder = targetFinder;
    }

    public void execute(World world, EntityLivingBase target, Entity attacker) {
        execute(world, target, attacker, false);
    }

    public void executeFromPlayer(EntityLivingBase target, EntityPlayer player) {
        EntityAttackValidator validator = new EntityAttackValidator(config);
        if (!validator.canHurt(target, player)) return;

        float[] damage = new float[]{config.getDamagePerHop()[0]};
        effectHandler.attackWithDamage(player, target, 0);
        effectHandler.applyParalysis(target, 0);
        effectHandler.playStrikeSound(target);

        List<Integer> entities = new ArrayList<>();
        entities.add(player.getEntityId());
        entities.add(target.getEntityId());
        effectHandler.sendParticlePacket(entities, target);
    }

    private void execute(World world, EntityLivingBase target, Entity attacker, boolean singleHopOnly) {
        EntityAttackValidator validator = new EntityAttackValidator(config);
        if (!validator.canHurt(target, attacker)) return;

        if (attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;
            if (player.getCooldownTracker().hasCooldown(IafItemRegistry.dragonbone_sword_lightning)) return;
            player.getCooldownTracker().setCooldown(IafItemRegistry.dragonbone_sword_lightning, config.getCooldown());
        }

        int hop = 0;
        effectHandler.attackWithDamage(attacker, target, hop);
        effectHandler.applyParalysis(target, hop);
        effectHandler.playStrikeSound(target);

        if (singleHopOnly) return;

        List<EntityLivingBase> candidates = targetFinder.findTargets(world, target, attacker);
        if (candidates.isEmpty()) return;

        LinkedList<Integer> alreadyTargeted = new LinkedList<>();
        alreadyTargeted.add(target.getEntityId());

        for (EntityLivingBase nextTarget : candidates) {
            hop++;
            if (hop >= config.getDamagePerHop().length) break;
            if (alreadyTargeted.contains(nextTarget.getEntityId())) continue;

            effectHandler.attackWithDamage(attacker, nextTarget, hop);
            effectHandler.applyParalysis(nextTarget, hop);
            alreadyTargeted.add(nextTarget.getEntityId());
        }

        alreadyTargeted.addFirst(target.getEntityId());
        effectHandler.sendParticlePacket(alreadyTargeted, target);
    }
}
