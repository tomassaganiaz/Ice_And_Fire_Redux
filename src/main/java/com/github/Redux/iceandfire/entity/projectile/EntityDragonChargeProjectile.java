package com.github.Redux.iceandfire.entity.projectile;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.entity.explosion.DragonExplosion;
import com.github.Redux.iceandfire.entity.explosion.FireChargeExplosion;
import com.github.Redux.iceandfire.entity.util.IDragonProjectile;
import com.github.Redux.iceandfire.entity.util.DragonUtils;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.github.Redux.iceandfire.enums.EnumParticle;
import com.github.Redux.iceandfire.integration.LycanitesCompat;
import com.github.Redux.iceandfire.util.ParticleHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Base consolidada para los proyectiles de carga (fuego, hielo, relámpago).
 * Reemplaza EntityDragonFireCharge/EntityDragonIceCharge/EntityDragonLightningCharge.
 * El tipo de dragón determina partículas, daño, efectos secundarios y velocidad.
 */
public class EntityDragonChargeProjectile extends EntityDragonProjectile {

	public int ticksInAir;
	protected EnumDragonType dragonType;

	public EntityDragonChargeProjectile(World worldIn, EnumDragonType type) {
		super(worldIn);
		this.dragonType = type;
	}

	public EntityDragonChargeProjectile(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ, EnumDragonType type) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
		this.dragonType = type;
		// relámpago usa velocidad por defecto de EntityFireball; fuego/hielo van más lento (0.07)
		if (type != EnumDragonType.LIGHTNING) {
			double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
			this.accelerationX = accelX / d0 * 0.07D;
			this.accelerationY = accelY / d0 * 0.07D;
			this.accelerationZ = accelZ / d0 * 0.07D;
		}
	}

	public EntityDragonChargeProjectile(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ, EnumDragonType type) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.dragonType = type;
		if (type == EnumDragonType.LIGHTNING) {
			this.setSize(0.5F, 0.5F);
		}
		double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		if (type == EnumDragonType.LIGHTNING) {
			// relámpago escala velocidad según etapa del dragón y si está volando
			this.accelerationX = accelX / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
			this.accelerationY = accelY / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
			this.accelerationZ = accelZ / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
		} else {
			this.accelerationX = accelX / d0 * 0.07D;
			this.accelerationY = accelY / d0 * 0.07D;
			this.accelerationZ = accelZ / d0 * 0.07D;
		}
	}

	@Override
	public void onUpdate() {
		// partículas de estela según tipo (fuego: FLAME, hielo: SNOWFLAKE, relámpago: SPARK)
		if (this.world.isRemote) {
			for (int i = 0; i < (dragonType == EnumDragonType.FIRE ? 4 : 10); ++i) {
				if (dragonType == EnumDragonType.FIRE) {
					ParticleHelper.spawnParticle(this.world, EnumParticleTypes.FLAME, this.posX + ((this.rand.nextDouble() - 0.5D) * width), this.posY + ((this.rand.nextDouble() - 0.5D) * width), this.posZ + ((this.rand.nextDouble() - 0.5D) * width), 0.0D, 0.0D, 0.0D);
				} else if (dragonType == EnumDragonType.ICE) {
					IceAndFire.PROXY.spawnParticle(EnumParticle.SNOWFLAKE, world, this.posX + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posY + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posZ + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
				} else if (dragonType == EnumDragonType.LIGHTNING) {
					IceAndFire.PROXY.spawnParticle(EnumParticle.SPARK, world, this.posX + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posY + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posZ + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
				}
			}
		}
		// el hielo no muere en agua
		if (dragonType != EnumDragonType.ICE && this.isInWater()) {
			setDead();
		}
		if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this))) {
			this.onEntityUpdate();

			if (this.isFireballFiery()) {
				this.setFire(1);
			}

			++this.ticksInAir;
			RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.getShootingEntity());

			if (raytraceresult != null) {
				this.onImpact(raytraceresult);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			ProjectileHelper.rotateTowardsMovement(this, 0.2F);
			float f = this.getMotionFactor();

			if (this.isInWater()) {
				if(this.world.isRemote) {
					for (int i = 0; i < 4; ++i) {
						ParticleHelper.spawnParticle(this.world, EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
					}
				}
				f = 0.8F;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= f;
			this.motionY *= f;
			this.motionZ *= f;
			if(this.world.isRemote) ParticleHelper.spawnParticle(this.world, this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
			this.setPosition(this.posX, this.posY, this.posZ);
		} else {
			this.setDead();
		}
	}

	/**
	 * Impacto: 1) explosión tipo charge contra bloques,
	 * 2) daño directo + efecto elemental contra entidades,
	 * 3) explosión secundaria pequeña siempre.
	 */
	@Override
	protected void onImpact(RayTraceResult movingObject) {
		boolean flag = this.world.getGameRules().getBoolean("mobGriefing");

		if (!this.world.isRemote) {
			if (movingObject.entityHit instanceof IDragonProjectile) {
				return;
			}
			if (DragonUtils.isOwner(movingObject.entityHit, shootingEntity) || DragonUtils.hasSameOwner(movingObject.entityHit, shootingEntity)) {
				this.setDead();
				return;
			}
			if (movingObject.entityHit == null || !(movingObject.entityHit instanceof IDragonProjectile) && movingObject.entityHit != shootingEntity) {
				// explosión contra bloque (DragonExplosion + FireChargeExplosion como daño secundario)
				if (this.shootingEntity != null) {
					int explodeSize = 2;
					if (this.shootingEntity instanceof EntityDragonBase) {
						explodeSize = 2 + ((EntityDragonBase) this.shootingEntity).getDragonStage();
					}
					DragonExplosion explosion = new DragonExplosion(dragonType, world, shootingEntity, this.posX, this.posY, this.posZ, explodeSize, flag);
					explosion.doExplosionA();
					explosion.doExplosionB(true);
					FireChargeExplosion explosion2 = new FireChargeExplosion(world, shootingEntity, this.posX, this.posY, this.posZ, explodeSize, flag);
					explosion2.doExplosionA();
					explosion2.doExplosionB(true);
				}
				this.setDead();
				return;
			}
			if (!(movingObject.entityHit instanceof IDragonProjectile) && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
				// daño directo a la entidad
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase) {
					if (dragonType == EnumDragonType.FIRE) {
						movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, IceAndFireConfig.DRAGON_SETTINGS.dragonFireChargeDamage);
						movingObject.entityHit.setFire(5);
					} else if (dragonType == EnumDragonType.ICE) {
						movingObject.entityHit.attackEntityFrom(IceAndFire.dragonIce, IceAndFireConfig.DRAGON_SETTINGS.dragonIceChargeDamage);
					} else if (dragonType == EnumDragonType.LIGHTNING) {
						movingObject.entityHit.attackEntityFrom(IceAndFire.dragonLightning, IceAndFireConfig.DRAGON_SETTINGS.dragonLightningChargeDamage);
					}
					if (movingObject.entityHit instanceof EntityLivingBase) {
						if (dragonType == EnumDragonType.ICE) {
							IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase) movingObject.entityHit);
							if (capability != null) {
								capability.setFrozen(200);
							}
						} else if (dragonType == EnumDragonType.LIGHTNING) {
							if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonKnockback) {
								double xRatio = this.shootingEntity.posX - movingObject.entityHit.posX;
								double zRatio = this.shootingEntity.posZ - movingObject.entityHit.posZ;
								((EntityLivingBase) movingObject.entityHit).knockBack(this.shootingEntity, 0.9F, xRatio, zRatio);
							}
							if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysis) {
								LycanitesCompat.applyParalysis(movingObject.entityHit, IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysisTicks);
							}
						}
						if (movingObject.entityHit instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) movingObject.entityHit;
							DragonUtils.fillBottleWithDragonBreath(player, dragonType);
						}
						if (((EntityLivingBase) movingObject.entityHit).getHealth() == 0) {
							((EntityDragonBase) this.shootingEntity).attackDecision = true;
						}
					}
				}
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
				// explosión pequeña adicional en impacto directo
				DragonExplosion explosion = new DragonExplosion(dragonType, world, null, this.posX, this.posY, this.posZ, 2, flag);
				if (shootingEntity != null) {
					explosion = new DragonExplosion(dragonType, world, shootingEntity, this.posX, this.posY, this.posZ, 2, flag);
				}
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				this.world.createExplosion(this, this.posX, this.posY, this.posZ, 4, flag);
			}
		}
		this.setDead();
	}

	@Override
	public float getCollisionBorderSize() {
		return 0F;
	}
}
