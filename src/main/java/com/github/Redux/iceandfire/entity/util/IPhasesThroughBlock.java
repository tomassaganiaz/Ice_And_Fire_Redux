package com.github.Redux.iceandfire.entity.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/** IPhasesThroughBlock — I Phases Through Block */


public interface IPhasesThroughBlock {

    boolean canPhaseThroughBlock(IBlockState state, World world, BlockPos pos);

}
