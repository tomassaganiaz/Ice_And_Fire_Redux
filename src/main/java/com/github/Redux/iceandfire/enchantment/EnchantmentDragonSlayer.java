package com.github.Redux.iceandfire.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

/** Asesino de Dragones — daño extra contra dragones (I=15%, II=25%, III=35%) */
public class EnchantmentDragonSlayer extends Enchantment {

	public EnchantmentDragonSlayer() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
		this.setName("iceandfire.dragon_slayer");
		this.setRegistryName("iceandfire", "dragon_slayer");
	}

	@Override
	public int getMinEnchantability(int level) {
		return 30;
	}

	@Override
	public int getMaxEnchantability(int level) {
		return 60;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
}
