package com.github.Redux.iceandfire.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
/** FoodUtils — Food Utils */


public class FoodUtils {

    private static final FoodProperties FOOD = new FoodProperties();

    public static int getFoodPoints(Entity entity) {
        return FOOD.getFoodPoints(entity);
    }

    public static int getFoodPoints(ItemStack item, boolean meatOnly, boolean includeFish) {
        return FOOD.getFoodPoints(item, meatOnly, includeFish);
    }

    public static boolean isSeeds(ItemStack stack) {
        return FOOD.isSeeds(stack);
    }
}
