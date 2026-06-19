package com.github.Redux.iceandfire.entity;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.client.particle.lightning.ParticleLightningVector;
import com.github.Redux.iceandfire.entity.explosion.DragonExplosion;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonLightning;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonLightningCharge;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonProjectile;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.enums.EnumDragonEgg;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.github.Redux.iceandfire.integration.LycanitesCompat;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import com.github.Redux.iceandfire.message.MessageDragonSyncFire;
import com.github.Redux.iceandfire.misc.IafSoundRegistry;
import com.github.Redux.iceandfire.entity.ai.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

/** Dragón de relámpago — implementa el tipo LIGHTNING con rayos, parálisis y grietas en bloques */
public class EntityLightningDragon extends EntityDragonBase {

	public static final float[] growth_stage_1 = new float[]{1F, 3F};
	public static final float[] growth_stage_2 = new float[]{3F, 7F};
	public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
	public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
	public static final float[] growth_stage_5 = new float[]{20F, 30F};
	public static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/lightning_dragon_female"));
	public static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/lightning_dragon_male"));
	public static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/lightning_dragon_skeleton"));

	public EntityLightningDragon(World worldIn) {
		super(worldIn, EnumDragonType.LIGHTNING, 1, 1 + IceAndFireConfig.DRAGON_SETTINGS.dragonAttackDamage, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth * 0.046, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth * 1.15, 0.15F, 0.4F);
		this.setSize(0.78F, 1.2F);
		this.ignoreFrustumCheck = true;
		this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
		this.stepHeight = 1;
	}

	@Override
	protected void addCustomTasks() {
		this.tasks.addTask(2, new DragonAISwim(this));
	}

	public String getVariantName(int variant) {
		switch (variant) {
			default:
				return "electric_";
			case 1:
				return "amethyst_";
			case 2:
				return "copper_";
			case 3:
				return "black_";
		}
	}

	public Item getVariantScale(int variant) {
		switch (variant) {
			default:
				return EnumDragonEgg.ELECTRIC.scales;
			case 1:
				return EnumDragonEgg.AMETHYST.scales;
			case 2:
				return EnumDragonEgg.COPPER.scales;
			case 3:
				return EnumDragonEgg.BLACK.scales;
		}
	}

	public Item getVariantEgg(int variant) {
		switch (variant) {
			default:
				return EnumDragonEgg.ELECTRIC.egg;
			case 1:
				return EnumDragonEgg.AMETHYST.egg;
			case 2:
				return EnumDragonEgg.COPPER.egg;
			case 3:
				return EnumDragonEgg.BLACK.egg;
		}
	}

