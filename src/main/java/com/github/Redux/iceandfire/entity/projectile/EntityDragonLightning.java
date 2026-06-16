package com.github.Redux.iceandfire.entity.projectile;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.world.World;

/** Breath de relámpago — wrapper fino que delega toda la lógica a EntityDragonBreathProjectile con EnumDragonType.LIGHTNING */
public class EntityDragonLightning extends EntityDragonBreathProjectile {

	public EntityDragonLightning(World worldIn) {
		super(worldIn, EnumDragonType.LIGHTNING);
	}

	public EntityDragonLightning(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ, EnumDragonType.LIGHTNING);
	}

	public EntityDragonLightning(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ, EnumDragonType.LIGHTNING);
	}
}
