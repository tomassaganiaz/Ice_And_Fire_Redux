package com.github.Redux.iceandfire.integration.crafttweaker;

import net.minecraftforge.fml.common.Loader;
/** CraftTweakerCompatBridge — Craft Tweaker Compat Bridge */


public class CraftTweakerCompatBridge {
    private static final String COMPAT_MOD_ID = "crafttweaker";

    public static void loadCraftTweakerCompat() {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            CraftTweakerCompat.preInit();
        }
    }
}
