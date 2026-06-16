package com.github.Redux.iceandfire.inventory;

import com.github.Redux.iceandfire.entity.EntityHippocampus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
/** ContainerHippocampus — Container Hippocampus */


public class ContainerHippocampus extends ContainerHippogryphBase {
    private final EntityHippocampus hippocampus;

    public ContainerHippocampus(final EntityHippocampus hippocampus, EntityPlayer player) {
        super(hippocampus, hippocampus.hippocampusInventory, player);
        this.hippocampus = hippocampus;
    }

    protected boolean isValidArmor(ItemStack stack) {
        return hippocampus.getIntFromArmor(stack) != 0;
    }

    protected boolean isChested() {
        return hippocampus.isChested();
    }

    protected void onChestSlotChanged() {
        hippocampus.refreshInventory();
    }
}
