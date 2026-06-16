package com.github.Redux.iceandfire.entity.projectile;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.world.World;

/** Carga de fuego — wrapper fino que delega toda la lógica a EntityDragonChargeProjectile con EnumDragonType.FIRE */
public class EntityDragonFireCharge extends EntityDragonChargeProjectile {

	public EntityDragonFireCharge(World worldIn) {
		super(worldIn, EnumDragonType.FIRE);
	}

	public EntityDragonFireCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ, EnumDragonType.FIRE);
	}

	public EntityDragonFireCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ, EnumDragonType.FIRE);
	}
}
