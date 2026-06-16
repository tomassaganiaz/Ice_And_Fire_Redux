package com.github.Redux.iceandfire.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/** FoodProperties — Food Properties */


public class FoodProperties {

    private static final Set<String> SEED_ORE_NAMES = new HashSet<>(Arrays.asList(
            "listAllseed", "listAllSeeds", "seed", "seeds"
    ));

    public int getFoodPoints(Entity entity) {
        int foodPoints = Math.round(entity.width * entity.height * 10);
        if (entity instanceof EntityAgeable) {
            return foodPoints;
        }
        if (entity instanceof EntityPlayer) {
            return 15;
        }
        return 0;
    }

    public int getFoodPoints(ItemStack item, boolean meatOnly, boolean includeFish) {
        if (item != null && item.getItem() instanceof ItemFood) {
            int food = ((ItemFood) item.getItem()).getHealAmount(item) * 10;
            if (!meatOnly) {
                return food;
            } else if (((ItemFood) item.getItem()).isWolfsFavoriteMeat()) {
                return food;
            } else if (includeFish && item.getItem() == Items.FISH) {
                return food;
            }
        }
        return 0;
    }

    public boolean isSeeds(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemSeeds && item != Items.NETHER_WART) {
            return true;
        }
        for (String oreName : SEED_ORE_NAMES) {
            for (ItemStack oreStack : OreDictionary.getOres(oreName)) {
                if (OreDictionary.itemMatches(oreStack, stack, false)) {
                    return true;
                }
            }
        }
        return false;
    }
}
