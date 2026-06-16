package com.github.Redux.iceandfire.event;

import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
/** EventLiving — Event Living */


public class EventLiving {

	public static final Map<BlockPos, EnumDragonType> BLOOD_CAULDRONS = DragonBloodHandler.BLOOD_CAULDRONS;
	public static final String BLOOD_BATHE_TAG = DragonBloodHandler.BLOOD_BATHE_TAG;
	public static final String BLOOD_BATHE_USES_TAG = DragonBloodHandler.BLOOD_BATHE_USES_TAG;
	public static final int MAX_BLOOD_BATHE_USES = DragonBloodHandler.MAX_BLOOD_BATHE_USES;

	public static float updateRotation(float angle, float targetAngle, float maxIncrease) {
		return DragonRidingHandler.updateRotation(angle, targetAngle, maxIncrease);
	}

	public static boolean isAnimaniaSheep(Entity entity) {
		return DragonEntityHandler.isAnimaniaSheep(entity);
	}

	public static boolean isAnimaniaChicken(Entity entity) {
		return DragonEntityHandler.isAnimaniaChicken(entity);
	}

	public static boolean isAnimaniaFerret(Entity entity) {
		return DragonEntityHandler.isAnimaniaFerret(entity);
	}

	public static boolean isQuarkCrab(Entity entity) {
		return DragonEntityHandler.isQuarkCrab(entity);
	}

	public static void onSwingGhostSword(final EntityPlayer playerEntity, final ItemStack stack) {
		DragonBlockHandler.onSwingGhostSword(playerEntity, stack);
	}
}
