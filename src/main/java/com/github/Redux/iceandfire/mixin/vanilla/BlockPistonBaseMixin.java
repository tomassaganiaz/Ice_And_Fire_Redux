package com.github.Redux.iceandfire.mixin.vanilla;

import com.github.Redux.iceandfire.block.IDreadBlock;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Bloque Piston Base Mixin */

@Mixin(BlockPistonBase.class)
public abstract class BlockPistonBaseMixin {

    @Inject(
            method = "canPush",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void overrideCanPush(IBlockState blockStateIn, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing p_185646_5_, CallbackInfoReturnable<Boolean> cir) {
        if (blockStateIn.getBlock() instanceof IDreadBlock) {
            cir.setReturnValue(false);
        }
    }
}