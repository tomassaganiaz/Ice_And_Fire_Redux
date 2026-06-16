package com.github.Redux.iceandfire.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** ContainerHippogryphBase — Container Hippogryph Base */


public abstract class ContainerHippogryphBase extends Container {
    private final IInventory entityInventory;
    private final EntityLivingBase entity;
    private final EntityPlayer player;

    public ContainerHippogryphBase(EntityLivingBase entity, IInventory entityInventory, EntityPlayer player) {
        this.entityInventory = entityInventory;
        this.entity = entity;
        this.player = player;
        entityInventory.openInventory(player);
        this.addSlotToContainer(new Slot(entityInventory, 0, 8, 18) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.getHasStack();
            }

            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlotToContainer(new Slot(entityInventory, 1, 8, 36) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !this.getHasStack();
            }

            public void onSlotChanged() {
                ContainerHippogryphBase.this.onChestSlotChanged();
            }

            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlotToContainer(new Slot(entityInventory, 2, 8, 52) {
            public boolean isItemValid(ItemStack stack) {
                return ContainerHippogryphBase.this.isValidArmor(stack);
            }

            public int getSlotStackLimit() {
                return 1;
            }

            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 5; ++l) {
                this.addSlotToContainer(new Slot(entityInventory, 3 + l + k * 5, 80 + l * 18, 18 + k * 18) {
                    @SideOnly(Side.CLIENT)
                    public boolean isEnabled() {
                        return ContainerHippogryphBase.this.isChested();
                    }

                    public boolean isItemValid(ItemStack stack) {
                        return ContainerHippogryphBase.this.isChested();
                    }
                });
            }
        }
        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlotToContainer(new Slot(player.inventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }
        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlotToContainer(new Slot(player.inventory, j1, 8 + j1 * 18, 142));
        }
    }

    protected abstract boolean isValidArmor(ItemStack stack);

    protected abstract boolean isChested();

    protected abstract void onChestSlotChanged();

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.entityInventory.isUsableByPlayer(playerIn) && this.entity.isEntityAlive() && this.entity.getDistance(playerIn) < 8.0F;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.entityInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.entityInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(2).isItemValid(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).isItemValid(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.entityInventory.getSizeInventory() <= 3 || !this.mergeItemStack(itemstack1, 3, this.entityInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.entityInventory.closeInventory(playerIn);
    }
}
