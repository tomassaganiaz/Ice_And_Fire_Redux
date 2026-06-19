package com.github.Redux.iceandfire.entity;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.entity.explosion.DragonExplosion;
import com.github.Redux.iceandfire.enums.EnumDragonEgg;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import com.github.Redux.iceandfire.message.MessageDragonSyncFire;
import com.github.Redux.iceandfire.misc.IafSoundRegistry;
import com.github.Redux.iceandfire.entity.ai.*;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonIce;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonIceCharge;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonProjectile;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

/** Dragón de hielo — implementa el tipo ICE con congelación, resistencia al frío y escarcha de bloques */
public class EntityIceDragon extends EntityDragonBase {

	private static final DataParameter<Boolean> SWIMMING = EntityDataManager.<Boolean>createKey(EntityIceDragon.class, DataSerializers.BOOLEAN);
	public static final float[] growth_stage_1 = new float[]{1F, 3F};
	public static final float[] growth_stage_2 = new float[]{3F, 7F};
	public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
	public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
	public static final float[] growth_stage_5 = new float[]{20F, 30F};
	public boolean isSwimming;
	public float swimProgress;
	public int ticksSwimming;
	public int swimCycle;
	public BlockPos waterTarget;
	public static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_female"));
	public static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_male"));
	public static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_skeleton"));

	public EntityIceDragon(World worldIn) {
		super(worldIn, EnumDragonType.ICE, 1, 1 + IceAndFireConfig.DRAGON_SETTINGS.dragonAttackDamage, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth * 0.046, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth * 1.15, 0.15F, 0.4F);
		this.setSize(0.78F, 1.2F);
		this.ignoreFrustumCheck = true;
		this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
		this.stepHeight = 1;
	}

