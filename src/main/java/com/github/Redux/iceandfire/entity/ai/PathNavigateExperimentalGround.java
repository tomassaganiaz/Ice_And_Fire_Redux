package com.github.Redux.iceandfire.entity.ai;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.world.World;
/** Navegador de ruta para ExperimentalGround */


public class PathNavigateExperimentalGround extends PathNavigateGround {

    public PathNavigateExperimentalGround(EntityLiving entityDragonBase, World worldIn) {
        super(entityDragonBase, worldIn);
    }

    protected PathFinder getPathFinder(){
        this.nodeProcessor = new ExperimentalWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
}
