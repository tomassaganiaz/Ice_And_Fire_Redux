package com.github.Redux.iceandfire.inventory;

import com.github.Redux.iceandfire.entity.EntityHippogryph;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
/** ContainerHippogryph — Container Hippogryph */


public class ContainerHippogryph extends ContainerHippogryphBase {
    private final EntityHippogryph hippogryph;

    public ContainerHippogryph(final EntityHippogryph hippogryph, EntityPlayer player) {
        super(hippogryph, hippogryph.hippogryphInventory, player);
        this.hippogryph = hippogryph;
    }

    protected boolean isValidArmor(ItemStack stack) {
        return hippogryph.getIntFromArmor(stack) != 0;
    }

    protected boolean isChested() {
        return hippogryph.isChested();
    }

    protected void onChestSlotChanged() {
        hippogryph.refreshInventory();
    }
}