	@Override
	public Item getSummoningCrystal() {
		return IafItemRegistry.summoning_crystal_lightning;
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		this.heal(15F);
		this.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20, 1));
	}

	@Override
	public void addPotionEffect(PotionEffect potioneffectIn) {
		if (LycanitesCompat.isParalysisEffect(potioneffectIn)) {
			return;
		}
		super.addPotionEffect(potioneffectIn);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (DamageSource.LIGHTNING_BOLT.damageType.contentEquals(source.damageType) || IceAndFire.dragonLightning.damageType.contentEquals(source.damageType)) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected boolean isTimeToWake() {
		return !this.world.isDaytime() || this.world.isThundering();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote) {
			if ((this.isInLava() || isInWater()) && !this.isFlying() && !this.isChild() && !this.isHovering() && this.canMove()) {
				this.setHovering(true);
				if (this.isInLava()) {
					this.jump();
					this.motionY += 0.8D;
				}
				this.flyTicks = 0;
			}
			handleCombatTarget();
		}
	}

	@Override
	public Vec3d getHeadPosition() {
		float deadProg = this.modelDeadProgress * -0.02F;
		float hoverProg = this.hoverProgress * 0.03F;
		float flyProg = this.flyProgress * 0.01F;
		float sitProg = this.sitProgress * 0.005F;
		float sleepProg = this.sleepProgress * 0.005F;
		float speed_walk = 0.2F;
		float speed_idle = 0.05F;
		float degree_walk = 0.5F;
		float degree_idle = 0.5F;
		final float flightXz = Math.max(1.0F + flyProg + hoverProg, 1.06F);
		float xzMod = (0.575F - hoverProg * 0.45F + flyProg * 0.2F - sitProg * 0.52F - sleepProg * 0.9F) * flightXz * getRenderSize();
		float xzSleepMod = -1.25F * sleepProg * getRenderSize();
		boolean walking = (!this.isFlying() && !this.isHovering()) || (hoverProgress == 0 && flyProgress == 0);
		float bobWalk = walking ? this.bob(speed_walk * 2, degree_walk * 1.7F, this.limbSwing, this.limbSwingAmount * -0.0625F) : 0;
		float bobIdle = walking ? this.bob(speed_idle, degree_idle * 1.3F, this.ticksExisted, -0.0625F) : 0;
		float extraY = Math.max(0.75F - getRenderSize() * 0.03F, 0.625F);
		float headPosX = (float) (posX + xzMod * Math.cos((rotationYaw + 90) * Math.PI / 180) + xzSleepMod * Math.cos(rotationYaw * Math.PI / 180));
		float headPosY = (float) (posY + (extraY + sitProg * 7F + (flyProg + hoverProg) * 0.45F + deadProg + sleepProg * 6F) * getRenderSize() * 0.3F) - bobWalk - bobIdle;
		float headPosZ = (float) (posZ + xzMod * Math.sin((rotationYaw + 90) * Math.PI / 180) + xzSleepMod * Math.sin(rotationYaw * Math.PI / 180));
		return new Vec3d(headPosX, headPosY, headPosZ);
	}

	@Override
	protected void breathFireAtPos(BlockPos burningTarget) {
		if (this.isBreathingFire()) {
			if (this.isActuallyBreathingFire()) {
				rotationYaw = renderYawOffset;
				stimulateFire(burningTarget.getX() + 0.5F, burningTarget.getY() + 0.5F, burningTarget.getZ() + 0.5F, 1);
			}
		} else {
			this.setBreathingFire(true);
		}
	}

	@Override
	public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
		if (syncType == 1 && !world.isRemote) {
			//sync with client
			IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDragonSyncFire(this.getEntityId(), burnX, burnY, burnZ, 0));
		}
		if (this.world.isRemote && this.ticksExisted % 5 == 0 && this.isActuallyBreathingFire()) {
			this.playSoundClientSide(IafSoundRegistry.LIGHTNINGDRAGON_BREATH, 4, 1);
		}
		this.getNavigator().clearPath();
		this.burnParticleX = burnX;
		this.burnParticleY = burnY;
		this.burnParticleZ = burnZ;
		Vec3d headPos = getHeadPosition();
		double d2 = burnX - headPos.x;
		double d3 = burnY - headPos.y;
		double d4 = burnZ - headPos.z;
		double distance = Math.max(5 * this.getDistance(burnX, burnY, burnZ), 0);
		double conqueredDistance = burnProgress / 40D * distance;
		int increment = (int) Math.ceil(conqueredDistance / 100);
		for (int i = 0; i < conqueredDistance; i += increment) {
			double progressX = headPos.x + d2 * (i / (float) distance);
			double progressY = headPos.y + d3 * (i / (float) distance);
			double progressZ = headPos.z + d4 * (i / (float) distance);
			if (!canPositionBeSeen(progressX, progressY, progressZ)) {
				RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(progressX, progressY, progressZ), false, true, false);
				if (result != null) {
					BlockPos pos = result.getBlockPos();
					if (!world.isRemote) {
						DragonExplosion explosion = new DragonExplosion(EnumDragonType.LIGHTNING, this.world, this, pos.getX(), pos.getY(), pos.getZ(), this.getDragonStage() * 2.5F, this.world.getGameRules().getBoolean("mobGriefing"));
						explosion.doExplosionA();
						explosion.doExplosionB(true);
					} else if (rand.nextInt(100) >= 60) {
						ParticleLightningVector source = new ParticleLightningVector(headPos);
						ParticleLightningVector target = ParticleLightningVector.fromBlockPos(pos);
						IceAndFire.PROXY.spawnLightningEffect(world, source, target, false);
					}
					return;
				}
			}
		}
		if (burnProgress >= 40D && canPositionBeSeen(burnX, burnY, burnZ)) {
			double spawnX = burnX + (rand.nextFloat() * 3.0) - 1.5;
			double spawnY = burnY + (rand.nextFloat() * 3.0) - 1.5;
			double spawnZ = burnZ + (rand.nextFloat() * 3.0) - 1.5;
			if (!world.isRemote) {
				DragonExplosion explosion = new DragonExplosion(EnumDragonType.LIGHTNING, this.world, this, spawnX, spawnY, spawnZ, this.getDragonStage() * 2.5F, this.world.getGameRules().getBoolean("mobGriefing"));
				explosion.doExplosionA();
				explosion.doExplosionB(true);
			} else {
				ParticleLightningVector source = new ParticleLightningVector(headPos);
				ParticleLightningVector target = new ParticleLightningVector(spawnX, spawnY, spawnZ);
				IceAndFire.PROXY.spawnLightningEffect(world, source, target, true);
			}
		}
	}

	@Override
	public ResourceLocation getDeadLootTable() {
		if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2) {
			return SKELETON_LOOT;
		}else{
			return isMale() ? MALE_LOOT : FEMALE_LOOT;
		}
	}

	@Override
	protected ItemStack getSkull() {
		return new ItemStack(IafItemRegistry.dragon_skull, 1, 2);
	}

	@Override
	protected ItemStack getHorn() {
		return new ItemStack(IafItemRegistry.dragon_horn_lightning);
	}

	@Override
	public Item getBlood() {
		return IafItemRegistry.lightning_dragon_blood;
	}

	@Override
	public Item getHeart() {
		return IafItemRegistry.lightning_dragon_heart;
	}

	@Override
	public Item getFlesh() {
		return IafItemRegistry.lightning_dragon_flesh;
	}

	@Override
	protected int getBaseEggTypeValue() {
		return 8;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_IDLE : this.isAdult() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_IDLE : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_HURT : this.isAdult() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_HURT : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_DEATH : this.isAdult() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_DEATH : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_DEATH;
	}

	@Override
	public SoundEvent getRoarSound() {
		return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_ROAR : this.isAdult() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_ROAR : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_ROAR;
	}

	@Override
	public SoundEvent getShortBreathSound() {
		return IafSoundRegistry.LIGHTNINGDRAGON_BREATH_SHORT;
	}

	@Override
	protected EntityDragonProjectile createChargeProjectile(World world, double d2, double d3, double d4) {
		return new EntityDragonLightningCharge(world, this, d2, d3, d4);
	}

	@Override
	protected EntityDragonProjectile createBreathProjectile(World world, double d2, double d3, double d4) {
		return new EntityDragonLightning(world, this, d2, d3, d4);
	}

	@Override
	protected DragonExplosion createExplosion(World world, double x, double y, double z, float size, boolean griefing) {
		return new DragonExplosion(EnumDragonType.LIGHTNING, world, this, x, y, z, size, griefing);
	}

	@Override
	protected Item getStewItem() {
		return IafItemRegistry.lightning_stew;
	}

}
