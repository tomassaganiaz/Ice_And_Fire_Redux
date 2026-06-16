package com.github.Redux.iceandfire.block;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.client.StatCollector;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
/** Bloque Sea Serpent Scales */


public class BlockSeaSerpentScales extends Block {
    TextFormatting color;
    String name;

    public BlockSeaSerpentScales(String name, TextFormatting color) {
        super(Material.ROCK);
        this.color = color;
        this.name = name;
        this.setTranslationKey("iceandfire.sea_serpent_scale_block");
        this.setRegistryName(IceAndFire.MODID, "sea_serpent_scale_block_" + name);
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(30F);
        this.setResistance(500F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(color + StatCollector.translateToLocal("sea_serpent." + name));
    }
}
