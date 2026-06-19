package com.github.Redux.iceandfire.structures;

import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.block.BlockCoinPile;
import com.github.Redux.iceandfire.block.IafBlockRegistry;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;
/** WorldGenDragonCave — World Gen Dragon Cave */


public abstract class WorldGenDragonCave extends WorldGenerator {

    protected boolean isMale;

    public void setGoldPile(World world, Random rand, BlockPos pos) {
        int chance = rand.nextInt(100);
        if (world.getBlockState(pos).getBlock() instanceof BlockChest) {
            return;
        }

        if (chance < 60) {
            boolean generateGold = IceAndFireConfig.WORLDGEN.dragonDenGoldAmount <= 1 || rand.nextInt(IceAndFireConfig.WORLDGEN.dragonDenGoldAmount) == 0;
            world.setBlockState(pos, generateGold ? getPile().withProperty(BlockCoinPile.LAYERS, 1 + rand.nextInt(7)) : Blocks.AIR.getDefaultState(), 2);
        } else if (chance == 60) {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[rand.nextInt(3)]), 2);
            if (world.getBlockState(pos).getBlock() instanceof BlockChest) {
                TileEntity chest = world.getTileEntity(pos);
                if (chest instanceof TileEntityChest && !(chest).isInvalid()) {
                    ((TileEntityChest) chest).setLootTable(getLootTable(), rand.nextLong());
                }
            }
        }
    }

    public void setOres(World world, Random rand, BlockPos pos) {
        float hardness = world.getBlockState(pos).getBlock().getBlockHardness(world.getBlockState(pos), world, pos);
        if(hardness == -1.0F || world.isAirBlock(pos)){
            return;
        }
        boolean isOre = rand.nextInt(IceAndFireConfig.WORLDGEN.oreToStoneRatioForDragonCaves + 1) == 0;
        if (isOre) {
            int chance = world.rand.nextInt(200);
            if (chance < 30) {
                world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState(), 2);
            } else if (chance < 40) {
                world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState(), 2);
            } else if (chance < 45) {
                world.setBlockState(pos, IceAndFireConfig.WORLDGEN.generateCopperOre ? IafBlockRegistry.copperOre.getDefaultState() : getStone(), 2);
            } else if (chance < 50) {
                world.setBlockState(pos, IceAndFireConfig.WORLDGEN.generateSilverOre ? IafBlockRegistry.silverOre.getDefaultState() : getStone(), 2);
            } else if (chance < 60) {
                world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState(), 2);
            } else if (chance < 70) {
                world.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState(), 2);
            } else if (chance < 80) {
                world.setBlockState(pos, Blocks.LAPIS_ORE.getDefaultState(), 2);
            } else if (chance < 90) {
                world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState(), 2);
            } else {
                world.setBlockState(pos, getGemstone(), 2);
            }
        } else {
            int chance = rand.nextInt(2);
            if (chance == 0) {
                world.setBlockState(pos, getStone(), 2);
            } else {
                world.setBlockState(pos, getCobblestone(), 2);
            }
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        isMale = rand.nextBoolean();
        int dragonAge = 75 + rand.nextInt(50);
        int i1 = dragonAge / 4;
        int i2 = i1 - 2;
        int ySize = rand.nextInt(2);
        for (int i = 0; i < 3; i++) {
            int j = i1 + rand.nextInt(2);
            int k = i1 / 2 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest) && worldIn.getBlockState(position).getBlock().getBlockHardness(worldIn.getBlockState(position), worldIn, position) >= 0) {
                        worldIn.setBlockState(blockpos, Blocks.STONE.getDefaultState(), 2);
                    }
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = i2 / 2 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest)) {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = (i2 + rand.nextInt(2));
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                float hardness = worldIn.getBlockState(position).getBlock().getBlockHardness(worldIn.getBlockState(position), worldIn, position);
                if (blockpos.distanceSq(position) <= (double) (f * f) && hardness >= 0) {
                    this.setOres(worldIn, rand, blockpos);
                }
            }
        }
        generateNestRim(worldIn, rand, position, i2, ySize);
        generateDragonBones(worldIn, rand, position, i2, ySize);
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = (i2 + rand.nextInt(2)) / 2;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && worldIn.getBlockState(blockpos.down()).getMaterial() == Material.ROCK && worldIn.getBlockState(blockpos).getMaterial() != Material.ROCK) {
                    this.setGoldPile(worldIn, rand, blockpos);
                }
            }
        }
        EntityDragonBase dragon = createDragon(worldIn);
        dragon.setGender(isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(rand.nextInt(4));
        dragon.setPositionAndRotation(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        dragon.setSleeping(true);
        dragon.setHunger(50);
        worldIn.spawnEntity(dragon);
        return true;
    }

    private void generateNestRim(World world, Random rand, BlockPos center, int innerRadius, int ySize) {
        IBlockState rimBlock = getCobblestone();
        int r = innerRadius + 1;
        int wallHeight = 1 + rand.nextInt(2);
        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                double distSq = x * x + z * z;
                double outerSq = r * r;
                double innerSq = (r - 2) * (r - 2);
                if (distSq >= innerSq && distSq <= outerSq) {
                    for (int y = center.getY(); y <= center.getY() + 1; y++) {
                        BlockPos pos = new BlockPos(center.getX() + x, y, center.getZ() + z);
                        if (world.getBlockState(pos).getMaterial() == Material.ROCK && rand.nextFloat() < 0.6f) {
                            for (int h = 0; h <= wallHeight; h++) {
                                BlockPos wallPos = pos.up(h);
                                if (world.getBlockState(wallPos).getMaterial() != Material.ROCK) {
                                    world.setBlockState(wallPos, rimBlock, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
        int platformRadius = innerRadius / 3;
        for (int x = -platformRadius; x <= platformRadius; x++) {
            for (int z = -platformRadius; z <= platformRadius; z++) {
                if (x * x + z * z <= platformRadius * platformRadius) {
                    BlockPos floorPos = new BlockPos(center.getX() + x, center.getY(), center.getZ() + z);
                    if (world.getBlockState(floorPos.down()).getMaterial() == Material.ROCK) {
                        world.setBlockState(floorPos, getStone(), 2);
                    }
                }
            }
        }
    }

    private void generateDragonBones(World world, Random rand, BlockPos center, int radius, int ySize) {
        IBlockState boneBlock = Blocks.BONE_BLOCK.getDefaultState();
        int count = 3 + rand.nextInt(8);
        for (int i = 0; i < count; i++) {
            int angle = rand.nextInt(360);
            double rad = Math.toRadians(angle);
            int boneRadius = radius - 1 - rand.nextInt(3);
            int bx = center.getX() + (int)(boneRadius * Math.cos(rad));
            int bz = center.getZ() + (int)(boneRadius * Math.sin(rad));
            int by = center.getY() - rand.nextInt(radius / 2);
            BlockPos bonePos = new BlockPos(bx, by, bz);
            float hardness = world.getBlockState(bonePos).getBlock().getBlockHardness(world.getBlockState(bonePos), world, bonePos);
            if (hardness >= 0 && hardness < 50 && world.getBlockState(bonePos).getMaterial() == Material.ROCK) {
                world.setBlockState(bonePos, boneBlock, 2);
                if (rand.nextBoolean()) {
                    BlockPos up = bonePos.up();
                    if (world.getBlockState(up).getMaterial() == Material.ROCK) {
                        world.setBlockState(up, boneBlock, 2);
                    }
                }
            }
        }
    }

    protected abstract IBlockState getStone();
    protected abstract IBlockState getCobblestone();
    protected abstract IBlockState getPile();
    protected abstract IBlockState getGemstone();
    protected abstract ResourceLocation getLootTable();
    protected abstract EntityDragonBase createDragon(World worldIn);
}
