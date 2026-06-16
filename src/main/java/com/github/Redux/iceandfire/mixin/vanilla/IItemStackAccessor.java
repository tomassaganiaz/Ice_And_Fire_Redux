package com.github.Redux.iceandfire.mixin.vanilla;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** IItemStackAccessor — I Item Stack Accessor */

@Mixin(ItemStack.class)
public interface IItemStackAccessor {

	@Accessor(value = "capabilities", remap = false)
	CapabilityDispatcher iceAndFire$getCapabilities();
}