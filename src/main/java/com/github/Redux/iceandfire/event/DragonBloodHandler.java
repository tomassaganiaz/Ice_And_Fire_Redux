package com.github.Redux.iceandfire.event;

import com.github.Redux.iceandfire.api.ChainLightningUtils;
import com.github.Redux.iceandfire.client.StatCollector;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.github.Redux.iceandfire.integration.tconstruct.TinkerModifierBridge;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
/** DragonBloodHandler — Dragon Blood Handler */


public class DragonBloodHandler {

	public static final Map<BlockPos, EnumDragonType> BLOOD_CAULDRONS = new HashMap<>();
	public static final String BLOOD_BATHE_TAG = "DragonBloodBathe";
	public static final String BLOOD_BATHE_USES_TAG = "DragonBloodBatheUses";
	public static final int MAX_BLOOD_BATHE_USES = 100;

	@SubscribeEvent
	public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		Block block = state.getBlock();
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = event.getItemStack();

		if (block instanceof BlockCauldron && !stack.isEmpty()) {
			int level = state.getValue(BlockCauldron.LEVEL);
			EnumDragonType bloodType = getDragonBloodType(stack);
			if (bloodType != null && level == 0) {
				if (!world.isRemote) {
					BLOOD_CAULDRONS.put(pos, bloodType);
					if (!player.capabilities.isCreativeMode) stack.shrink(1);
				}
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				event.setCanceled(true);
			} else if (BLOOD_CAULDRONS.containsKey(pos) && isWeaponOrTool(stack)) {
				EnumDragonType cauldronBlood = BLOOD_CAULDRONS.get(pos);
				if (cauldronBlood != null && !hasBloodBathe(stack)) {
					applyBloodBathe(stack, cauldronBlood);
					if (!world.isRemote) {
						BLOOD_CAULDRONS.remove(pos);
						world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
					}
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					if (world.isRemote) {
						for (int i = 0; i < 20; i++) {
							world.spawnParticle(EnumParticleTypes.SPELL_MOB, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D,
								world.rand.nextGaussian() * 0.1D, world.rand.nextGaussian() * 0.1D, world.rand.nextGaussian() * 0.1D);
						}
					}
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event) {
		Block block = event.getState().getBlock();
		if (block instanceof BlockCauldron) {
			BLOOD_CAULDRONS.remove(event.getPos());
		}
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving().world.isRemote) return;
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
		if (event.getEntityLiving().ticksExisted % 20 != 0) return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		boolean hasFireBlood = false;
		boolean hasIceBlood = false;
		boolean hasLightningBlood = false;

		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) continue;
			ItemStack armor = player.getItemStackFromSlot(slot);
			if (hasBloodBathe(armor)) {
				String bloodType = armor.getTagCompound().getString(BLOOD_BATHE_TAG);
				if ("fire".equals(bloodType)) hasFireBlood = true;
				else if ("ice".equals(bloodType)) hasIceBlood = true;
				else if ("lightning".equals(bloodType)) hasLightningBlood = true;
			}
		}

