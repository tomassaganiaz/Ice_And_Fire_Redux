package com.github.Redux.iceandfire.item;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.enums.EnumTroll;
import net.minecraft.item.Item;
/** Ítem Troll Leather */


public class ItemTrollLeather extends Item {

    public ItemTrollLeather(EnumTroll troll, String gameName, String name) {
        this.setRegistryName(IceAndFire.MODID, gameName);
        this.setTranslationKey(name);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
    }
}
