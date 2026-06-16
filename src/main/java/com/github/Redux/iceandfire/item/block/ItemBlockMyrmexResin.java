package com.github.Redux.iceandfire.item.block;

import com.github.Redux.iceandfire.block.IafBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
/** Ítem Myrmex Resin */


public class ItemBlockMyrmexResin extends ItemBlock {
    private boolean sticky;
    public ItemBlockMyrmexResin(Block block) {
        super(block);
        sticky = block == IafBlockRegistry.myrmex_resin_sticky;
        this.setHasSubtypes(true);
    }

    @Override
    public String getTranslationKey(ItemStack itemstack) {
        if(sticky) {
            return itemstack.getItemDamage() == 1 ? "tile.iceandfire.jungle_myrmex_resin_sticky" : "tile.iceandfire.desert_myrmex_resin_sticky";
        }else{
            return itemstack.getItemDamage() == 1 ? "tile.iceandfire.jungle_myrmex_resin" : "tile.iceandfire.desert_myrmex_resin";
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
