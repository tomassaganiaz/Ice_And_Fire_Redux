package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.entity.util.EntityMultipartPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/** LightningTargetFinder — Lightning Target Finder */


public class LightningTargetFinder {

    private final ChainLightningConfig config;

    public LightningTargetFinder(ChainLightningConfig config) {
        this.config = config;
    }

    public List<EntityLivingBase> findTargets(World world, EntityLivingBase source, Entity attacker) {
        LightningSource lightningSource = new LightningSource(source);
        EntityAttackValidator validator = new EntityAttackValidator(config);
        int range = config.getRange();

        List<EntityLivingBase> candidates = new ArrayList<>();
        for (Entity ent : world.getEntitiesWithinAABBExcludingEntity(lightningSource.get(), lightningSource.getBoundingBox(range))) {
            if (ent instanceof EntityMultipartPart) {
                ent = ((EntityMultipartPart) ent).getParent();
            }
            if (ent instanceof EntityLivingBase
                    && !candidates.contains(ent)
                    && lightningSource.canChainTo((EntityLivingBase) ent, attacker, validator)) {
                candidates.add((EntityLivingBase) ent);
            }
        }

        candidates.sort(Comparator.comparingDouble(e -> e.getDistanceSq(lightningSource.get())));
        return candidates;
    }
}
