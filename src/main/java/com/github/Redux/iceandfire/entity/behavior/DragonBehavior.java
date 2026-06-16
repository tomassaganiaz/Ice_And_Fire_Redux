package com.github.Redux.iceandfire.entity.behavior;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/** Comportamiento base para los 3 tipos de dragón — define huevos, ítems, sonidos, loot según EnumDragonType */
public abstract class DragonBehavior implements IDragonBehavior {

    protected static final float[][] SHARED_GROWTH_STAGES = {
        {1F, 3F},
        {3F, 7F},
        {7F, 12.5F},
        {12.5F, 20F},
        {20F, 30F}
    };

    protected final Item summoningCrystal;
    protected final Item blood;
    protected final Item heart;
    protected final Item flesh;
    protected final Item skullItem;
    protected final ItemStack horn;
    protected final Item stew;
    protected final int baseEggTypeValue;
    protected final ResourceLocation femaleLoot;
    protected final ResourceLocation maleLoot;
    protected final ResourceLocation skeletonLoot;

    protected DragonBehavior(Item summoningCrystal, Item blood, Item heart, Item flesh,
                             Item skullItem, ItemStack horn, Item stew, int baseEggTypeValue,
                             ResourceLocation femaleLoot, ResourceLocation maleLoot,
                             ResourceLocation skeletonLoot) {
        this.summoningCrystal = summoningCrystal;
        this.blood = blood;
        this.heart = heart;
        this.flesh = flesh;
        this.skullItem = skullItem;
        this.horn = horn;
        this.stew = stew;
        this.baseEggTypeValue = baseEggTypeValue;
        this.femaleLoot = femaleLoot;
        this.maleLoot = maleLoot;
        this.skeletonLoot = skeletonLoot;
    }

    @Override public Item getSummoningCrystal() { return summoningCrystal; }
    @Override public Item getBlood() { return blood; }
    @Override public Item getHeart() { return heart; }
    @Override public Item getFlesh() { return flesh; }
    @Override public Item getSkullItem() { return skullItem; }
    @Override public ItemStack getHorn() { return horn; }
    @Override public Item getStew() { return stew; }
    @Override public int getBaseEggTypeValue() { return baseEggTypeValue; }
    @Override public ResourceLocation getFemaleLoot() { return femaleLoot; }
    @Override public ResourceLocation getMaleLoot() { return maleLoot; }
    @Override public ResourceLocation getSkeletonLoot() { return skeletonLoot; }
    @Override public float[][] getGrowthStages() { return SHARED_GROWTH_STAGES; }
    @Override public boolean canDamageEntity(DamageSource source) { return true; }

    protected static SoundEvent selectSound(EntityDragonBase dragon, SoundEvent child, SoundEvent teen, SoundEvent adult) {
        if (dragon.isTeen()) return teen;
        if (dragon.isAdult()) return adult;
        return child;
    }
}
