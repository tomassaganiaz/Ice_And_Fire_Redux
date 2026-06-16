package com.github.Redux.iceandfire.mixin.vanilla;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** IEntityAgeableAccessor — I Entity Ageable Accessor */

@Mixin(EntityAgeable.class)
public interface IEntityAgeableAccessor {
	
	@Accessor(value = "BABY")
	static DataParameter<Boolean> iceAndFire$getBabyParameter() {
		throw new UnsupportedOperationException("Baby Accessor Failed to Apply");
	}
}