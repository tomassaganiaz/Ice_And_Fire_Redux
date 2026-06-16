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

/** Comportamiento específico del dragón de relámpago: huevos, ítems, sonidos, loot */
public class LightningDragonBehavior extends DragonBehavior {

    private static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/lightning_dragon_female"));
    private static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/lightning_dragon_male"));
    private static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/lightning_dragon_skeleton"));

    private static final EnumDragonEgg[] EGG_TYPES = {EnumDragonEgg.ELECTRIC, EnumDragonEgg.AMETHYST, EnumDragonEgg.COPPER, EnumDragonEgg.BLACK};

    public LightningDragonBehavior() {
        super(IafItemRegistry.summoning_crystal_lightning,
              IafItemRegistry.lightning_dragon_blood,
              IafItemRegistry.lightning_dragon_heart,
              IafItemRegistry.lightning_dragon_flesh,
              IafItemRegistry.dragon_skull,
              new ItemStack(IafItemRegistry.dragon_horn_lightning),
              IafItemRegistry.lightning_stew,
              8,
              FEMALE_LOOT, MALE_LOOT, SKELETON_LOOT);
    }

    @Override
    public EnumDragonEgg[] getEggTypes() { return EGG_TYPES; }

    @Override
    public SoundEvent getAmbientSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.LIGHTNINGDRAGON_CHILD_IDLE, IafSoundRegistry.LIGHTNINGDRAGON_TEEN_IDLE, IafSoundRegistry.LIGHTNINGDRAGON_ADULT_IDLE);
    }

    @Override
    public SoundEvent getHurtSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.LIGHTNINGDRAGON_CHILD_HURT, IafSoundRegistry.LIGHTNINGDRAGON_TEEN_HURT, IafSoundRegistry.LIGHTNINGDRAGON_ADULT_HURT);
    }

    @Override
    public SoundEvent getDeathSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.LIGHTNINGDRAGON_CHILD_DEATH, IafSoundRegistry.LIGHTNINGDRAGON_TEEN_DEATH, IafSoundRegistry.LIGHTNINGDRAGON_ADULT_DEATH);
    }

    @Override
    public SoundEvent getRoarSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.LIGHTNINGDRAGON_CHILD_ROAR, IafSoundRegistry.LIGHTNINGDRAGON_TEEN_ROAR, IafSoundRegistry.LIGHTNINGDRAGON_ADULT_ROAR);
    }

    @Override
    public SoundEvent getBreathSound() { return IafSoundRegistry.LIGHTNINGDRAGON_BREATH; }

    @Override
    public SoundEvent getShortBreathSound() { return IafSoundRegistry.LIGHTNINGDRAGON_BREATH_SHORT; }

    @Override
    public boolean canDamageEntity(DamageSource source) {
        return !IceAndFire.dragonLightning.damageType.contentEquals(source.damageType);
    }

    @Override
    public String getVariantName(int variant) {
        switch (variant) {
            default: return "electric_";
            case 1: return "amethyst_";
            case 2: return "copper_";
            case 3: return "black_";
        }
    }

    @Override
    public Item getVariantScale(int variant) {
        switch (variant) {
            default: return EnumDragonEgg.ELECTRIC.scales;
            case 1: return EnumDragonEgg.AMETHYST.scales;
            case 2: return EnumDragonEgg.COPPER.scales;
            case 3: return EnumDragonEgg.BLACK.scales;
        }
    }

    @Override
    public Item getVariantEgg(int variant) {
        switch (variant) {
            default: return EnumDragonEgg.ELECTRIC.egg;
            case 1: return EnumDragonEgg.AMETHYST.egg;
            case 2: return EnumDragonEgg.COPPER.egg;
            case 3: return EnumDragonEgg.BLACK.egg;
        }
    }

    @Override
    public Item getSkullItem() { return IafItemRegistry.dragon_skull; }

    public ItemStack getSkull() { return new ItemStack(IafItemRegistry.dragon_skull, 1, 2); }

    public ItemStack getHorn() { return new ItemStack(IafItemRegistry.dragon_horn_lightning); }
}
