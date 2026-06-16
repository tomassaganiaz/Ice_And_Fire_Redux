package com.github.Redux.iceandfire.client.particle;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;
import java.util.List;

/** ParticleTargetedDragonBreath — Particle Targeted Dragon Breath */

@SideOnly(Side.CLIENT)
public abstract class ParticleTargetedDragonBreath extends ParticleFlame {

	protected final float dragonSize;
	protected final Vector3d initial;
	protected final Vector3d target;
	protected final float speedBonus;
	@Nullable
	protected final EntityDragonBase dragon;
	protected int touchedTime = 0;

	public ParticleTargetedDragonBreath(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, EntityDragonBase entityDragonBase) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0D, 0D, 0D);
		this.particleMaxAge = 30;
		this.dragon = entityDragonBase;
		this.initial = new Vector3d(xCoordIn, yCoordIn, zCoordIn);
		this.target = new Vector3d(
				dragon.burnParticleX + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F,
				dragon.burnParticleY + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F,
				dragon.burnParticleZ + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F
		);
		this.speedBonus = rand.nextFloat() * 0.015F;
		this.dragonSize = MathHelper.clamp(entityDragonBase.getRenderSize() * 0.08F, 0.55F, 3F);
		this.setPosition(xCoordIn, yCoordIn, zCoordIn);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (dragon == null) {
			float distX = (float) (this.initial.x - this.posX);
			float distZ = (float) (this.initial.z - this.posZ);
			this.motionX += distX * -0.01F * dragonSize * rand.nextFloat();
			this.motionZ += distZ * -0.01F * dragonSize * rand.nextFloat();
			this.motionY += 0.015F * rand.nextFloat();
		} else {
			double d2 = this.target.x - initial.x;
			double d3 = this.target.y - initial.y;
			double d4 = this.target.z - initial.z;
			float speed = 0.015F + speedBonus;
			this.motionX += d2 * speed;
			this.motionY += d3 * speed;
			this.motionZ += d4 * speed;
			if (touchedTime > 3) {
				this.setExpired();
			}
		}
	}

	@Override
	public void move(double x, double y, double z) {
		double d0 = y;
		double origX = x;
		double origZ = z;

		if (this.canCollide) {
			List<AxisAlignedBB> list = this.world.getCollisionBoxes(null, this.getBoundingBox().expand(x, y, z));

			for (AxisAlignedBB axisalignedbb : list) {
				y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
			}

			this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

			for (AxisAlignedBB axisalignedbb1 : list) {
				x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
			}

			this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

			for (AxisAlignedBB axisalignedbb2 : list) {
				z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
			}

			this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
			if (!list.isEmpty()) {
				touchedTime++;
			}
		} else {
			this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		}

		this.resetPositionToBB();
		this.onGround = d0 != y && d0 < 0.0D;

		if (origX != x) {
			this.motionX = 0.0D;
		}

		if (origZ != z) {
			this.motionZ = 0.0D;
		}
	}

	protected float distance(Vector3d vector) {
		double d0 = this.posX - vector.x;
		double d1 = this.posY - vector.y;
		double d2 = this.posZ - vector.z;
		return (float) Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	}

	@Override
	public int getBrightnessForRender(float partialTick) {
		return 240;
	}
}