package com.github.Redux.iceandfire.entity.projectile;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.world.World;

/** Breath de fuego — wrapper fino que delega toda la lógica a EntityDragonBreathProjectile con EnumDragonType.FIRE */
public class EntityDragonFire extends EntityDragonBreathProjectile {

	public EntityDragonFire(World worldIn) {
		super(worldIn, EnumDragonType.FIRE);
	}

	public EntityDragonFire(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ, EnumDragonType.FIRE);
	}

	public EntityDragonFire(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ, EnumDragonType.FIRE);
	}
}
