package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.entity.EntityGhost;
import com.github.Redux.iceandfire.entity.EntitySiren;
import net.minecraft.world.World;
/** IEffectEntityLookup — I Effect Entity Lookup */


public interface IEffectEntityLookup {
    EntitySiren getSiren(World world);
    EntityGhost getGhost(World world);
}
