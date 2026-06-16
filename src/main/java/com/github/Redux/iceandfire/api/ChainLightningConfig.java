package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.IceAndFireConfig;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
/** ChainLightningConfig — Chain Lightning Config */


public class ChainLightningConfig {

    public float[] getDamagePerHop() {
        return IceAndFireConfig.MISC_SETTINGS.chainLightningDamagePerHop;
    }

    public int getRange() {
        return IceAndFireConfig.MISC_SETTINGS.chainLightningRange;
    }

    public boolean isParalysisEnabled() {
        return IceAndFireConfig.MISC_SETTINGS.chainLightningParalysis;
    }

    public int[] getParalysisTicksPerHop() {
        return IceAndFireConfig.MISC_SETTINGS.chainLightningParalysisTicksPerHop;
    }

    public int getCooldown() {
        return IceAndFireConfig.MISC_SETTINGS.chainLightningCooldown;
    }

    public boolean bypassesArmor() {
        return IceAndFireConfig.MISC_SETTINGS.chainLightningBypassesArmor;
    }

    public HashSet<ResourceLocation> getEntityBlacklist() {
        return IceAndFireConfig.getChainLightningEntityBlacklist();
    }
}