		if (hasFireBlood) {
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 40, 0, false, false));
		}
		if (hasIceBlood) {
			if (player.isPotionActive(MobEffects.SLOWNESS)) {
				player.removePotionEffect(MobEffects.SLOWNESS);
			}
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		DamageSource source = event.getSource();
		EntityLivingBase victim = event.getEntityLiving();

		if (event.getAmount() <= 0.0f) return;

		// Defensa de armadura con sangre de dragón
		if (victim instanceof EntityPlayer) {
			boolean hasLightningArmor = false;
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
				if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) continue;
				ItemStack armor = victim.getItemStackFromSlot(slot);
				if (hasBloodBathe(armor) && "lightning".equals(armor.getTagCompound().getString(BLOOD_BATHE_TAG))) {
					hasLightningArmor = true;
					break;
				}
			}
			if (hasLightningArmor && ("lightningBolt".equals(source.getDamageType())
					|| source == DamageSource.LIGHTNING_BOLT)) {
				event.setAmount(event.getAmount() * 0.25F);
			}
		}

		if (source.isProjectile()
				|| source.isFireDamage()
				|| source.isExplosion()
				|| source.isMagicDamage()) {
			return;
		}

		// Ataque con arma bañada en sangre de dragón
		if (source.getImmediateSource() == source.getTrueSource() && source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
			ItemStack stack = attacker.getHeldItemMainhand();
			if (hasBloodBathe(stack)) {
				NBTTagCompound tag = stack.getTagCompound();
				String bloodType = tag.getString(BLOOD_BATHE_TAG);
				boolean isFire = "fire".equals(bloodType);
				boolean isIce = "ice".equals(bloodType);
				boolean isLightning = "lightning".equals(bloodType);
				EnumDragonType dragonType = victim instanceof EntityDragonBase ? ((EntityDragonBase)victim).dragonType : null;
				boolean isFireDragon = dragonType == EnumDragonType.FIRE;
				boolean isIceDragon = dragonType == EnumDragonType.ICE;
				boolean isLightningDragon = dragonType == EnumDragonType.LIGHTNING;

				if (isFire) {
					victim.setFire(5);
					if (isIceDragon) event.setAmount(event.getAmount() * 1.5F);
				} else if (isIce) {
					victim.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
					if (isFireDragon) event.setAmount(event.getAmount() * 1.5F);
			} else if (isLightning) {
					if (!victim.world.isRemote && victim.world.rand.nextInt(3) == 0) {
						ChainLightningUtils.createChainLightningFromTarget(victim.world, victim, attacker);
					}
					if (isFireDragon || isIceDragon) event.setAmount(event.getAmount() * 1.5F);
				}

			int uses = tag.getInteger(BLOOD_BATHE_USES_TAG) - 1;
				if (uses <= 0) {
					String oldBlood = tag.getString(BLOOD_BATHE_TAG);
					EnumDragonType oldType = "fire".equals(oldBlood) ? EnumDragonType.FIRE
						: "ice".equals(oldBlood) ? EnumDragonType.ICE
						: "lightning".equals(oldBlood) ? EnumDragonType.LIGHTNING : null;
					tag.removeTag(BLOOD_BATHE_TAG);
					tag.removeTag(BLOOD_BATHE_USES_TAG);
					if (tag.getSize() == 0) stack.setTagCompound(null);
					if (oldType != null) TinkerModifierBridge.syncTinkerModifier(stack, oldType, false);
				} else {
					tag.setInteger(BLOOD_BATHE_USES_TAG, uses);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (hasBloodBathe(stack)) {
			NBTTagCompound tag = stack.getTagCompound();
			String bloodType = tag.getString(BLOOD_BATHE_TAG);
			int uses = tag.getInteger(BLOOD_BATHE_USES_TAG);
			String typeName = StatCollector.translateToLocal("item.iceandfire.dragon_blood_bathe." + bloodType);
			event.getToolTip().add(TextFormatting.DARK_PURPLE + String.format(StatCollector.translateToLocal("item.iceandfire.dragon_blood_bathe.tooltip"), typeName));
			event.getToolTip().add(TextFormatting.GRAY + String.format(StatCollector.translateToLocal("item.iceandfire.dragon_blood_bathe.uses"), uses));
		}
	}

	public static EnumDragonType getDragonBloodType(ItemStack stack) {
		Item item = stack.getItem();
		if (item == IafItemRegistry.fire_dragon_blood) return EnumDragonType.FIRE;
		if (item == IafItemRegistry.ice_dragon_blood) return EnumDragonType.ICE;
		if (item == IafItemRegistry.lightning_dragon_blood) return EnumDragonType.LIGHTNING;
		return null;
	}

	public static boolean isWeaponOrTool(ItemStack stack) {
		return stack.getItem().isDamageable();
	}

	public static boolean hasBloodBathe(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(BLOOD_BATHE_TAG);
	}

	public static void applyBloodBathe(ItemStack stack, EnumDragonType type) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString(BLOOD_BATHE_TAG, type.getName());
		stack.getTagCompound().setInteger(BLOOD_BATHE_USES_TAG, MAX_BLOOD_BATHE_USES);
		TinkerModifierBridge.syncTinkerModifier(stack, type, true);
	}
}
