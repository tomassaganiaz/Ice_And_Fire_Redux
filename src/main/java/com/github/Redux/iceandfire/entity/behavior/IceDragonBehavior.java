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

/** Comportamiento específico del dragón de hielo: huevos, ítems, sonidos, loot */
public class IceDragonBehavior extends DragonBehavior {

    private static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_female"));
    private static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_male"));
    private static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_skeleton"));

    private static final EnumDragonEgg[] EGG_TYPES = {EnumDragonEgg.BLUE, EnumDragonEgg.WHITE, EnumDragonEgg.SAPPHIRE, EnumDragonEgg.SILVER};

    public IceDragonBehavior() {
        super(IafItemRegistry.summoning_crystal_ice,
              IafItemRegistry.ice_dragon_blood,
              IafItemRegistry.ice_dragon_heart,
              IafItemRegistry.ice_dragon_flesh,
              IafItemRegistry.dragon_skull,
              new ItemStack(IafItemRegistry.dragon_horn_ice),
              IafItemRegistry.frost_stew,
              4,
              FEMALE_LOOT, MALE_LOOT, SKELETON_LOOT);
    }

    @Override
    public EnumDragonEgg[] getEggTypes() { return EGG_TYPES; }

    @Override
    public SoundEvent getAmbientSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.ICEDRAGON_CHILD_IDLE, IafSoundRegistry.ICEDRAGON_TEEN_IDLE, IafSoundRegistry.ICEDRAGON_ADULT_IDLE);
    }

    @Override
    public SoundEvent getHurtSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.ICEDRAGON_CHILD_HURT, IafSoundRegistry.ICEDRAGON_TEEN_HURT, IafSoundRegistry.ICEDRAGON_ADULT_HURT);
    }

    @Override
    public SoundEvent getDeathSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.ICEDRAGON_CHILD_DEATH, IafSoundRegistry.ICEDRAGON_TEEN_DEATH, IafSoundRegistry.ICEDRAGON_ADULT_DEATH);
    }

    @Override
    public SoundEvent getRoarSound(EntityDragonBase dragon) {
        return selectSound(dragon, IafSoundRegistry.ICEDRAGON_CHILD_ROAR, IafSoundRegistry.ICEDRAGON_TEEN_ROAR, IafSoundRegistry.ICEDRAGON_ADULT_ROAR);
    }

    @Override
    public SoundEvent getBreathSound() { return IafSoundRegistry.ICEDRAGON_BREATH; }

    @Override
    public SoundEvent getShortBreathSound() { return IafSoundRegistry.ICEDRAGON_BREATH_SHORT; }

    @Override
    public boolean canDamageEntity(DamageSource source) {
        String type = source.damageType;
        return !IceAndFire.dragonIce.damageType.contentEquals(type)
            && !"ooze".contentEquals(type)
            && !"cold_fire".contentEquals(type);
    }

    @Override
    public String getVariantName(int variant) {
        switch (variant) {
            default: return "blue_";
            case 1: return "white_";
            case 2: return "sapphire_";
            case 3: return "silver_";
        }
    }

    @Override
    public Item getVariantScale(int variant) {
        switch (variant) {
            default: return EnumDragonEgg.BLUE.scales;
            case 1: return EnumDragonEgg.WHITE.scales;
            case 2: return EnumDragonEgg.SAPPHIRE.scales;
            case 3: return EnumDragonEgg.SILVER.scales;
        }
    }

    @Override
    public Item getVariantEgg(int variant) {
        switch (variant) {
            default: return EnumDragonEgg.BLUE.egg;
            case 1: return EnumDragonEgg.WHITE.egg;
            case 2: return EnumDragonEgg.SAPPHIRE.egg;
            case 3: return EnumDragonEgg.SILVER.egg;
        }
    }

    @Override
    public Item getSkullItem() { return IafItemRegistry.dragon_skull; }

    public ItemStack getSkull() { return new ItemStack(IafItemRegistry.dragon_skull, 1, 1); }

    public ItemStack getHorn() { return new ItemStack(IafItemRegistry.dragon_horn_ice); }
}