	@Override
	protected void addCustomTasks() {
		this.tasks.addTask(5, new DragonAIWaterTarget(this));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SWIMMING, Boolean.FALSE);
	}

	public String getVariantName(int variant) {
		switch (variant) {
			default:
				return "blue_";
			case 1:
				return "white_";
			case 2:
				return "sapphire_";
			case 3:
				return "silver_";
		}
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public Item getVariantScale(int variant) {
		switch (variant) {
			default:
				return EnumDragonEgg.BLUE.scales;
			case 1:
				return EnumDragonEgg.WHITE.scales;
			case 2:
				return EnumDragonEgg.SAPPHIRE.scales;
			case 3:
				return EnumDragonEgg.SILVER.scales;
		}
	}

	public Item getVariantEgg(int variant) {
		switch (variant) {
			default:
				return EnumDragonEgg.BLUE.egg;
			case 1:
				return EnumDragonEgg.WHITE.egg;
			case 2:
				return EnumDragonEgg.SAPPHIRE.egg;
			case 3:
				return EnumDragonEgg.SILVER.egg;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("Swimming", this.isSwimming());
		compound.setInteger("SwimmingTicks", this.ticksSwimming);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setSwimming(compound.getBoolean("Swimming"));
		this.ticksSwimming = compound.getInteger("SwimmingTicks");
	}

	@Override
	public Item getSummoningCrystal() {
		return IafItemRegistry.summoning_crystal_ice;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (IceAndFire.dragonIce.damageType.contentEquals(source.damageType) || "ooze".contentEquals(source.damageType) || "cold_fire".contentEquals(source.damageType)) {
				return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote) {
			if (this.isInLava() && !this.isFlying() && !this.isChild() && !this.isHovering() && this.canMove()) {
				this.setHovering(true);
				this.jump();
				this.motionY += 0.8D;
				this.flyTicks = 0;
			}
			handleCombatTarget();
			if (this.isInsideWaterBlock() && !this.isSwimming() && (!this.isFlying() && !this.isHovering() || this.flyTicks > 100)) {
				this.setSwimming(true);
				this.setHovering(false);
				this.setFlying(false);
				this.flyTicks = 0;
				this.ticksSwimming = 0;
			}
			if (this.isInsideWaterBlock()) {
				swimAround();
			}
			if (!this.isInsideWaterBlock() && this.isSwimming()) {
				this.setSwimming(false);
				ticksSwimming = 0;
			}
			if (this.isSwimming()) {
				ticksSwimming++;
				if ((this.isInsideWaterBlock() || this.isOverWater()) && (ticksSwimming > 4000 || this.getAttackTarget() != null && this.isInWater() != this.getAttackTarget().isInWater()) && !this.isChild() && !this.isHovering() && !this.isFlying()) {
					this.setHovering(true);
					this.jump();
					this.motionY += 0.8D;
					this.flyTicks = 0;
					this.setSwimming(false);
				}
			}
		}
		boolean swimming = isSwimming() && !isHovering() && !isFlying() && ridingProgress == 0;
		if (swimming && swimProgress < 20.0F) {
			swimProgress += 0.5F;
		} else if (!swimming && swimProgress > 0.0F) {
			swimProgress -= 0.5F;
		}
		if (swimCycle < 48) {
			swimCycle += 2;
		} else {
			swimCycle = 0;
		}
		if (this.isModelDead() && swimCycle != 0) {
			swimCycle = 0;
		}
	}

	public boolean isInsideWaterBlock() {
		return this.isInsideOfMaterial(Material.WATER);
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
			this.playSoundClientSide(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
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
			if (canPositionBeSeen(progressX, progressY, progressZ)) {
				if (world.isRemote && rand.nextInt(5) == 0) {
					IceAndFire.PROXY.spawnDragonParticle(this);
				}
			} else {
				if (!world.isRemote) {
					RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(progressX, progressY, progressZ), false, true, false);
					if (result != null) {
						BlockPos pos = result.getBlockPos();
						DragonExplosion explosion = new DragonExplosion(EnumDragonType.ICE, this.world, this, pos.getX(), pos.getY(), pos.getZ(), this.getDragonStage() * 2.5F, this.world.getGameRules().getBoolean("mobGriefing"));
						explosion.doExplosionA();
						explosion.doExplosionB(true);
					}
				}
			}
		}
		if (burnProgress >= 40D && canPositionBeSeen(burnX, burnY, burnZ)) {
			double spawnX = burnX + (rand.nextFloat() * 3.0) - 1.5;
			double spawnY = burnY + (rand.nextFloat() * 3.0) - 1.5;
			double spawnZ = burnZ + (rand.nextFloat() * 3.0) - 1.5;
			if (!world.isRemote) {
				DragonExplosion explosion = new DragonExplosion(EnumDragonType.ICE, this.world, this, spawnX, spawnY, spawnZ, this.getDragonStage() * 2.5F, this.world.getGameRules().getBoolean("mobGriefing"));
				explosion.doExplosionA();
				explosion.doExplosionB(true);
			}
		}
	}

	public void swimAround() {
		if (waterTarget != null) {
			if (!isTargetInWater() || getDistance(waterTarget.getX() + 0.5D, waterTarget.getY() + 0.5D, waterTarget.getZ() + 0.5D) < 2 || ticksSwimming > 6000) {
				waterTarget = null;
			}
			swimTowardsTarget();
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

	public void swimTowardsTarget() {
		if (waterTarget != null && isTargetInWater() && this.isInsideWaterBlock() && this.getDistanceSquared(new Vec3d(waterTarget.getX(), this.posY, waterTarget.getZ())) > 3) {
			double targetX = waterTarget.getX() + 0.5D - posX;
			double targetY = waterTarget.getY() + 1D - posY;
			double targetZ = waterTarget.getZ() + 0.5D - posZ;
			motionX += (Math.signum(targetX) * 0.5D - motionX) * 0.100000000372529 * ((3 * ((double) this.getAgeInDays() / 125)) + 2);
			motionY += (Math.signum(targetY) * 0.5D - motionY) * 0.100000000372529 * ((3 * ((double) this.getAgeInDays() / 125)) + 2);
			motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * 0.100000000372529 * ((3 * ((double) this.getAgeInDays() / 125)) + 2);
			float angle = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) - 90.0F;
			float rotation = MathHelper.wrapDegrees(angle - rotationYaw);
			moveForward = 0.5F;
			prevRotationYaw = rotationYaw;
			rotationYaw += rotation;
		} else {
			this.waterTarget = null;
		}
	}

	protected boolean isTargetInWater() {
		return waterTarget != null && (world.getBlockState(waterTarget).getMaterial() == Material.WATER);
	}

	public boolean isSwimming() {
		if (world.isRemote) {
			boolean swimming = this.dataManager.get(SWIMMING);
			this.isSwimming = swimming;
			return swimming;
		}
		return isSwimming;
	}

	public void setSwimming(boolean swimming) {
		this.dataManager.set(SWIMMING, swimming);
		if (!world.isRemote) {
			this.isSwimming = swimming;
		}
	}

	@Override
	protected ItemStack getSkull() {
		return new ItemStack(IafItemRegistry.dragon_skull, 1, 1);
	}

	@Override
	protected ItemStack getHorn() {
		return new ItemStack(IafItemRegistry.dragon_horn_ice);
	}

	@Override
	public Item getBlood() {
		return IafItemRegistry.ice_dragon_blood;
	}

	@Override
	public Item getHeart() {
		return IafItemRegistry.ice_dragon_heart;
	}

	@Override
	public Item getFlesh() {
		return IafItemRegistry.ice_dragon_flesh;
	}

	@Override
	protected int getBaseEggTypeValue() {
		return 4;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_IDLE : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_IDLE : IafSoundRegistry.ICEDRAGON_CHILD_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_HURT : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_HURT : IafSoundRegistry.ICEDRAGON_CHILD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_DEATH : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_DEATH : IafSoundRegistry.ICEDRAGON_CHILD_DEATH;
	}

	@Override
	public SoundEvent getRoarSound() {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_ROAR : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_ROAR : IafSoundRegistry.ICEDRAGON_CHILD_ROAR;
	}

	@Override
	public SoundEvent getShortBreathSound() {
		return IafSoundRegistry.ICEDRAGON_BREATH_SHORT;
	}

	@Override
	protected EntityDragonProjectile createChargeProjectile(World world, double d2, double d3, double d4) {
		return new EntityDragonIceCharge(world, this, d2, d3, d4);
	}

	@Override
	protected EntityDragonProjectile createBreathProjectile(World world, double d2, double d3, double d4) {
		return new EntityDragonIce(world, this, d2, d3, d4);
	}

	@Override
	protected DragonExplosion createExplosion(World world, double x, double y, double z, float size, boolean griefing) {
		return new DragonExplosion(EnumDragonType.ICE, world, this, x, y, z, size, griefing);
	}

	@Override
	protected Item getStewItem() {
		return IafItemRegistry.frost_stew;
	}

}
