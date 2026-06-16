package com.github.Redux.iceandfire.integration.tconstruct;

import com.github.Redux.iceandfire.IceAndFire;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Puente entre DragonBloodHandler y Tinker's Construct.
 * Cuando una herramienta de Tinker recibe sangre de dragón (NBT tag DragonBloodBathe),
 * este handler sincroniza el estado con el sistema de modifiers de Tinker.
 *
 * El daño elemental lo aplica DragonBloodHandler.onLivingHurt() — esta clase solo
 * se encarga de la parte visual (tooltip de modifier) y registro en TinkerRegistry.
 */
public class TinkerModifierBridge {

	private static boolean initialized = false;
	private static boolean conArmInitialized = false;
	private static Object flameModifier = null;
	private static Object frostModifier = null;
	private static Object lightningModifier = null;

	public static void init() {
		if (initialized) return;
		initialized = true;
		if (!com.github.Redux.iceandfire.integration.CompatLoadUtil.isTConstructLoaded()) return;

		try {
			Class<?> abstractModifier = Class.forName("slimeknights.tconstruct.library.modifiers.Modifier");
			Class<?> tinkerRegistry = Class.forName("slimeknights.tconstruct.library.TinkerRegistry");
			Class<?> modifierNBT = Class.forName("slimeknights.tconstruct.library.modifiers.ModifierNBT");
			Class<?> toolHelper = Class.forName("slimeknights.tconstruct.library.utils.ToolHelper");

			flameModifier = registerModifier(abstractModifier, tinkerRegistry, modifierNBT,
				"flame", "Dragon Blood (Fire)", "DragonBlood:flame",
				0xE04040, "Sets enemies on fire for 10s");
			frostModifier = registerModifier(abstractModifier, tinkerRegistry, modifierNBT,
				"frost", "Dragon Blood (Ice)", "DragonBlood:frost",
				0x40A0E0, "Freezes enemies for 10s");
			lightningModifier = registerModifier(abstractModifier, tinkerRegistry, modifierNBT,
				"lightning", "Dragon Blood (Lightning)", "DragonBlood:lightning",
				0xC040E0, "Chance to strike chain lightning");

			IceAndFire.logger.info("Registered Tinker modifier bridge for dragon blood modifiers");

			initConArmArmorTraits();
		} catch (Exception e) {
			IceAndFire.logger.warn("Failed to register Tinker modifier bridge: " + e.getMessage());
		}
	}

	private static void initConArmArmorTraits() {
		if (conArmInitialized) return;
		if (!com.github.Redux.iceandfire.integration.CompatLoadUtil.isConstructsArmoryLoaded()) return;

		try {
			Class<?> abstractModifier = Class.forName("slimeknights.tconstruct.library.modifiers.Modifier");
			Class<?> tinkerRegistry = Class.forName("slimeknights.tconstruct.library.TinkerRegistry");
			Class<?> modifierNBT = Class.forName("slimeknights.tconstruct.library.modifiers.ModifierNBT");

			registerModifier(abstractModifier, tinkerRegistry, modifierNBT,
				"dragonblood_fire_resist", "Dragon Blood (Fire Armor)", "DragonBlood:fireArmor",
				0xFF6600, "Grants Fire Resistance while worn");
			registerModifier(abstractModifier, tinkerRegistry, modifierNBT,
				"dragonblood_ice_resist", "Dragon Blood (Ice Armor)", "DragonBlood:iceArmor",
				0x66CCFF, "Clears Slowness effects while worn");
			registerModifier(abstractModifier, tinkerRegistry, modifierNBT,
				"dragonblood_lightning_resist", "Dragon Blood (Lightning Armor)", "DragonBlood:lightningArmor",
				0xCC66FF, "Reduces lightning damage while worn");

			conArmInitialized = true;
			IceAndFire.logger.info("Registered Construct Armory armor traits for dragon blood");
		} catch (Exception e) {
			IceAndFire.logger.warn("Failed to register ConArm armor traits: " + e.getMessage());
		}
	}

	private static Object registerModifier(Class<?> abstractModifier, Class<?> tinkerRegistry,
			Class<?> modifierNBT, String id, String name, String locName, int color, String desc) throws Exception {

		java.lang.reflect.Constructor<?> ctor = abstractModifier.getDeclaredConstructor(
			String.class, String.class, int.class, int.class);
		ctor.setAccessible(true);
		Object modifier = ctor.newInstance(locName, desc, color, 1);

		java.lang.reflect.Method registerModifier = tinkerRegistry.getMethod("registerModifier", abstractModifier);
		registerModifier.invoke(null, modifier);

		IceAndFire.logger.info("Registered Tinker modifier: " + id);
		return modifier;
	}

