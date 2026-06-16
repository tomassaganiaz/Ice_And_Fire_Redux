package com.github.Redux.iceandfire.block;

import com.github.Redux.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
/** Bloque Dragon Bone Wall */


public class BlockDragonBoneWall extends BlockWall implements IDragonProof {

    public BlockDragonBoneWall(String gameName, String name, Block baseBlock) {
        super(baseBlock);
        this.setHardness(20F);
        this.setResistance(500F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey(name);
        this.setRegistryName(IceAndFire.MODID, gameName);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }

}
