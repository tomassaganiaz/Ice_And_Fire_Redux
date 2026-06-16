package com.github.Redux.iceandfire.client.render.entity;

import com.github.Redux.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.Redux.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.github.Redux.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Renderizador de Dragon Base */

@SideOnly(Side.CLIENT)
public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

	private final Map<String, ResourceLocation> LAYERED_TEXTURE_CACHE = Maps.newHashMap();

	public RenderDragonBase(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0.8F);
		this.addLayer(new LayerDragonEyes(this));
		this.addLayer(new LayerDragonRider(this));
		this.addLayer(new LayerDragonArmor(this));
	}

	@Override
	public boolean shouldRender(EntityDragonBase dragon, ICamera camera, double camX, double camY, double camZ) {
		return true;
	}

	@Override
	protected void preRenderCallback(EntityDragonBase entity, float f) {
		this.shadowSize = entity.getRenderSize() / 3;
		GlStateManager.scale(shadowSize, shadowSize, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
		String baseTexture = entity.getVariantName(entity.getVariant()) + " " + entity.getDragonStage() + entity.isModelDead() + entity.isSkeletal() + entity.isSleeping() + entity.isBlinking();
		ResourceLocation resourcelocation = LAYERED_TEXTURE_CACHE.get(baseTexture);
		if (resourcelocation == null) {
			resourcelocation = EnumDragonTextures.getTextureFromDragon(entity);
			LAYERED_TEXTURE_CACHE.put(baseTexture, resourcelocation);
		}
		return resourcelocation;
	}

	public static void clearCache(String str){
		LayerDragonArmor.LAYERED_ARMOR_CACHE.remove(str);
	}
}
