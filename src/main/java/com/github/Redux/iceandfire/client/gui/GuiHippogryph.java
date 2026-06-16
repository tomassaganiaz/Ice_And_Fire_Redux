package com.github.Redux.iceandfire.client.gui;

import com.github.Redux.iceandfire.entity.EntityHippogryph;
import com.github.Redux.iceandfire.inventory.ContainerHippogryph;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Interfaz gráfica de Hippogryph */

@SideOnly(Side.CLIENT)
public class GuiHippogryph extends GuiHippogryphBase<EntityHippogryph> {

	public GuiHippogryph(IInventory playerInv, EntityHippogryph hippogryph) {
		super(new ContainerHippogryph(hippogryph, Minecraft.getMinecraft().player), playerInv, hippogryph.hippogryphInventory, hippogryph);
	}

	@Override
	protected boolean isEntityChested() {
		return entity.isChested();
	}
}