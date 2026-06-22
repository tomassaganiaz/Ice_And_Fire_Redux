package com.github.Redux.iceandfire.block;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDragonforgeCore extends BlockContainer implements IDragonProof {
    private static boolean keepInventory;

    final EnumDragonType type;
    final boolean activated;

    public BlockDragonforgeCore(EnumDragonType type, boolean activated) {
        super(Material.IRON);
        this.setHardness(40F);
        this.setResistance(500F);
        this.setSoundType(SoundType.METAL);
        this.type = type;
        this.activated = activated;
        if (!activated) {
            this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        }
        this.setTranslationKey("iceandfire.dragonforge_" + type.getName() + "_core" + (activated ? "" : "_disabled"));
        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + type.getName() + "_core" + (activated ? "" : "_disabled"));
        if (activated) {
            this.setLightLevel(1.0F);
        }
    }

    public static void setState(EnumDragonType type, boolean active, World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (type == EnumDragonType.FIRE) {
            worldIn.setBlockState(pos, (active ? IafBlockRegistry.dragonforge_fire_core : IafBlockRegistry.dragonforge_fire_core_disabled).getDefaultState(), 3);
            worldIn.setBlockState(pos, (active ? IafBlockRegistry.dragonforge_fire_core : IafBlockRegistry.dragonforge_fire_core_disabled).getDefaultState(), 3);
        } else if (type == EnumDragonType.ICE) {
            worldIn.setBlockState(pos, (active ? IafBlockRegistry.dragonforge_ice_core : IafBlockRegistry.dragonforge_ice_core_disabled).getDefaultState(), 3);
            worldIn.setBlockState(pos, (active ? IafBlockRegistry.dragonforge_ice_core : IafBlockRegistry.dragonforge_ice_core_disabled).getDefaultState(), 3);
        } else if (type == EnumDragonType.LIGHTNING) {
            worldIn.setBlockState(pos, (active ? IafBlockRegistry.dragonforge_lightning_core : IafBlockRegistry.dragonforge_lightning_core_disabled).getDefaultState(), 3);
            worldIn.setBlockState(pos, (active ? IafBlockRegistry.dragonforge_lightning_core : IafBlockRegistry.dragonforge_lightning_core_disabled).getDefaultState(), 3);
        }

        keepInventory = false;

        if (te != null) {
            te.validate();
            worldIn.setTileEntity(pos, te);
        }
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) return false;
        playerIn.openGui(IceAndFire.INSTANCE, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(getDisabledVariant());
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(getDisabledVariant());
    }

    private Block getDisabledVariant() {
        if (type == EnumDragonType.FIRE) return IafBlockRegistry.dragonforge_fire_core_disabled;
        if (type == EnumDragonType.ICE) return IafBlockRegistry.dragonforge_ice_core_disabled;
        if (type == EnumDragonType.LIGHTNING) return IafBlockRegistry.dragonforge_lightning_core_disabled;
        return IafBlockRegistry.dragonforge_fire_core_disabled;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return this.type == EnumDragonType.FIRE;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforge(type);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityDragonforge) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDragonforge) tileEntity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }
}
