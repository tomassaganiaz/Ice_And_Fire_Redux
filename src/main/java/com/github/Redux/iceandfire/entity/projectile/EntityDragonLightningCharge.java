package com.github.Redux.iceandfire.entity.projectile;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.world.World;

/** Carga de relámpago — wrapper fino que delega toda la lógica a EntityDragonChargeProjectile con EnumDragonType.LIGHTNING */
public class EntityDragonLightningCharge extends EntityDragonChargeProjectile {

	public EntityDragonLightningCharge(World worldIn) {
		super(worldIn, EnumDragonType.LIGHTNING);
	}

	public EntityDragonLightningCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ, EnumDragonType.LIGHTNING);
	}

	public EntityDragonLightningCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ, EnumDragonType.LIGHTNING);
	}
}
