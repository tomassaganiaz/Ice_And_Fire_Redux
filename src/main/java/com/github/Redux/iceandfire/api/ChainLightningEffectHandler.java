package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.event.EventLiving;
import com.github.Redux.iceandfire.integration.LycanitesCompat;
import com.github.Redux.iceandfire.message.MessageChainLightningFX;
import com.github.Redux.iceandfire.misc.IafSoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;
/** ChainLightningEffectHandler — Chain Lightning Effect Handler */


public class ChainLightningEffectHandler {

    private final ChainLightningConfig config;

    public ChainLightningEffectHandler(ChainLightningConfig config) {
        this.config = config;
    }

    public void attackWithDamage(Entity attacker, EntityLivingBase target, int hop) {
        if (EventLiving.isQuarkCrab(target)) {
            strikeWithLightningBolt(target);
            return;
        }

        float[] damage = config.getDamagePerHop();
        DamageSource damageSource = new EntityDamageSourceIndirect("lightningBolt", attacker, attacker);
        if (config.bypassesArmor()) {
            damageSource = damageSource.setDamageBypassesArmor();
        }

        target.attackEntityFrom(damageSource, damage[hop]);

        if (target instanceof EntityCreeper && !((EntityCreeper) target).getPowered()) {
            EntityCreeper creeper = (EntityCreeper) target;
            NBTTagCompound compound = new NBTTagCompound();
            creeper.writeEntityToNBT(compound);
            compound.setBoolean("powered", true);
            creeper.readEntityFromNBT(compound);
        }
    }

    public void applyParalysis(EntityLivingBase target, int hop) {
        if (!config.isParalysisEnabled()) return;
        int[] paralysisTicks = config.getParalysisTicksPerHop();
        int ticks = paralysisTicks.length > hop ? paralysisTicks[hop] : 0;
        if (ticks > 0) {
            LycanitesCompat.applyParalysis(target, ticks);
        }
    }

    public void playStrikeSound(EntityLivingBase target) {
        target.playSound(IafSoundRegistry.LIGHTNING_STRIKE, 0.5F, 1);
    }

    public void sendParticlePacket(List<Integer> entities, EntityLivingBase origin) {
        if (!entities.isEmpty()) {
            IceAndFire.NETWORK_WRAPPER.sendToAllAround(
                    new MessageChainLightningFX(entities),
                    new NetworkRegistry.TargetPoint(
                            origin.dimension,
                            origin.posX,
                            origin.posY + origin.height / 2,
                            origin.posZ,
                            60
                    )
            );
        }
    }

    private void strikeWithLightningBolt(Entity entity) {
        EntityLightningBolt lightningBolt = new EntityLightningBolt(entity.world, entity.posX, entity.posY, entity.posZ, true);
        entity.onStruckByLightning(lightningBolt);
    }
}