	/**
	 * Aplica o remueve el modifier de Tinker y el trait de ConArm según la sangre aplicada.
	 * Llamado por DragonBloodHandler al aplicar/remover sangre de dragón.
	 */
	public static void syncTinkerModifier(ItemStack stack, com.github.Redux.iceandfire.enums.EnumDragonType type, boolean apply) {
		if (!initialized || flameModifier == null) return;

		try {
			Class<?> toolHelper = Class.forName("slimeknights.tconstruct.library.utils.ToolHelper");
			Class<?> tagUtil = Class.forName("slimeknights.tconstruct.library.utils.TagUtil");

			String toolModId;
			String armorModId;
			switch (type) {
				case FIRE:
					toolModId = "DragonBlood:flame";
					armorModId = "DragonBlood:fireArmor";
					break;
				case ICE:
					toolModId = "DragonBlood:frost";
					armorModId = "DragonBlood:iceArmor";
					break;
				default:
					toolModId = "DragonBlood:lightning";
					armorModId = "DragonBlood:lightningArmor";
					break;
			}

			if (apply) {
				removeIncompatible(stack, type, toolModId);
				addModifierTag(stack, toolModId);
				addModifierTag(stack, armorModId);
			} else {
				removeModifierTag(stack, toolModId);
				removeModifierTag(stack, armorModId);
			}

		} catch (Exception e) {
			// Herramienta/armadura no-Tinker o API no disponible — ignorar silenciosamente
		}
	}

	private static void removeIncompatible(ItemStack stack, com.github.Redux.iceandfire.enums.EnumDragonType type, String currentId) throws Exception {
		if (type == com.github.Redux.iceandfire.enums.EnumDragonType.FIRE) {
			removeModifierTag(stack, "DragonBlood:frost");
			removeModifierTag(stack, "DragonBlood:iceArmor");
		} else if (type == com.github.Redux.iceandfire.enums.EnumDragonType.ICE) {
			removeModifierTag(stack, "DragonBlood:flame");
			removeModifierTag(stack, "DragonBlood:fireArmor");
		}
	}

	private static void addModifierTag(ItemStack stack, String modifierName) throws Exception {
		Class<?> tagUtil = Class.forName("slimeknights.tconstruct.library.utils.TagUtil");
		Class<?> modifierNBT = Class.forName("slimeknights.tconstruct.library.modifiers.ModifierNBT");

		NBTTagCompound tag = (NBTTagCompound) tagUtil.getMethod("getTagSafe", ItemStack.class).invoke(null, stack);
		NBTTagCompound toolTag = tag.getCompoundTag("TinkerData");
		if (!tag.hasKey("TinkerData")) {
			tag.setTag("TinkerData", toolTag);
		}

		NBTTagList modifiers = toolTag.getTagList("Modifiers", Constants.NBT.TAG_COMPOUND);
		boolean exists = false;
		for (int i = 0; i < modifiers.tagCount(); i++) {
			if (modifierName.equals(modifiers.getCompoundTagAt(i).getString("identifier"))) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			NBTTagCompound modTag = new NBTTagCompound();
			modTag.setString("identifier", modifierName);
			modifiers.appendTag(modTag);
			toolTag.setTag("Modifiers", modifiers);
		}
	}

	private static void removeModifierTag(ItemStack stack, String modifierName) throws Exception {
		Class<?> tagUtil = Class.forName("slimeknights.tconstruct.library.utils.TagUtil");
		NBTTagCompound tag = (NBTTagCompound) tagUtil.getMethod("getTagSafe", ItemStack.class).invoke(null, stack);
		if (!tag.hasKey("TinkerData")) return;

		NBTTagCompound toolTag = tag.getCompoundTag("TinkerData");
		if (!toolTag.hasKey("Modifiers")) return;

		NBTTagList modifiers = toolTag.getTagList("Modifiers", Constants.NBT.TAG_COMPOUND);
		for (int i = modifiers.tagCount() - 1; i >= 0; i--) {
			if (modifierName.equals(modifiers.getCompoundTagAt(i).getString("identifier"))) {
				modifiers.removeTag(i);
			}
		}
	}
}
