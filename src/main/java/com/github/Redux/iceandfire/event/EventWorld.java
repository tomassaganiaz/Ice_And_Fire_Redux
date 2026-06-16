package com.github.Redux.iceandfire.event;

import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.world.DragonPosWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/** EventWorld — Event World */


public class EventWorld {

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        Chunk chunk = event.getChunk();
        World world = event.getWorld();
        if (chunk == null || world == null) {
            return;
        }

        for (ClassInheritanceMultiMap<Entity> entityList : chunk.getEntityLists())
        {
            for (Entity entity : entityList) {
                if (entity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) entity;
                    if (dragon.isBoundToCrystal() && !dragon.isDead) {
                        DragonPosWorldData.get(world).addDragon(dragon.getUniqueID(), dragon.getPosition());
                    }
                }
            }
        }
    }
}
