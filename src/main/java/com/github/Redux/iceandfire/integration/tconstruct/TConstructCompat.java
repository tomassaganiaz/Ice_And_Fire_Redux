package com.github.Redux.iceandfire.integration.tconstruct;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.item.IafItemRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/** TConstructCompat — T Construct Compat */


public class TConstructCompat {

	private static boolean initialized = false;

	public static void init() {
		if (initialized) return;
		initialized = true;
		if (!CompatLoadUtil.isTConstructLoaded()) return;

		try {
			Class<?> tinkerRegistry = Class.forName("slimeknights.tconstruct.library.TinkerRegistry");
			Class<?> materialClass = Class.forName("slimeknights.tconstruct.library.materials.Material");
			Class<?> headStatsClass = Class.forName("slimeknights.tconstruct.library.materials.HeadMaterialStats");
			Class<?> handleStatsClass = Class.forName("slimeknights.tconstruct.library.materials.HandleMaterialStats");
			Class<?> extraStatsClass = Class.forName("slimeknights.tconstruct.library.materials.ExtraMaterialStats");

			registerDragonbone(tinkerRegistry, materialClass, headStatsClass, handleStatsClass, extraStatsClass);
			registerDragonsteel(tinkerRegistry, materialClass, headStatsClass, handleStatsClass, extraStatsClass,
					"fire_dragonsteel", 0xE04040, 2000, 12.0F, 7.0F, 5, 1.2F, 150);
			registerDragonsteel(tinkerRegistry, materialClass, headStatsClass, handleStatsClass, extraStatsClass,
					"ice_dragonsteel", 0x40A0E0, 2000, 12.0F, 7.0F, 5, 1.2F, 150);
			registerDragonsteel(tinkerRegistry, materialClass, headStatsClass, handleStatsClass, extraStatsClass,
					"lightning_dragonsteel", 0xC040E0, 2000, 12.0F, 7.0F, 5, 1.2F, 150);
		} catch (Exception e) {
			IceAndFire.logger.warn("Failed to register Tinkers' Construct materials: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}

		if (CompatLoadUtil.isConstructsArmoryLoaded()) {
			initArmorMaterials();
		}

		TinkerModifierBridge.init();
	}

	private static void registerDragonbone(Class<?> tinkerRegistry, Class<?> materialClass,
			Class<?> headStatsClass, Class<?> handleStatsClass, Class<?> extraStatsClass) throws Exception {
		Constructor<?> matCtor = materialClass.getConstructor(String.class, int.class);
		Object dragonboneMat = matCtor.newInstance("dragonbone", 0xA67B5B);

		Method setHidden = materialClass.getMethod("setHidden", boolean.class);
		setHidden.invoke(dragonboneMat, false);
		Method setCastable = materialClass.getMethod("setCastable", boolean.class);
		setCastable.invoke(dragonboneMat, false);
		Method setShard = materialClass.getMethod("setShard", boolean.class);
		setShard.invoke(dragonboneMat, true);

		Object headStats = headStatsClass.getConstructor(float.class, float.class, float.class, int.class)
				.newInstance(500, 7.0F, 4.0F, 3);
		Object handleStats = handleStatsClass.getConstructor(float.class, int.class)
				.newInstance(1.0F, 100);
		Object extraStats = extraStatsClass.getConstructor(int.class)
				.newInstance(50);

		Method addMaterialStats = tinkerRegistry.getMethod("addMaterialStats", materialClass, Object[].class);
		addMaterialStats.invoke(null, dragonboneMat, new Object[]{headStats, handleStats, extraStats});

		Method addMaterial = tinkerRegistry.getMethod("addMaterial", materialClass);
		addMaterial.invoke(null, dragonboneMat);

		IceAndFire.logger.info("Registered Tinkers' Construct material: dragonbone");
	}

	private static void registerDragonsteel(Class<?> tinkerRegistry, Class<?> materialClass,
			Class<?> headStatsClass, Class<?> handleStatsClass, Class<?> extraStatsClass,
			String name, int color, int durability, float efficiency, float damage,
			int harvestLevel, float handleMod, int extraDura) throws Exception {
		Constructor<?> matCtor = materialClass.getConstructor(String.class, int.class);
		Object mat = matCtor.newInstance(name, color);

		Method setHidden = materialClass.getMethod("setHidden", boolean.class);
		setHidden.invoke(mat, false);
		Method setCastable = materialClass.getMethod("setCastable", boolean.class);
		setCastable.invoke(mat, true);
		Method setShard = materialClass.getMethod("setShard", boolean.class);
		setShard.invoke(mat, true);
		Method setFluid = materialClass.getMethod("setFluid", String.class);
		setFluid.invoke(mat, name);

		Object headStats = headStatsClass.getConstructor(float.class, float.class, float.class, int.class)
				.newInstance((float) durability, (float) efficiency, (float) damage, harvestLevel);
		Object handleStats = handleStatsClass.getConstructor(float.class, int.class)
				.newInstance((float) handleMod, (int) (durability * 0.1F));
		Object extraStats = extraStatsClass.getConstructor(int.class)
				.newInstance(extraDura);

		Method addMaterialStats = tinkerRegistry.getMethod("addMaterialStats", materialClass, Object[].class);
		addMaterialStats.invoke(null, mat, new Object[]{headStats, handleStats, extraStats});

		Method addMaterial = tinkerRegistry.getMethod("addMaterial", materialClass);
		addMaterial.invoke(null, mat);

		IceAndFire.logger.info("Registered Tinkers' Construct material: " + name);
	}

	private static void initArmorMaterials() {
		try {
			Class<?> tinkerRegistry = Class.forName("slimeknights.tconstruct.library.TinkerRegistry");
			Class<?> materialClass = Class.forName("slimeknights.tconstruct.library.materials.Material");
			Class<?> armoryRegistryClass = Class.forName("c4.conarm.lib.ArmoryRegistry");
			Class<?> coreStatsClass = Class.forName("c4.conarm.lib.materials.CoreMaterialStats");
			Class<?> platesStatsClass = Class.forName("c4.conarm.lib.materials.PlatesMaterialStats");
			Class<?> trimStatsClass = Class.forName("c4.conarm.lib.materials.TrimMaterialStats");

			IceAndFire.logger.info("Construct Armory detected — registering armor materials");

			registerDragonboneArmor(tinkerRegistry, materialClass, armoryRegistryClass,
					coreStatsClass, platesStatsClass, trimStatsClass);
			registerDragonsteelArmor(tinkerRegistry, materialClass, armoryRegistryClass,
					coreStatsClass, platesStatsClass, trimStatsClass,
					"fire_dragonsteel", 35.0F, 22.0F, 3.0F, 2.5F, 8.0F);
			registerDragonsteelArmor(tinkerRegistry, materialClass, armoryRegistryClass,
					coreStatsClass, platesStatsClass, trimStatsClass,
					"ice_dragonsteel", 35.0F, 22.0F, 3.0F, 2.5F, 8.0F);
			registerDragonsteelArmor(tinkerRegistry, materialClass, armoryRegistryClass,
					coreStatsClass, platesStatsClass, trimStatsClass,
					"lightning_dragonsteel", 35.0F, 22.0F, 3.0F, 2.5F, 8.0F);

		} catch (Exception e) {
			IceAndFire.logger.warn("Failed to init Construct Armory materials: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	private static Object newCoreStats(Class<?> clazz, float durability, float defense) throws Exception {
		try { return clazz.getConstructor(float.class, float.class).newInstance(durability, defense); }
		catch (NoSuchMethodException e1) {
			try { return clazz.getConstructor(float.class).newInstance(defense); }
			catch (NoSuchMethodException e2) {
				throw new NoSuchMethodException("CoreMaterialStats: no constructor (float,float) or (float)");
			}
		}
	}

	private static Object newPlatesStats(Class<?> clazz, float modifier, float toughness) throws Exception {
		try { return clazz.getConstructor(float.class, float.class).newInstance(modifier, toughness); }
		catch (NoSuchMethodException e1) {
			try { return clazz.getConstructor(float.class).newInstance(toughness); }
			catch (NoSuchMethodException e2) {
				throw new NoSuchMethodException("PlatesMaterialStats: no constructor (float,float) or (float)");
			}
		}
	}

	private static Object newTrimStats(Class<?> clazz, float extraDurability) throws Exception {
		try { return clazz.getConstructor(float.class).newInstance(extraDurability); }
		catch (NoSuchMethodException e1) {
			try { return clazz.getConstructor().newInstance(); }
			catch (NoSuchMethodException e2) {
				throw new NoSuchMethodException("TrimMaterialStats: no constructor (float) or ()");
			}
		}
	}

	private static Method findAddArmorStats(Class<?> armoryRegistryClass, Class<?> materialClass,
			Class<?> coreStatsClass, Class<?> platesStatsClass, Class<?> trimStatsClass) throws Exception {
		try {
			return armoryRegistryClass.getMethod("addArmorMaterialStats",
					materialClass, coreStatsClass, platesStatsClass, trimStatsClass);
		} catch (NoSuchMethodException e1) {
			try {
				return armoryRegistryClass.getMethod("addArmorMaterialStats",
						materialClass, coreStatsClass);
			} catch (NoSuchMethodException e2) {
				throw new NoSuchMethodException("ArmoryRegistry: no method addArmorMaterialStats(Material,Core,Plates,Trim) or (Material,Core)");
			}
		}
	}

	private static void registerDragonboneArmor(Class<?> tinkerRegistry, Class<?> materialClass,
			Class<?> armoryRegistryClass, Class<?> coreStatsClass,
			Class<?> platesStatsClass, Class<?> trimStatsClass) {

		try {
			Method getMaterial = tinkerRegistry.getMethod("getMaterial", String.class);
			Object dragonboneMat = getMaterial.invoke(null, "dragonbone");
			if (dragonboneMat == null) {
				IceAndFire.logger.warn("ConArm: dragonbone material not yet registered, skipping armor stats");
				return;
			}

			Object coreStats = newCoreStats(coreStatsClass, 20.0F, 16.0F);
			Object platesStats = newPlatesStats(platesStatsClass, 1.5F, 0.5F);
			Object trimStats = newTrimStats(trimStatsClass, 5.0F);

			Method addArmorStats = findAddArmorStats(armoryRegistryClass, materialClass,
					coreStatsClass, platesStatsClass, trimStatsClass);
			if (addArmorStats.getParameterCount() == 2) {
				addArmorStats.invoke(null, dragonboneMat, coreStats);
			} else {
				addArmorStats.invoke(null, dragonboneMat, coreStats, platesStats, trimStats);
			}

			IceAndFire.logger.info("ConArm: registered dragonbone (core=16.0, plates=0.5, trim=5.0)");
		} catch (Exception e) {
			IceAndFire.logger.warn("ConArm dragonbone failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	private static void registerDragonsteelArmor(Class<?> tinkerRegistry, Class<?> materialClass,
			Class<?> armoryRegistryClass, Class<?> coreStatsClass,
			Class<?> platesStatsClass, Class<?> trimStatsClass,
			String name, float durability, float defense, float modifier,
			float toughness, float extraDurability) {

		try {
			Method getMaterial = tinkerRegistry.getMethod("getMaterial", String.class);
			Object mat = getMaterial.invoke(null, name);
			if (mat == null) {
				IceAndFire.logger.warn("ConArm: " + name + " material not yet registered, skipping armor stats");
				return;
			}

			Object coreStats = newCoreStats(coreStatsClass, durability, defense);
			Object platesStats = newPlatesStats(platesStatsClass, modifier, toughness);
			Object trimStats = newTrimStats(trimStatsClass, extraDurability);

			Method addArmorStats = findAddArmorStats(armoryRegistryClass, materialClass,
					coreStatsClass, platesStatsClass, trimStatsClass);
			if (addArmorStats.getParameterCount() == 2) {
				addArmorStats.invoke(null, mat, coreStats);
			} else {
				addArmorStats.invoke(null, mat, coreStats, platesStats, trimStats);
			}

			IceAndFire.logger.info("ConArm: registered " + name + " (core def=" + defense + ")");
		} catch (Exception e) {
			IceAndFire.logger.warn("ConArm " + name + " failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
}
