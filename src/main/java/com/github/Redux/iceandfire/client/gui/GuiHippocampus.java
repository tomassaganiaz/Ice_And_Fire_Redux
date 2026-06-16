package com.github.Redux.iceandfire.client.gui;

import com.github.Redux.iceandfire.entity.EntityHippocampus;
import com.github.Redux.iceandfire.inventory.ContainerHippocampus;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Interfaz gráfica de Hippocampus */

@SideOnly(Side.CLIENT)
public class GuiHippocampus extends GuiHippogryphBase<EntityHippocampus> {

	public GuiHippocampus(IInventory playerInv, EntityHippocampus hippocampus) {
		super(new ContainerHippocampus(hippocampus, Minecraft.getMinecraft().player), playerInv, hippocampus.hippocampusInventory, hippocampus);
	}

	@Override
	protected boolean isEntityChested() {
		return entity.isChested();
	}
}