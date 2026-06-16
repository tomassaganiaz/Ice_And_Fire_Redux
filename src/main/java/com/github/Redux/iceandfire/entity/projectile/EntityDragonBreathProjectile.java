package com.github.Redux.iceandfire.entity.projectile;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.client.particle.lightning.ParticleLightningVector;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.entity.explosion.DragonExplosion;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Base consolidada para los proyectiles de breath (fuego, hielo, relámpago).
 * Reemplaza EntityDragonFire/EntityDragonIce/EntityDragonLightning.
 * Todo el comportamiento varía según EnumDragonType.
 */
public class EntityDragonBreathProjectile extends EntityDragonProjectile {

	private int ticksInAir;
	private Vec3d lastPos; // solo para relámpago: dibuja trayectoria
	protected EnumDragonType dragonType;

	public EntityDragonBreathProjectile(World worldIn, EnumDragonType type) {
		super(worldIn);
		this.dragonType = type;
	}

	public EntityDragonBreathProjectile(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ, EnumDragonType type) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
		this.dragonType = type;
	}

	public EntityDragonBreathProjectile(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ, EnumDragonType type) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.dragonType = type;
		this.setSize(0.5F, 0.5F);
		double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
		this.accelerationY = accelY / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
		this.accelerationZ = accelZ / d0 * (0.1D * (shooter.isFlying() ? 4 * shooter.getDragonStage() : 1));
	}

	@Override
	public void onUpdate() {
		if (ticksInAir > 160) {
			setDead();
		}
		// el hielo no se apaga en agua (a diferencia de fuego/relámpago)
		if (this.dragonType != EnumDragonType.ICE && this.isInWater()) {
			setDead();
		}
		if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this))) {
			if (this.dragonType == EnumDragonType.LIGHTNING && lastPos == null) {
				lastPos = new Vec3d(this.posX, this.posY, this.posZ);
			}

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
			if (this.world.isRemote) {
				if (dragonType == EnumDragonType.FIRE) {
					for (int i = 0; i < 6; ++i) {
						IceAndFire.PROXY.spawnParticle(EnumParticle.DRAGON_FIRE, world, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
					}
				} else if (dragonType == EnumDragonType.ICE) {
					for (int i = 0; i < 6; ++i) {
						IceAndFire.PROXY.spawnParticle(EnumParticle.DRAGON_ICE, world, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
					}
				}
				if (dragonType != EnumDragonType.LIGHTNING) {
					ParticleHelper.spawnParticle(this.world, this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
				}
			}
			if (dragonType == EnumDragonType.LIGHTNING) {
				// dibuja el rayo entre la posición anterior y la actual
				emitLightningFx(new Vec3d(this.posX, this.posY, this.posZ));
			}
			this.setPosition(this.posX, this.posY, this.posZ);
		} else {
			this.setDead();
		}
	}

	/**
	 * Maneja el impacto: 1) esquiva proyectiles del mismo dueño,
	 * 2) si no golpea entidad → explosión de breath,
	 * 3) si golpea entidad → daño directo + efecto según tipo.
	 */
	@Override
	protected void onImpact(RayTraceResult movingObject) {
		boolean flag = this.world.getGameRules().getBoolean("mobGriefing");

		if (dragonType == EnumDragonType.LIGHTNING) {
			emitLightningFx(movingObject.hitVec);
		}

		if (!this.world.isRemote) {
			if (movingObject.entityHit instanceof IDragonProjectile) {
				return;
			}
			if (DragonUtils.isOwner(movingObject.entityHit, shootingEntity) || DragonUtils.hasSameOwner(movingObject.entityHit, shootingEntity)) {
				this.setDead();
				return;
			}
			if (movingObject.entityHit == null || !(movingObject.entityHit instanceof IDragonProjectile) && this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase && movingObject.entityHit != shootingEntity) {
				// impacto contra bloque o entidad inválida → explosión tipo breath
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase) {
					DragonExplosion explosion = new DragonExplosion(dragonType, world, shootingEntity, this.posX, this.posY, this.posZ, ((EntityDragonBase) this.shootingEntity).getDragonStage() * 2.5F, flag);
					explosion.doExplosionA();
					explosion.doExplosionB(true);
				}
				this.setDead();
				return;
			}
			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof IDragonProjectile) && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
				// impacto directo contra entidad enemiga
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase) {
					if (movingObject.entityHit instanceof EntityLivingBase && ((EntityLivingBase) movingObject.entityHit).getHealth() == 0) {
						((EntityDragonBase) this.shootingEntity).attackDecision = true;
					}
				}
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
				if (dragonType == EnumDragonType.FIRE) {
					movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, IceAndFireConfig.DRAGON_SETTINGS.dragonFireDamage);
					movingObject.entityHit.setFire(5);
				} else if (dragonType == EnumDragonType.ICE) {
					movingObject.entityHit.attackEntityFrom(IceAndFire.dragonIce, IceAndFireConfig.DRAGON_SETTINGS.dragonIceDamage);
					if (movingObject.entityHit instanceof EntityLivingBase) {
						IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability((EntityLivingBase) movingObject.entityHit);
						if (capability != null) {
							capability.setFrozen(200);
						}
					}
				} else if (dragonType == EnumDragonType.LIGHTNING) {
					movingObject.entityHit.attackEntityFrom(IceAndFire.dragonLightning, IceAndFireConfig.DRAGON_SETTINGS.dragonLightningDamage);
					if (movingObject.entityHit instanceof EntityLivingBase) {
						if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonKnockback && this.shootingEntity != null) {
							double xRatio = this.shootingEntity.posX - movingObject.entityHit.posX;
							double zRatio = this.shootingEntity.posZ - movingObject.entityHit.posZ;
							((EntityLivingBase) movingObject.entityHit).knockBack(this.shootingEntity, 0.3F, xRatio, zRatio);
						}
						if (IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysis) {
							LycanitesCompat.applyParalysis(movingObject.entityHit, IceAndFireConfig.DRAGON_SETTINGS.lightningDragonParalysisTicks);
						}
					}
				}
				if (movingObject.entityHit instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) movingObject.entityHit;
					DragonUtils.fillBottleWithDragonBreath(player, dragonType);
				}
			}
		}
		this.setDead();
	}

	/**
	 * Dibuja la trayectoria del rayo entre ticks (solo cliente).
	 * Usa lastPos/pos actual para crear segmentos de ParticleLightningVector.
	 */
	private void emitLightningFx(Vec3d pos) {
		if (!world.isRemote) {
			return;
		}
		if (lastPos != null && !pos.equals(lastPos)) {
			ParticleLightningVector source = new ParticleLightningVector(lastPos);
			ParticleLightningVector target = new ParticleLightningVector(pos);
			IceAndFire.PROXY.spawnLightningEffect(world, source, target, false);
		}
		lastPos = pos;
	}
}
