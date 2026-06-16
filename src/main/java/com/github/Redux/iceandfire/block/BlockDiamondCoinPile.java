package com.github.Redux.iceandfire.block;

import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.integration.VariedCommoditiesCompat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;
/** Bloque Diamond Coin Pile */


public class BlockDiamondCoinPile extends BlockCoinPile {

    public BlockDiamondCoinPile(String name) {
        super(name, Items.AIR);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (CompatLoadUtil.isVariedCommoditiesLoaded()) {
            return VariedCommoditiesCompat.getDiamondCoin();
        }
        return Items.AIR;
    }
}