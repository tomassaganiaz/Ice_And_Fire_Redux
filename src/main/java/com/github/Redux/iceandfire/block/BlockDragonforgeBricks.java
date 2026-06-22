package com.github.Redux.iceandfire.block;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.Redux.iceandfire.entity.tile.TileEntityDragonforgeBrick;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDragonforgeBricks extends BlockContainer implements IDragonProof {

    public static final PropertyBool GRILL = PropertyBool.create("grill");
    private final EnumDragonType dragonType;

    public BlockDragonforgeBricks(EnumDragonType dragonType) {
        super(Material.ROCK);
        this.dragonType = dragonType;
        this.setHardness(40F);
        this.setResistance(500F);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(GRILL, false));
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dragonforge_" + dragonType.getName() + "_brick");
        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + dragonType.getName() + "_brick");
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityDragonforge forge = getConnectedForge(worldIn, pos);
        if (forge != null && forge.isType(dragonType)) {
            playerIn.openGui(IceAndFire.INSTANCE, 7, worldIn, forge.getPos().getX(), forge.getPos().getY(), forge.getPos().getZ());
            return true;
        }
        return false;
    }

    @Nullable
    private TileEntityDragonforge getConnectedForge(World worldIn, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity te = worldIn.getTileEntity(pos.offset(facing));
            if (te instanceof TileEntityDragonforge) {
                TileEntityDragonforge forge = (TileEntityDragonforge) te;
                if (forge.assembled()) return forge;
            }
        }
        return null;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(GRILL, meta > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(GRILL) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, GRILL);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforgeBrick();
    }
}
