package com.github.Redux.iceandfire.event;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.core.ModPotions;
import com.github.Redux.iceandfire.entity.*;
import com.github.Redux.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.Redux.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.entity.util.*;
import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import com.github.Redux.iceandfire.item.ItemBloodedArmor;
import com.github.Redux.iceandfire.item.ItemSeaSerpentArmor;
import com.github.Redux.iceandfire.item.ItemTideTrident;
import com.github.Redux.iceandfire.item.ItemTrollArmor;
import com.github.Redux.iceandfire.message.MessagePlayerHitMultipart;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;
/** DragonEntityHandler — Dragon Entity Handler */


public class DragonEntityHandler {

	private Random rand = new Random();

	@SubscribeEvent
	public void onPlayerAttackMob(AttackEntityEvent event) {
		if (CompatLoadUtil.isRLCombatLoaded()) return;
		if (event.getTarget() instanceof EntityMultipartPart && event.getEntity() instanceof EntityPlayer) {
			event.setCanceled(true);
			EntityLivingBase parent = ((EntityMultipartPart)event.getTarget()).getParent();
			((EntityPlayer) event.getEntity()).attackTargetEntityWithCurrentItem(parent);
			int extraData = 0;
			if (event.getTarget() instanceof EntityHydraHead && parent instanceof EntityHydra) {
				extraData = ((EntityHydraHead)event.getTarget()).headIndex;
				((EntityHydra) parent).triggerHeadFlags(extraData);
			}
			IceAndFire.NETWORK_WRAPPER.sendToServer(new MessagePlayerHitMultipart(parent.getEntityId(), extraData));
		}
	}

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityWitherSkeleton) {
			entity.dropItem(IafItemRegistry.witherbone, entity.getRNG().nextInt(2));
		}
		if (entity instanceof EntityLiving) {
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(entity);
			if (capability != null && capability.isStoned()) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDespawn(LivingSpawnEvent.AllowDespawn event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityLiving) {
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(entity);
			if (capability != null && capability.isStoned()) {
				event.setResult(Event.Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void onLivingAttacked(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityDragonBase && event.getAmount() > 0F) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			if (dragon.isSleeping()) {
				dragon.setSleeping(false);
			}
		}
		if (event.getSource().getTrueSource() != null) {
			Entity attacker = event.getSource().getTrueSource();
			if (isAnimaniaChicken(entity) && attacker instanceof EntityLivingBase) {
				signalChickenAlarm(entity, (EntityLivingBase) attacker);
			}
			if (DragonUtils.isVillager(entity) && attacker instanceof EntityLivingBase){
				signalAmphithereAlarm(entity, (EntityLivingBase) attacker);
			}
		}
	}

	@SubscribeEvent
	public void onLivingSetTarget(LivingSetAttackTargetEvent event) {
		if (event.getTarget() != null) {
			EntityLivingBase attacker = event.getEntityLiving();
			if (isAnimaniaChicken(event.getTarget())) {
				signalChickenAlarm(event.getTarget(), attacker);
			}
			if(DragonUtils.isVillager(event.getTarget())){
				signalAmphithereAlarm(event.getTarget(), attacker);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event) {
		if (event.getTarget() != null && isAnimaniaSheep(event.getTarget())) {
			float dist = IceAndFireConfig.ENTITY_SETTINGS.cyclopesSheepSearchLength;
			List<Entity> list = event.getTarget().world.getEntitiesWithinAABBExcludingEntity(event.getEntityPlayer(), event.getEntityPlayer().getEntityBoundingBox().expand(dist, dist, dist));
			list.sort(new EntityAINearestAttackableTarget.Sorter(event.getEntityPlayer()));
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if (entity instanceof EntityCyclops) {
						EntityCyclops cyclops = (EntityCyclops) entity;
						if (!cyclops.isBlinded() && !event.getEntityPlayer().capabilities.isCreativeMode) {
							cyclops.setAttackTarget(event.getEntityPlayer());
						}
					}
				}
			}
		}
		if (event.getTarget() instanceof EntityLiving && event.getTarget().isEntityAlive()) {
			boolean stonePlayer = event.getTarget() instanceof EntityStoneStatue;
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLiving)event.getTarget());
			if (capability != null && capability.isStoned() || stonePlayer) {
				((EntityLiving) event.getTarget()).setHealth(((EntityLiving) event.getTarget()).getMaxHealth());
				if (event.getEntityPlayer() != null) {
					ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
					if (stack.getItem() != null && (stack.getItem().canHarvestBlock(Blocks.STONE.getDefaultState()) || stack.getItem().getTranslationKey().contains("pickaxe"))) {
						boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
						boolean ready = false;
						if (capability != null && !stonePlayer) {
							capability.tickData();
							ready = capability.getAdditionalData() > 9;
						}
						if (stonePlayer) {
							EntityStoneStatue statue = (EntityStoneStatue) event.getTarget();
							statue.setCrackAmount(statue.getCrackAmount() + 1);
							ready = statue.getCrackAmount() > 9;
						}
						if (ready) {
							event.getTarget().setDead();
							if (silkTouch) {
								ItemStack statuette = new ItemStack(IafItemRegistry.stone_statue);
								NBTTagCompound compound = new NBTTagCompound();
								compound.setBoolean("IAFStoneStatueEntityPlayer", stonePlayer);
								compound.setInteger("IAFStoneStatueEntityID", stonePlayer ? 90 : EntityList.getID(event.getTarget().getClass()));
								((EntityLiving)event.getTarget()).writeEntityToNBT(compound);
								compound.removeTag("Items");
								compound.removeTag("ArmorItems");
								compound.removeTag("HandItems");
								statuette.setTagCompound(compound);
								if (!event.getTarget().world.isRemote) {
									event.getTarget().entityDropItem(statuette, 1);
								}
							} else {
								if (!(event.getTarget()).world.isRemote) {
									event.getTarget().dropItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 2 + event.getEntityLiving().getRNG().nextInt(4));
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getEntity() instanceof EntityTroll) {
			if (!event.isSpawner()) {
				BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
				int spawnCheckHeight = IceAndFireConfig.ENTITY_SPAWNING.trollSpawnCheckHeight;
				if (!IceAndFireConfig.getTrollSpawnHeight().isEmpty()) {
					Biome biome = event.getWorld().getBiome(pos);
					String biomeName =  biome.getRegistryName() != null ? biome.getRegistryName().toString() : null;
					if (biomeName != null && IceAndFireConfig.getTrollSpawnHeight().containsKey(biomeName)) {
						spawnCheckHeight = IceAndFireConfig.getTrollSpawnHeight().get(biomeName);
					}
				}
				if (pos.getY() > spawnCheckHeight) {
					event.setResult(Event.Result.DENY);
				}
			}
		}
	}

	@SubscribeEvent
	public void onSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityDreadLich && event.getSpawner() != null) {
			EntityDreadLich lich = (EntityDreadLich) entity;
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() != null && isAnimaniaSheep(event.getEntity()) && event.getEntity() instanceof EntityAnimal) {
			EntityAnimal animal = (EntityAnimal) event.getEntity();
			animal.tasks.addTask(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
		}
		if (event.getEntity() != null && DragonUtils.isVillager(event.getEntity()) && event.getEntity() instanceof EntityCreature && IceAndFireConfig.DRAGON_SETTINGS.villagersFearDragons) {
			EntityCreature villager = (EntityCreature) event.getEntity();
			villager.tasks.addTask(1, new VillagerAIFearUntamed(villager, EntityLivingBase.class, (entity) -> entity instanceof IVillagerFear, 8.0F, 0.8D, 0.8D));
		}
		if (event.getEntity() != null && DragonUtils.isLivestock(event.getEntity()) && event.getEntity() instanceof EntityCreature && IceAndFireConfig.DRAGON_SETTINGS.animalsFearDragons) {
			EntityCreature animal = (EntityCreature) event.getEntity();
			animal.tasks.addTask(1, new VillagerAIFearUntamed(animal, EntityLivingBase.class, (entity) -> entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(animal), 12.0F, 1.2D, 1.5D));
		}
		if (event.getEntity() instanceof EntityMyrmexQueen && !event.getWorld().isRemote) {
			((EntityMyrmexQueen) event.getEntity()).refreshIncorrectTrades();
		}
	}

	@SubscribeEvent
	public void onMobGrief(EntityMobGriefingEvent event) {
		if (event.getEntity() instanceof EntityLiving && !event.getEntity().world.isRemote && ((EntityLiving)event.getEntity()).canPickUpLoot()) {
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase)event.getEntity());
			if (capability != null && capability.isStoned() || event.getEntity() instanceof EntityStoneStatue) {
				event.setResult(Event.Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();

		if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemSeaSerpentArmor || entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemSeaSerpentArmor || entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemSeaSerpentArmor || entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemSeaSerpentArmor) {
			entity.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 50, 0, false, false));
			if (entity.isWet()) {
				int headMod = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
				int chestMod = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
				int legMod = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
				int footMod = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
				entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 50, headMod + chestMod + legMod + footMod - 1, false, false));

			}
		}
		if (IceAndFireConfig.ENTITY_SETTINGS.chickensLayRottenEggs && !entity.world.isRemote && isAnimaniaChicken(entity) && !entity.isChild() && entity instanceof EntityAnimal) {
			if (entity.ticksExisted > 30 && entity.getRNG().nextInt(IceAndFireConfig.ENTITY_SETTINGS.chickenEggChance * 6000) == 0) {
				entity.playSound(SoundEvents.ENTITY_CHICKEN_HURT, 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				entity.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				entity.dropItem(IafItemRegistry.rotten_egg, 1);
			}
		}

		if (entity.isInWater() && entity.getActivePotionEffect(ModPotions.acid) != null) {
			entity.removePotionEffect(ModPotions.acid);
		}
	}

	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof EntityLiving) {
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase)event.getTarget());
			if (capability != null && capability.isStoned()) {
				event.setCanceled(true);
			}
        }
	}

	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityLiving) {
			IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase)event.getTarget());
			if (capability != null && capability.isStoned()) {
				event.setCanceled(true);
			}
        }
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		DamageSource source = event.getSource();
		EntityLivingBase victim = event.getEntityLiving();

		if (source.isProjectile()) {
			float multi = 1;
			if (victim.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor) {
				multi -= 0.1F;
			}
			if (victim.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor) {
				multi -= 0.3F;
			}
			if (victim.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor) {
				multi -= 0.2F;
			}
			if (victim.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemTrollArmor) {
				multi -= 0.1F;
			}
			event.setAmount(event.getAmount() * multi);
		}

		if (event.getAmount() <= 0.0f
				|| source.isProjectile()
				|| source.isFireDamage()
				|| source.isExplosion()
				|| source.isMagicDamage()) {
			return;
		}

		if (source.getImmediateSource() == source.getTrueSource() && source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
			ItemStack stack = attacker.getHeldItemMainhand();
			if (stack.getItem() instanceof ItemTideTrident
					&& ItemTideTrident.isEmpty(stack)
					&& source.getDamageType().equals("player")) {
				event.setAmount(1.0f);
			}
			if (!CompatLoadUtil.isFirstAidLoaded()) {
				if (victim instanceof EntityPlayer) {
					ItemBloodedArmor.applySetEffect((EntityPlayer) victim, attacker);
				}
			}
		}
	}

	static void signalChickenAlarm(EntityLivingBase chicken, EntityLivingBase attacker){
		float d0 = IceAndFireConfig.ENTITY_SETTINGS.cockatriceChickenSearchLength;
		List<Entity> list = chicken.world.getEntitiesWithinAABB(EntityCockatrice.class, (new AxisAlignedBB(chicken.posX, chicken.posY, chicken.posZ, chicken.posX + 1.0D, chicken.posY + 1.0D, chicken.posZ + 1.0D)).grow(d0, 10.0D, d0));
		list.sort(new EntityAINearestAttackableTarget.Sorter(attacker));
		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if (entity instanceof EntityCockatrice && !(attacker instanceof EntityCockatrice)) {
					EntityCockatrice cockatrice = (EntityCockatrice) entity;
					if (!DragonUtils.hasSameOwner(cockatrice, attacker)) {
						if (attacker instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) attacker;
							if (!player.isCreative() && !cockatrice.isOwner(player)) {
								cockatrice.setAttackTarget(player);
							}
						} else {
							cockatrice.setAttackTarget(attacker);
						}
					}
				}
			}
		}
	}

	static void signalAmphithereAlarm(EntityLivingBase villager, EntityLivingBase attacker){
		float d0 = (float)IceAndFireConfig.ENTITY_SETTINGS.amphithereVillagerSearchLength;
		List<Entity> list = villager.world.getEntitiesWithinAABB(EntityAmphithere.class, (new AxisAlignedBB(villager.posX - 1.0D, villager.posY - 1.0D, villager.posZ - 1.0D, villager.posX + 1.0D, villager.posY + 1.0D, villager.posZ + 1.0D)).grow(d0, d0, d0));
		list.sort(new EntityAINearestAttackableTarget.Sorter(attacker));
		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if (entity instanceof EntityAmphithere && !(attacker instanceof EntityAmphithere)) {
					EntityAmphithere amphithere = (EntityAmphithere) entity;
					if (!DragonUtils.hasSameOwner(amphithere, attacker)) {
						if (attacker instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) attacker;
							if (!player.isCreative() && !amphithere.isOwner(player)) {
								amphithere.setAttackTarget(player);
							}
						} else {
							amphithere.setAttackTarget(attacker);
						}
					}
				}
			}
		}
	}

	public static boolean isAnimaniaSheep(Entity entity) {
		String className = entity.getClass().getName();
		return className.contains("sheep") || entity instanceof EntitySheep;
	}

	public static boolean isAnimaniaChicken(Entity entity) {
		String className = entity.getClass().getName();
		return (className.contains("chicken") || entity instanceof EntityChicken) && entity instanceof EntityLiving && !entity.isCreatureType(EnumCreatureType.MONSTER, false);
	}

	public static boolean isAnimaniaFerret(Entity entity) {
		String className = entity.getClass().getName();
		return className.contains("ferret") || className.contains("polecat");
	}

	public static boolean isQuarkCrab(Entity entity) {
		if (entity == null) {
			return false;
		}
		String className = entity.getClass().getSimpleName();
		return "EntityCrab".equals(className);
	}
}
