package com.github.Redux.iceandfire.block;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.entity.tile.TileEntityDreadSpawner;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
/** Bloque Dread Spawner */


public class BlockDreadSpawner extends BlockMobSpawner implements IDragonProof {

    public BlockDreadSpawner() {
        super();
        this.setHardness(9.0F);
        this.setResistance(10000F);
        this.setTranslationKey("iceandfire.dread_spawner");
        this.setSoundType(SoundType.METAL);
        this.setRegistryName(IceAndFire.MODID, "dread_spawner");
        GameRegistry.registerTileEntity(TileEntityDreadSpawner.class, "dread_spawner");
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDreadSpawner();
    }

}
