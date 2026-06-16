package com.github.Redux.iceandfire.client.render.entity.layer;

import com.github.Redux.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonTextures;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** LayerDragonArmor — Layer Dragon Armor */

@SideOnly(Side.CLIENT)
public class LayerDragonArmor implements LayerRenderer<EntityDragonBase> {

	private final RenderLiving<EntityDragonBase> render;
	public static final Map<String, ResourceLocation> LAYERED_ARMOR_CACHE = Maps.newHashMap();

	public LayerDragonArmor(RenderLiving<EntityDragonBase> renderIn) {
		this.render = renderIn;
	}

	@Override
	public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		int armorHead = dragon.getArmorInSlot(EntityEquipmentSlot.HEAD);
		int armorNeck = dragon.getArmorInSlot(EntityEquipmentSlot.CHEST);
		int armorLegs = dragon.getArmorInSlot(EntityEquipmentSlot.LEGS);
		int armorFeet = dragon.getArmorInSlot(EntityEquipmentSlot.FEET);
		if (armorHead != 0 || armorNeck != 0 || armorLegs != 0 || armorFeet != 0) {
			String armorTexture = dragon.dragonType.getName() + "|" + armorHead + "|" + armorNeck + "|" + armorLegs + "|" + armorFeet;
			ResourceLocation resourcelocation = LAYERED_ARMOR_CACHE.get(armorTexture);
			if(resourcelocation == null){
				resourcelocation = new ResourceLocation("iceandfire" + "dragonArmor_" + armorTexture);
				List<String> tex = new ArrayList<>();
				for (EntityEquipmentSlot slot : EntityDragonBase.ARMOR_SLOTS) {
					if (dragon.dragonType == EnumDragonType.ICE) {
						tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).ICETEXTURE.toString());
					} else if (dragon.dragonType == EnumDragonType.LIGHTNING) {
						tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).LIGHTNINGTEXTURE.toString());
					} else {
						tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).FIRETEXTURE.toString());
					}
				}
				ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
				Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, layeredBase);
				LAYERED_ARMOR_CACHE.put(armorTexture, resourcelocation);
			}
			this.render.bindTexture(resourcelocation);
			this.render.getMainModel().render(dragon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
