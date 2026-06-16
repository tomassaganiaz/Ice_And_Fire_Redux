package com.github.Redux.iceandfire.entity.projectile;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.world.World;

/** Breath de hielo — wrapper fino que delega toda la lógica a EntityDragonBreathProjectile con EnumDragonType.ICE */
public class EntityDragonIce extends EntityDragonBreathProjectile {

	public EntityDragonIce(World worldIn) {
		super(worldIn, EnumDragonType.ICE);
	}

	public EntityDragonIce(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ, EnumDragonType.ICE);
	}

	public EntityDragonIce(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ, EnumDragonType.ICE);
	}
}
