package com.github.Redux.iceandfire.misc;

import com.github.Redux.iceandfire.IceAndFire;
import com.github.Redux.iceandfire.block.IafBlockRegistry;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
/** CreativeTab — Creative Tab */


public class CreativeTab extends CreativeTabs {


	public CreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return this == IceAndFire.TAB_ITEMS ? new ItemStack(IafItemRegistry.dragon_skull) : new ItemStack(IafBlockRegistry.dragon_bone_block);
	}

	@Override
	public boolean hasSearchBar() {
		return false;
	}
}
