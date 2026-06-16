package com.github.Redux.iceandfire.core;

import com.github.Redux.iceandfire.potion.BasePotion;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
/** ModPotions — Mod Potions */


public class ModPotions {
    public static Potion acid = new BasePotion("acid", 9227300, true);

    public static void register() {
        ForgeRegistries.POTIONS.register(acid);
    }
}
