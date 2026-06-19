package com.github.Redux.iceandfire.structures;

import com.github.Redux.iceandfire.block.IafBlockRegistry;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.entity.EntityLightningDragon;
import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
/** WorldGenLightningDragonRoost — World Gen Lightning Dragon Roost */


public class WorldGenLightningDragonRoost extends WorldGenDragonRoost {

    public static final ResourceLocation LIGHTNING_TIER1 = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_cave_t1"));
    public static final ResourceLocation LIGHTNING_TIER3 = LootTableList.register(new ResourceLocation("iceandfire", "lightning_dragon_cave_t3"));

    protected void transformState(World world, BlockPos blockpos, IBlockState state) {
        if (state.getMaterial() == Material.GRASS && state.getBlock() == Blocks.GRASS) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledGrass.getDefaultState(), 2);
        } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledDirt.getDefaultState(), 2);
        } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledGravel.getDefaultState(), 2);
        } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledCobblestone.getDefaultState(), 2);
        } else if (state.getMaterial() == Material.ROCK && state.getBlock() != IafBlockRegistry.crackledCobblestone) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledStone.getDefaultState(), 2);
        } else if (state.getBlock() == Blocks.GRASS_PATH) {
            world.setBlockState(blockpos, IafBlockRegistry.crackledGrassPath.getDefaultState(), 2);
        } else if (state.getMaterial() == Material.WOOD) {
            world.setBlockState(blockpos, IafBlockRegistry.ash.getDefaultState(), 2);
        } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS) {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
        } else if (state.getMaterial() == Material.SAND) {
            world.setBlockState(blockpos, IafBlockRegistry.fulgurite.getDefaultState(), 2);
        }
    }

    protected IBlockState getPileBlock() {
        if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
            return IafBlockRegistry.diamondPile.getDefaultState();
        }
        return IafBlockRegistry.copperPile.getDefaultState();
    }

    protected IBlockState getBuildingBlock() {
        return IafBlockRegistry.crackledCobblestone.getDefaultState();
    }

    protected Block[] getDragonTransformedBlocks() {
        return new Block[] {
                IafBlockRegistry.crackledGrass,
                IafBlockRegistry.crackledDirt,
                IafBlockRegistry.crackledGravel,
                IafBlockRegistry.crackledGrassPath,
                IafBlockRegistry.crackledStone,
                IafBlockRegistry.crackledCobblestone,
                IafBlockRegistry.fulgurite
        };
    }

    protected ResourceLocation getLootTable(int dragonAge, boolean male) {
        if (dragonAge < 50) return LIGHTNING_TIER1;
        if (dragonAge >= 80) return LIGHTNING_TIER3;
        return male ? WorldGenLightningDragonCave.LIGHTNINGDRAGON_MALE_CHEST : WorldGenLightningDragonCave.LIGHTNINGDRAGON_CHEST;
    }

    protected EntityDragonBase createDragon(World worldIn) {
        return new EntityLightningDragon(worldIn);
    }
}
