package com.github.Redux.iceandfire.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/** Sopa de dragón con efectos de poción */
public class ItemDragonStew extends ItemFood {

	private final PotionEffect[] effects;

	public ItemDragonStew(int amount, float saturation, String name, PotionEffect... effects) {
		super(amount, saturation, false);
		this.effects = effects;
		this.setAlwaysEdible();
		this.setTranslationKey("iceandfire." + name);
		this.setRegistryName("iceandfire", name);
		this.setCreativeTab(com.github.Redux.iceandfire.IceAndFire.TAB_ITEMS);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		if (!worldIn.isRemote && effects != null) {
			for (PotionEffect effect : effects) {
				player.addPotionEffect(new PotionEffect(effect));
			}
		}
	}
}
