package com.github.Redux.iceandfire.integration;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/** SpartanWeaponryCompat — Spartan Weaponry Compat */


public class SpartanWeaponryCompat {

	public static final ResourceLocation RETURN_ENCHANTMENT = new ResourceLocation("spartanweaponry", "return");

	private static boolean initialized = false;

	public static boolean hasReturnEnchantment() {
		return getReturnEnchantment() != null;
	}

	@Nullable
	public static Enchantment getReturnEnchantment() {
		return Enchantment.REGISTRY.getObject(RETURN_ENCHANTMENT);
	}

	@Nullable
	public static SoundEvent getReturnSoundEvent() {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("spartanweaponry", "throwing_weapon_return"));
	}

	@Nullable
	public static SoundEvent getThrowingWeaponSoundEvent() {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("spartanweaponry", "throwing_weapon_sound"));
	}

	public static void init() {
		if (initialized) return;
		initialized = true;
		if (!CompatLoadUtil.isSpartanWeaponryLoaded()) return;

		try {
			Class<?> apiClass = Class.forName("com.oblivioussp.spartanweaponry.api.SpartanWeaponryAPI");
			Class<?> materialClass = Class.forName("com.oblivioussp.spartanweaponry.api.WeaponMaterial");

			registerDragonboneMaterials(apiClass, materialClass);
			registerDragonsteelMaterials(apiClass, materialClass, "fire",
					IafItemRegistry.fireDragonsteelTools, IafItemRegistry.dragonsteel_fire_ingot,
					new ResourceLocation("forge:ingots/fire_dragonsteel"));
			registerDragonsteelMaterials(apiClass, materialClass, "ice",
					IafItemRegistry.iceDragonsteelTools, IafItemRegistry.dragonsteel_ice_ingot,
					new ResourceLocation("forge:ingots/ice_dragonsteel"));
			registerDragonsteelMaterials(apiClass, materialClass, "lightning",
					IafItemRegistry.lightningDragonsteelTools, IafItemRegistry.dragonsteel_lightning_ingot,
					new ResourceLocation("forge:ingots/lightning_dragonsteel"));
		} catch (Exception e) {
			IceAndFire.logger.warn("Failed to register SpartanWeaponry materials: " + e.getMessage());
		}
	}

	private static void registerDragonboneMaterials(Class<?> apiClass, Class<?> materialClass) throws Exception {
		Constructor<?> materialCtor = materialClass.getConstructor(
				String.class, String.class, int.class, int.class,
				int.class, int.class, float.class, float.class, int.class,
				ResourceLocation.class);

		Object dragonboneMat = materialCtor.newInstance("dragonbone", IceAndFire.MODID,
				0xA67B5B, 0xD4A574,
				IafItemRegistry.boneTools.getHarvestLevel(),
				IafItemRegistry.boneTools.getMaxUses(),
				IafItemRegistry.boneTools.getEfficiency(),
				IafItemRegistry.boneTools.getAttackDamage(),
				IafItemRegistry.boneTools.getEnchantability(),
				new ResourceLocation("forge:bone_dragon"));

		registerAllWeapons(apiClass, dragonboneMat);
	}

	private static void registerDragonsteelMaterials(Class<?> apiClass, Class<?> materialClass,
			String name, Item.ToolMaterial toolMat, Item ingot, ResourceLocation tag) throws Exception {
		Constructor<?> materialCtor = materialClass.getConstructor(
				String.class, String.class, int.class, int.class,
				int.class, int.class, float.class, float.class, int.class,
				ResourceLocation.class);

		int colourPrimary = name.equals("fire") ? 0xE04040 : name.equals("ice") ? 0x40A0E0 : 0xC040E0;
		int colourSecondary = name.equals("fire") ? 0xFF8080 : name.equals("ice") ? 0x80D0FF : 0xE080FF;

		Object dragonsteelMat = materialCtor.newInstance(name + "_dragonsteel", IceAndFire.MODID,
				colourPrimary, colourSecondary,
				toolMat.getHarvestLevel(), toolMat.getMaxUses(),
				toolMat.getEfficiency(), toolMat.getAttackDamage(),
				toolMat.getEnchantability(), tag);

		registerAllWeapons(apiClass, dragonsteelMat);
	}

	private static void registerAllWeapons(Class<?> apiClass, Object material) throws Exception {
		Method createDagger = apiClass.getMethod("createDagger", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createLongsword = apiClass.getMethod("createLongsword", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createKatana = apiClass.getMethod("createKatana", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createSaber = apiClass.getMethod("createSaber", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createRapier = apiClass.getMethod("createRapier", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createGreatsword = apiClass.getMethod("createGreatsword", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createBattleHammer = apiClass.getMethod("createBattleHammer", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createWarhammer = apiClass.getMethod("createWarhammer", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createSpear = apiClass.getMethod("createSpear", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createHalberd = apiClass.getMethod("createHalberd", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createPike = apiClass.getMethod("createPike", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createLance = apiClass.getMethod("createLance", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createLongbow = apiClass.getMethod("createLongbow", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createThrowingKnife = apiClass.getMethod("createThrowingKnife", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createTomahawk = apiClass.getMethod("createTomahawk", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createJavelin = apiClass.getMethod("createJavelin", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createBoomerang = apiClass.getMethod("createBoomerang", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createBattleaxe = apiClass.getMethod("createBattleaxe", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createFlangedMace = apiClass.getMethod("createFlangedMace", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createGlaive = apiClass.getMethod("createGlaive", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);
		Method createQuarterstaff = apiClass.getMethod("createQuarterstaff", material.getClass(), net.minecraft.creativetab.CreativeTabs.class);

		createDagger.invoke(null, material, IceAndFire.TAB_ITEMS);
		createLongsword.invoke(null, material, IceAndFire.TAB_ITEMS);
		createKatana.invoke(null, material, IceAndFire.TAB_ITEMS);
		createSaber.invoke(null, material, IceAndFire.TAB_ITEMS);
		createRapier.invoke(null, material, IceAndFire.TAB_ITEMS);
		createGreatsword.invoke(null, material, IceAndFire.TAB_ITEMS);
		createBattleHammer.invoke(null, material, IceAndFire.TAB_ITEMS);
		createWarhammer.invoke(null, material, IceAndFire.TAB_ITEMS);
		createSpear.invoke(null, material, IceAndFire.TAB_ITEMS);
		createHalberd.invoke(null, material, IceAndFire.TAB_ITEMS);
		createPike.invoke(null, material, IceAndFire.TAB_ITEMS);
		createLance.invoke(null, material, IceAndFire.TAB_ITEMS);
		createLongbow.invoke(null, material, IceAndFire.TAB_ITEMS);
		createThrowingKnife.invoke(null, material, IceAndFire.TAB_ITEMS);
		createTomahawk.invoke(null, material, IceAndFire.TAB_ITEMS);
		createJavelin.invoke(null, material, IceAndFire.TAB_ITEMS);
		createBoomerang.invoke(null, material, IceAndFire.TAB_ITEMS);
		createBattleaxe.invoke(null, material, IceAndFire.TAB_ITEMS);
		createFlangedMace.invoke(null, material, IceAndFire.TAB_ITEMS);
		createGlaive.invoke(null, material, IceAndFire.TAB_ITEMS);
		createQuarterstaff.invoke(null, material, IceAndFire.TAB_ITEMS);
	}
}
