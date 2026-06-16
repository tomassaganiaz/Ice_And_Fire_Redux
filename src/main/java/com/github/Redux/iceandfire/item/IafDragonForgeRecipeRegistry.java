package com.github.Redux.iceandfire.item;

import com.github.Redux.iceandfire.enums.EnumBloodedDragonArmor;
import com.github.Redux.iceandfire.enums.EnumDragonArmor;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.github.Redux.iceandfire.recipe.DragonForgeRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/** IafDragonForgeRecipeRegistry — Iaf Dragon Forge Recipe Registry */


public class IafDragonForgeRecipeRegistry {

    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> LIGHTNING_FORGE_RECIPES = new ArrayList<>();

	public static void preInit() {
		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_sword), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_sword_fire), true));
		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_bow), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_bow_fire), true));
		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_sword), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_sword_ice), true));
		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_bow), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_bow_ice), true));
		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_sword), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_sword_lightning), true));
		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone_bow), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonbone_bow_lightning), true));

		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_fire_ingot)));
		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_fire_ingot, 3), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_fire_sword)));
		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_fire_ingot, 3), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_fire_pickaxe)));
		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_fire_ingot, 3), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_fire_axe)));
		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_fire_ingot, 2), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_fire_shovel)));
		FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_fire_ingot, 2), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_fire_hoe)));

		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_ice_ingot)));
		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_ice_ingot, 3), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_ice_sword)));
		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_ice_ingot, 3), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_ice_pickaxe)));
		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_ice_ingot, 3), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_ice_axe)));
		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_ice_ingot, 2), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_ice_shovel)));
		ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_ice_ingot, 2), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_ice_hoe)));

		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonbone), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_lightning_ingot)));
		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_lightning_ingot, 3), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_lightning_sword)));
		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_lightning_ingot, 3), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_lightning_pickaxe)));
		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_lightning_ingot, 3), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_lightning_axe)));
		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_lightning_ingot, 2), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_lightning_shovel)));
		LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(IafItemRegistry.dragonsteel_lightning_ingot, 2), new ItemStack(IafItemRegistry.lightning_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_lightning_hoe)));

        for (EnumDragonArmor input : EnumDragonArmor.values()) {
            List<DragonForgeRecipe> recipes;
            Item blood;
            switch (input.eggType.dragonType) {
                case ICE:
                    recipes = ICE_FORGE_RECIPES;
                    blood = IafItemRegistry.ice_dragon_blood;
                    break;
                case LIGHTNING:
                    recipes = LIGHTNING_FORGE_RECIPES;
                    blood = IafItemRegistry.lightning_dragon_blood;
                    break;
                default:
                    recipes = FIRE_FORGE_RECIPES;
                    blood = IafItemRegistry.fire_dragon_blood;
            }

            EnumBloodedDragonArmor result = EnumBloodedDragonArmor.valueOf(input.name());

            recipes.add(new DragonForgeRecipe(new ItemStack(input.helmet), new ItemStack(blood), new ItemStack(result.helmet), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.chestplate), new ItemStack(blood), new ItemStack(result.chestplate), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.leggings), new ItemStack(blood), new ItemStack(result.leggings), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.boots), new ItemStack(blood), new ItemStack(result.boots), true));
        }

        for (EnumBloodedDragonArmor input : EnumBloodedDragonArmor.values()) {
            List<DragonForgeRecipe> recipes;
            Item blood;
            Item outputHelmet, outputChestplate, outputLeggings, outputBoots;
            switch (input.eggType.dragonType) {
                case ICE:
                    recipes = ICE_FORGE_RECIPES;
                    blood = IafItemRegistry.ice_dragon_blood;
                    outputHelmet = IafItemRegistry.dragonsteel_ice_helmet;
                    outputChestplate = IafItemRegistry.dragonsteel_ice_chestplate;
                    outputLeggings = IafItemRegistry.dragonsteel_ice_leggings;
                    outputBoots = IafItemRegistry.dragonsteel_ice_boots;
                    break;
                case LIGHTNING:
                    recipes = LIGHTNING_FORGE_RECIPES;
                    blood = IafItemRegistry.lightning_dragon_blood;
                    outputHelmet = IafItemRegistry.dragonsteel_lightning_helmet;
                    outputChestplate = IafItemRegistry.dragonsteel_lightning_chestplate;
                    outputLeggings = IafItemRegistry.dragonsteel_lightning_leggings;
                    outputBoots = IafItemRegistry.dragonsteel_lightning_boots;
                    break;
                default:
                    recipes = FIRE_FORGE_RECIPES;
                    blood = IafItemRegistry.fire_dragon_blood;
                    outputHelmet = IafItemRegistry.dragonsteel_fire_helmet;
                    outputChestplate = IafItemRegistry.dragonsteel_fire_chestplate;
                    outputLeggings = IafItemRegistry.dragonsteel_fire_leggings;
                    outputBoots = IafItemRegistry.dragonsteel_fire_boots;
            }

            recipes.add(new DragonForgeRecipe(new ItemStack(input.helmet), new ItemStack(blood), new ItemStack(outputHelmet), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.chestplate), new ItemStack(blood), new ItemStack(outputChestplate), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.leggings), new ItemStack(blood), new ItemStack(outputLeggings), true));
            recipes.add(new DragonForgeRecipe(new ItemStack(input.boots), new ItemStack(blood), new ItemStack(outputBoots), true));
        }
    }

    @Nullable
    public static DragonForgeRecipe getForgeRecipe(EnumDragonType type, ItemStack stack) {
        for (DragonForgeRecipe recipe : getForgeRecipes(type)) {
            if (!stack.isEmpty() && stack.isItemEqualIgnoreDurability(recipe.getInput())) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static DragonForgeRecipe getForgeRecipe(ItemStack stack) {
        for (EnumDragonType type : EnumDragonType.values()) {
            DragonForgeRecipe recipe = getForgeRecipe(type, stack);
            if (recipe != null) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static DragonForgeRecipe getForgeRecipeForBlood(EnumDragonType type, ItemStack stack) {
        for (DragonForgeRecipe recipe : getForgeRecipes(type)) {
            if (stack != null && stack.isItemEqual(recipe.getBlood())) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean isValidBlood(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() == IafItemRegistry.fire_dragon_blood
                || stack.getItem() == IafItemRegistry.ice_dragon_blood
                || stack.getItem() == IafItemRegistry.lightning_dragon_blood) {
            return true;
        }
        for (EnumDragonType type : EnumDragonType.values()) {
            for (DragonForgeRecipe recipe : getForgeRecipes(type)) {
                if (stack.isItemEqual(recipe.getBlood())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<DragonForgeRecipe> getForgeRecipes(EnumDragonType type) {
        if (type == null) {
            return new ArrayList<>();
        }
        switch (type) {
            case ICE: return ICE_FORGE_RECIPES;
            case LIGHTNING: return LIGHTNING_FORGE_RECIPES;
            default: return FIRE_FORGE_RECIPES;
        }
    }
}
