package com.github.Redux.iceandfire.enums;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.item.ItemMobSkull;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
/** EnumSkullType — Enum Skull Type */


public enum EnumSkullType {
    HIPPOGRYPH,
    CYCLOPS,
    COCKATRICE,
    STYMPHALIAN,
    TROLL,
    AMPHITHERE,
    SEASERPENT,
    HYDRA;

    public String itemResourceName;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":mob_skull")
    public Item skull_item;

    EnumSkullType() {
        itemResourceName = this.name().toLowerCase() + "_skull";
    }

    public static void initItems() {
        for (EnumSkullType skull : EnumSkullType.values()) {
            skull.skull_item = new ItemMobSkull(skull);

        }
    }
}
