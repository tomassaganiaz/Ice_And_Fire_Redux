package com.github.Redux.iceandfire.item;

import com.github.Redux.iceandfire.IceAndFire;
import net.minecraft.item.Item;
/** Ítem Dragon Bone */


public class ItemDragonBone extends Item {

	public ItemDragonBone() {
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey("iceandfire.dragonbone");
		this.maxStackSize = 8;
		this.setRegistryName(IceAndFire.MODID, "dragonbone");
	}
}
