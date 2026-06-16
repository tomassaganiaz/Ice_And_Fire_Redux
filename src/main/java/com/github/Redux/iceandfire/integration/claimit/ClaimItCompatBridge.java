package com.github.Redux.iceandfire.integration.claimit;

import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/** ClaimItCompatBridge — Claim It Compat Bridge */


public class ClaimItCompatBridge {

	public static boolean isBlockInAnyClaim(World world, BlockPos pos) {
		if (CompatLoadUtil.isClaimItLoaded()) {
			return ClaimItCompat.isBlockInAnyClaim(world, pos);
		}
		return false;
	}
}