package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.capability.entityeffect.EntityEffectProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
/** InFCapabilities — In F Capabilities */


public class InFCapabilities {

    public static IEntityEffectCapability getEntityEffectCapability(EntityLivingBase entity) {
        return entity.getCapability(EntityEffectProvider.ENTITY_EFFECT, null);
    }
}