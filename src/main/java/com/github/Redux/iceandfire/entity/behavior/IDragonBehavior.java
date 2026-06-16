package com.github.Redux.iceandfire.entity.behavior;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/** Interfaz que define los datos específicos por tipo de dragón (huevos, ítems, sonidos, loot, comportamiento) */
public interface IDragonBehavior {

    EnumDragonEgg[] getEggTypes();

    Item getSummoningCrystal();

    Item getBlood();

    Item getHeart();

    Item getFlesh();

    Item getSkullItem();

    ItemStack getHorn();

    Item getStew();

    int getBaseEggTypeValue();

    SoundEvent getAmbientSound(EntityDragonBase dragon);

    SoundEvent getHurtSound(EntityDragonBase dragon);

    SoundEvent getDeathSound(EntityDragonBase dragon);

    SoundEvent getRoarSound(EntityDragonBase dragon);

    SoundEvent getBreathSound();

    SoundEvent getShortBreathSound();

    ResourceLocation getFemaleLoot();

    ResourceLocation getMaleLoot();

    ResourceLocation getSkeletonLoot();

    boolean canDamageEntity(DamageSource source);

    String getVariantName(int variant);

    Item getVariantScale(int variant);

    Item getVariantEgg(int variant);

    float[][] getGrowthStages();
}
