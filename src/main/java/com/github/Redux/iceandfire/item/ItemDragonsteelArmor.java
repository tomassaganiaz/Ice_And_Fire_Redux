package com.github.Redux.iceandfire.item;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.client.StatCollector;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
/** Ítem Dragonsteel Armor */


public class ItemDragonsteelArmor extends ItemArmor {

	public EnumDragonType dragonType;

	public ItemDragonsteelArmor(EnumDragonType dragonType, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String name) {
		super(material, renderIndex, slot);
		this.dragonType = dragonType;
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey("iceandfire." + name);
		this.setRegistryName(IceAndFire.MODID, name);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (dragonType == EnumDragonType.FIRE) {
			return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 23 : 22);
		} else if (dragonType == EnumDragonType.ICE) {
			return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 25 : 24);
		}
		return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 27 : 26);
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		String base = dragonType == EnumDragonType.FIRE ? "armor_dragonsteel_fire" :
				dragonType == EnumDragonType.ICE ? "armor_dragonsteel_ice" : "armor_dragonsteel_lightning";
		return "iceandfire:textures/models/armor/" + base + (renderIndex == 2 ? "_legs.png" : ".png");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		String color = dragonType == EnumDragonType.FIRE ? "\u00A7c" : dragonType == EnumDragonType.ICE ? "\u00A7b" : "\u00A7d";
		String typeName = dragonType == EnumDragonType.FIRE ? "dragon.fire" : dragonType == EnumDragonType.ICE ? "dragon.ice" : "dragon.lightning";
		tooltip.add(color + StatCollector.translateToLocal(typeName));
	}

	public void applyEffect(EntityPlayer player, EntityLivingBase attacker) {
		if (isCooldownActive(player)) {
			return;
		}
		if (dragonType == EnumDragonType.FIRE) {
			if (attacker instanceof EntityDragonBase && ((EntityDragonBase)attacker).isWeakToFire()) {
				attacker.attackEntityFrom(DamageSource.IN_FIRE, 18.0F);
			}
			attacker.setFire(10);
			attacker.knockBack(attacker, 1.5F, player.posX - attacker.posX, player.posZ - attacker.posZ);
		} else if (dragonType == EnumDragonType.ICE) {
			if (attacker instanceof EntityDragonBase && ((EntityDragonBase)attacker).isWeakToIce()) {
				attacker.attackEntityFrom(DamageSource.DROWN, 18.0F);
			}
			attacker.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 3));
			attacker.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 3));
			attacker.knockBack(attacker, 1.5F, player.posX - attacker.posX, player.posZ - attacker.posZ);
		} else if (dragonType == EnumDragonType.LIGHTNING) {
			if (attacker instanceof EntityDragonBase && ((EntityDragonBase)attacker).isWeakToLightning()) {
				attacker.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 8.0F);
			}
			attacker.knockBack(attacker, 2.0F, player.posX - attacker.posX, player.posZ - attacker.posZ);
		}
	}

	public static void applySetEffect(EntityPlayer player, EntityLivingBase attacker) {
		ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		if (helmet.isEmpty() || !(helmet.getItem() instanceof ItemDragonsteelArmor)) {
			return;
		}
		ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (chestplate.isEmpty() || !(chestplate.getItem() instanceof ItemDragonsteelArmor)) {
			return;
		}
		ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (leggings.isEmpty() || !(leggings.getItem() instanceof ItemDragonsteelArmor)) {
			return;
		}
		ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		if (boots.isEmpty() || !(boots.getItem() instanceof ItemDragonsteelArmor)) {
			return;
		}
		switch (player.world.rand.nextInt(4)) {
			case 0:
				((ItemDragonsteelArmor) helmet.getItem()).applyEffect(player, attacker);
				break;
			case 1:
				((ItemDragonsteelArmor) chestplate.getItem()).applyEffect(player, attacker);
				break;
			case 2:
				((ItemDragonsteelArmor) leggings.getItem()).applyEffect(player, attacker);
				break;
			default:
				((ItemDragonsteelArmor) boots.getItem()).applyEffect(player, attacker);
		}
	}

	private static boolean isCooldownActive(EntityPlayer player) {
		Item item = IafItemRegistry.dragonsteel_fire_chestplate;
		if (player.getCooldownTracker().hasCooldown(item)) {
			return true;
		}
		player.getCooldownTracker().setCooldown(item, IceAndFireConfig.MISC_SETTINGS.bloodedDragonArmorSetEffectCooldown);
		return false;
	}
}
