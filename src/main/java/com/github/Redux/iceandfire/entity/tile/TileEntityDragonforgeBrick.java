package com.github.Redux.iceandfire.entity.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityDragonforgeBrick extends TileEntity implements ITickable {

    public int grillTimer = 0;

    @Override
    public void update() {
        if (grillTimer > 0) grillTimer--;
    }
}
