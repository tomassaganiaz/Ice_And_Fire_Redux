package com.github.Redux.iceandfire.entity.behavior;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonEgg;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import com.github.Redux.iceandfire.misc.IafSoundRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootTableList;

/** Comportamiento específico del dragón de fuego: huevos, ítems, sonidos, loot */
public class FireDragonBehavior extends DragonBehavior {

    private static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_female"));
    private static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_male"));
    private static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_skeleton"));

    private static final EnumDragonEgg[] EGG_TYPES = {EnumDragonEgg.RED, EnumDragonEgg.GREEN, EnumDragonEgg.BRONZE, EnumDragonEgg.GRAY};

    public FireDragonBehavior() {
        super(IafItemRegistry.summoning_crystal_fire,
              IafItemRegistry.fire_dragon_blood,
              IafItemRegistry.fire_dragon_heart,
              IafItemRegistry.fire_dragon_flesh,
              IafItemRegistry.dragon_skull,
              new ItemStack(IafItemRegistry.dragon_horn_fire),
              IafItemRegistry.fire_stew,
              0,
              FEMALE_LOOT, MALE_LOOT, SKELETON_LOOT);
    }

    @Override
    public EnumDragonEgg[] getEggTypes() { return EGG_TYPES; }

    @Override
    public SoundEvent getAmbientSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.FIREDRAGON_CHILD_IDLE, IafSoundRegistry.FIREDRAGON_TEEN_IDLE, IafSoundRegistry.FIREDRAGON_ADULT_IDLE);
    }

    @Override
    public SoundEvent getHurtSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.FIREDRAGON_CHILD_HURT, IafSoundRegistry.FIREDRAGON_TEEN_HURT, IafSoundRegistry.FIREDRAGON_ADULT_HURT);
    }

    @Override
    public SoundEvent getDeathSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.FIREDRAGON_CHILD_DEATH, IafSoundRegistry.FIREDRAGON_TEEN_DEATH, IafSoundRegistry.FIREDRAGON_ADULT_DEATH);
    }

    @Override
    public SoundEvent getRoarSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.FIREDRAGON_CHILD_ROAR, IafSoundRegistry.FIREDRAGON_TEEN_ROAR, IafSoundRegistry.FIREDRAGON_ADULT_ROAR);
    }

    @Override
    public SoundEvent getBreathSound() { return IafSoundRegistry.FIREDRAGON_BREATH; }

    @Override
    public SoundEvent getShortBreathSound() { return IafSoundRegistry.FIREDRAGON_BREATH_SHORT; }

    @Override
    public boolean canDamageEntity(DamageSource source) {
        return !IceAndFire.dragonFire.damageType.contentEquals(source.damageType);
    }

    @Override
    public String getVariantName(int variant) {
        switch (variant) {
            default: return "red_";
            case 1: return "green_";
            case 2: return "bronze_";
            case 3: return "gray_";
        }
    }

    @Override
    public Item getVariantScale(int variant) {
        switch (variant) {
            default: return EnumDragonEgg.RED.scales;
            case 1: return EnumDragonEgg.GREEN.scales;
            case 2: return EnumDragonEgg.BRONZE.scales;
            case 3: return EnumDragonEgg.GRAY.scales;
        }
    }

    @Override
    public Item getVariantEgg(int variant) {
        switch (variant) {
            default: return EnumDragonEgg.RED.egg;
            case 1: return EnumDragonEgg.GREEN.egg;
            case 2: return EnumDragonEgg.BRONZE.egg;
            case 3: return EnumDragonEgg.GRAY.egg;
        }
    }

    @Override
    public Item getSkullItem() { return IafItemRegistry.dragon_skull; }

    public ItemStack getSkull() { return new ItemStack(IafItemRegistry.dragon_skull, 1, 0); }

    public ItemStack getHorn() { return new ItemStack(IafItemRegistry.dragon_horn_fire); }
}
