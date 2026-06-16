package com.github.Redux.iceandfire;

import com.github.Redux.iceandfire.block.*;
import com.github.Redux.iceandfire.client.gui.GuiMyrmexAddRoom;
import com.github.Redux.iceandfire.client.gui.GuiMyrmexStaff;
import com.github.Redux.iceandfire.client.gui.bestiary.GuiBestiary;
import com.github.Redux.iceandfire.client.model.*;
import com.github.Redux.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import com.github.Redux.iceandfire.client.particle.*;
import com.github.Redux.iceandfire.client.particle.lightning.ParticleLightningRenderer;
import com.github.Redux.iceandfire.client.particle.lightning.ParticleLightningVector;
import com.github.Redux.iceandfire.client.render.entity.EntityRenderRegistry;
import com.github.Redux.iceandfire.client.render.entity.*;
import com.github.Redux.iceandfire.client.render.entity.player.RenderModArmor;
import com.github.Redux.iceandfire.client.render.entity.player.RenderModCapes;
import com.github.Redux.iceandfire.client.render.tile.*;
import com.github.Redux.iceandfire.block.IafBlockRegistry;
import com.github.Redux.iceandfire.integration.baubles.BaublesCompatBridge;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import com.github.Redux.iceandfire.core.ModKeys;
import com.github.Redux.iceandfire.entity.*;
import com.github.Redux.iceandfire.entity.projectile.*;
import com.github.Redux.iceandfire.entity.tile.*;
import com.github.Redux.iceandfire.client.ClientStateManager;
import com.github.Redux.iceandfire.entity.util.MyrmexHive;
import com.github.Redux.iceandfire.enums.*;
import com.github.Redux.iceandfire.event.EventClient;
import com.github.Redux.iceandfire.event.EventNewMenu;
import com.github.Redux.iceandfire.item.ICustomRendered;
import com.github.Redux.iceandfire.mixin.vanilla.IEntityGuardianAccessor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

/** ClientProxy — Client Proxy */

@Mod.EventBusSubscriber(modid = IceAndFire.MODID)
public class ClientProxy extends CommonProxy {

	private static final Object[] ARMOR_MODELS = {
		new ModelCopperArmor(0.5F),
		new ModelCopperArmor(0.2F),
		new ModelSilverArmor(0.5F),
		new ModelSilverArmor(0.2F),
		new ModelFireDragonArmor(0.5F, false),
		new ModelFireDragonArmor(0.2F, true),
		new ModelIceDragonArmor(0.5F, false),
		new ModelIceDragonArmor(0.2F, true),
		new ModelLightningDragonArmor(0.5F, false),
		new ModelLightningDragonArmor(0.2F, true),
		new ModelBloodedFireDragonArmor(0.5F, false),
		new ModelBloodedFireDragonArmor(0.2F, true),
		new ModelBloodedIceDragonArmor(0.5F, false),
		new ModelBloodedIceDragonArmor(0.2F, true),
		new ModelBloodedLightningDragonArmor(0.5F, false),
		new ModelBloodedLightningDragonArmor(0.2F, true),
		new ModelBloodedFireDragonArmor(0.5F, false),
		new ModelBloodedFireDragonArmor(0.2F, true),
		new ModelBloodedIceDragonArmor(0.5F, false),
		new ModelBloodedIceDragonArmor(0.2F, true),
		new ModelBloodedLightningDragonArmor(0.5F, false),
		new ModelBloodedLightningDragonArmor(0.2F, true),
		new ModelDeathWormArmor(0.5F),
		new ModelDeathWormArmor(0.2F),
		new ModelTrollArmor(0.75F),
		new ModelTrollArmor(0.35F),
		new ModelSeaSerpentArmor(0.4F),
		new ModelSeaSerpentArmor(0.2F),
	};
	private FontRenderer bestiaryFontRenderer;
	@SideOnly(Side.CLIENT)
	private static final IceAndFireTEISR TEISR = new IceAndFireTEISR();

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		ModelBakery.registerItemVariants(Item.getItemFromBlock(IafBlockRegistry.podium), new ResourceLocation("iceandfire:podium_oak"), new ResourceLocation("iceandfire:podium_spruce"), new ResourceLocation("iceandfire:podium_birch"), new ResourceLocation("iceandfire:podium_jungle"), new ResourceLocation("iceandfire:podium_acacia"), new ResourceLocation("iceandfire:podium_dark_oak"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.podium), 0, new ModelResourceLocation("iceandfire:podium_oak", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.podium), 1, new ModelResourceLocation("iceandfire:podium_spruce", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.podium), 2, new ModelResourceLocation("iceandfire:podium_birch", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.podium), 3, new ModelResourceLocation("iceandfire:podium_jungle", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.podium), 4, new ModelResourceLocation("iceandfire:podium_acacia", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.podium), 5, new ModelResourceLocation("iceandfire:podium_dark_oak", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragonbone_bow, new ResourceLocation("iceandfire:dragonbone_bow"), new ResourceLocation("iceandfire:dragonbone_bow_pulling_0"), new ResourceLocation("iceandfire:dragonbone_bow_pulling_1"), new ResourceLocation("iceandfire:dragonbone_bow_pulling_2"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragonbone_bow, 0, new ModelResourceLocation("iceandfire:dragonbone_bow", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragonbone_bow_fire, new ResourceLocation("iceandfire:dragonbone_bow_fire"), new ResourceLocation("iceandfire:dragonbone_bow_fire_pulling_0"), new ResourceLocation("iceandfire:dragonbone_bow_fire_pulling_1"), new ResourceLocation("iceandfire:dragonbone_bow_fire_pulling_2"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragonbone_bow_fire, 0, new ModelResourceLocation("iceandfire:dragonbone_bow_fire", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragonbone_bow_ice, new ResourceLocation("iceandfire:dragonbone_bow_ice"), new ResourceLocation("iceandfire:dragonbone_bow_ice_pulling_0"), new ResourceLocation("iceandfire:dragonbone_bow_ice_pulling_1"), new ResourceLocation("iceandfire:dragonbone_bow_ice_pulling_2"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragonbone_bow_ice, 0, new ModelResourceLocation("iceandfire:dragonbone_bow_ice", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragonbone_bow_lightning, new ResourceLocation("iceandfire:dragonbone_bow_lightning"), new ResourceLocation("iceandfire:dragonbone_bow_lightning_pulling_0"), new ResourceLocation("iceandfire:dragonbone_bow_lightning_pulling_1"), new ResourceLocation("iceandfire:dragonbone_bow_lightning_pulling_0"), new ResourceLocation("iceandfire:dragonbone_bow_lightning_pulling_2"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragonbone_bow_lightning, 0, new ModelResourceLocation("iceandfire:dragonbone_bow_lightning", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_skull, new ResourceLocation("iceandfire:dragon_skull_fire"), new ResourceLocation("iceandfire:dragon_skull_ice"), new ResourceLocation("iceandfire:dragon_skull_lightning"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_skull, 0, new ModelResourceLocation("iceandfire:dragon_skull_fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_skull, 1, new ModelResourceLocation("iceandfire:dragon_skull_ice", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_skull, 2, new ModelResourceLocation("iceandfire:dragon_skull_lightning", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_iron, new ResourceLocation("iceandfire:dragonarmor_iron_head"), new ResourceLocation("iceandfire:dragonarmor_iron_neck"), new ResourceLocation("iceandfire:dragonarmor_iron_body"), new ResourceLocation("iceandfire:dragonarmor_iron_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_iron, 0, new ModelResourceLocation("iceandfire:dragonarmor_iron_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_iron, 1, new ModelResourceLocation("iceandfire:dragonarmor_iron_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_iron, 2, new ModelResourceLocation("iceandfire:dragonarmor_iron_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_iron, 3, new ModelResourceLocation("iceandfire:dragonarmor_iron_tail", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_gold, new ResourceLocation("iceandfire:dragonarmor_gold_head"), new ResourceLocation("iceandfire:dragonarmor_gold_neck"), new ResourceLocation("iceandfire:dragonarmor_gold_body"), new ResourceLocation("iceandfire:dragonarmor_gold_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_gold, 0, new ModelResourceLocation("iceandfire:dragonarmor_gold_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_gold, 1, new ModelResourceLocation("iceandfire:dragonarmor_gold_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_gold, 2, new ModelResourceLocation("iceandfire:dragonarmor_gold_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_gold, 3, new ModelResourceLocation("iceandfire:dragonarmor_gold_tail", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_diamond, new ResourceLocation("iceandfire:dragonarmor_diamond_head"), new ResourceLocation("iceandfire:dragonarmor_diamond_neck"), new ResourceLocation("iceandfire:dragonarmor_diamond_body"), new ResourceLocation("iceandfire:dragonarmor_diamond_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_diamond, 0, new ModelResourceLocation("iceandfire:dragonarmor_diamond_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_diamond, 1, new ModelResourceLocation("iceandfire:dragonarmor_diamond_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_diamond, 2, new ModelResourceLocation("iceandfire:dragonarmor_diamond_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_diamond, 3, new ModelResourceLocation("iceandfire:dragonarmor_diamond_tail", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_silver, new ResourceLocation("iceandfire:dragonarmor_silver_head"), new ResourceLocation("iceandfire:dragonarmor_silver_neck"), new ResourceLocation("iceandfire:dragonarmor_silver_body"), new ResourceLocation("iceandfire:dragonarmor_silver_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_silver, 0, new ModelResourceLocation("iceandfire:dragonarmor_silver_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_silver, 1, new ModelResourceLocation("iceandfire:dragonarmor_silver_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_silver, 2, new ModelResourceLocation("iceandfire:dragonarmor_silver_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_silver, 3, new ModelResourceLocation("iceandfire:dragonarmor_silver_tail", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_copper, new ResourceLocation("iceandfire:dragonarmor_copper_head"), new ResourceLocation("iceandfire:dragonarmor_copper_neck"), new ResourceLocation("iceandfire:dragonarmor_copper_body"), new ResourceLocation("iceandfire:dragonarmor_copper_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_copper, 0, new ModelResourceLocation("iceandfire:dragonarmor_copper_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_copper, 1, new ModelResourceLocation("iceandfire:dragonarmor_copper_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_copper, 2, new ModelResourceLocation("iceandfire:dragonarmor_copper_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_copper, 3, new ModelResourceLocation("iceandfire:dragonarmor_copper_tail", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_fire_dragonsteel, new ResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_head"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_neck"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_body"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_fire_dragonsteel, 0, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_fire_dragonsteel, 1, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_fire_dragonsteel, 2, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_fire_dragonsteel, 3, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_fire_tail", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_ice_dragonsteel, new ResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_head"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_neck"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_body"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_ice_dragonsteel, 0, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_ice_dragonsteel, 1, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_ice_dragonsteel, 2, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_ice_dragonsteel, 3, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_ice_tail", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.dragon_armor_lightning_dragonsteel, new ResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_head"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_neck"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_body"), new ResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_tail"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_lightning_dragonsteel, 0, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_lightning_dragonsteel, 1, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_lightning_dragonsteel, 2, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.dragon_armor_lightning_dragonsteel, 3, new ModelResourceLocation("iceandfire:dragonarmor_dragonsteel_lightning_tail", "inventory"));
		for(int i = 0; i < EnumHippogryphTypes.values().length; i++){
			ModelLoader.setCustomModelResourceLocation(IafItemRegistry.hippogryph_egg, i, new ModelResourceLocation("iceandfire:hippogryph_egg", "inventory"));
		}
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.gorgon_head, 0, new ModelResourceLocation("iceandfire:gorgon_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.gorgon_head, 1, new ModelResourceLocation("iceandfire:gorgon_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.pixieHouse), 0, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.pixieHouse), 1, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.pixieHouse), 2, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.pixieHouse), 3, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.pixieHouse), 4, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.pixieHouse), 5, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(IafBlockRegistry.jar_pixie), new ResourceLocation("iceandfire:jar_0"), new ResourceLocation("iceandfire:jar_1"), new ResourceLocation("iceandfire:jar_2"), new ResourceLocation("iceandfire:jar_3"), new ResourceLocation("iceandfire:jar_4"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.jar_empty), 0, new ModelResourceLocation("iceandfire:jar", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.jar_pixie), 0, new ModelResourceLocation("iceandfire:jar_0", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.jar_pixie), 1, new ModelResourceLocation("iceandfire:jar_1", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.jar_pixie), 2, new ModelResourceLocation("iceandfire:jar_2", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.jar_pixie), 3, new ModelResourceLocation("iceandfire:jar_3", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.jar_pixie), 4, new ModelResourceLocation("iceandfire:jar_4", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.deathworm_chitin, new ResourceLocation("iceandfire:deathworm_chitin_yellow"), new ResourceLocation("iceandfire:deathworm_chitin_white"), new ResourceLocation("iceandfire:deathworm_chitin_red"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.deathworm_chitin, 0, new ModelResourceLocation("iceandfire:deathworm_chitin_yellow", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.deathworm_chitin, 1, new ModelResourceLocation("iceandfire:deathworm_chitin_white", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.deathworm_chitin, 2, new ModelResourceLocation("iceandfire:deathworm_chitin_red", "inventory"));
		ModelBakery.registerItemVariants(IafItemRegistry.deathworm_egg, new ResourceLocation("iceandfire:deathworm_egg"), new ResourceLocation("iceandfire:deathworm_egg_giant"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.deathworm_egg, 0, new ModelResourceLocation("iceandfire:deathworm_egg", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.deathworm_egg, 1, new ModelResourceLocation("iceandfire:deathworm_egg_giant", "inventory"));
		for (EnumDragonArmor armor : EnumDragonArmor.values()) {
			renderDragonArmors(armor);
		}
		for (EnumBloodedDragonArmor armor : EnumBloodedDragonArmor.values()) {
			renderBloodedDragonArmors(armor);
		}
		for (EnumSeaSerpent armor : EnumSeaSerpent.values()) {
			renderSeaSerpentArmors(armor);
		}
		for (EnumDragonEgg egg : EnumDragonEgg.values()) {
			renderDragonEggs(egg);
		}
		for(EnumTroll.Weapon weapon : EnumTroll.Weapon.values()){
			ModelLoader.setCustomModelResourceLocation(weapon.item, 0, new ModelResourceLocation("iceandfire:troll_weapon", "inventory"));
		}
		for (EnumTroll troll : EnumTroll.values()) {
			ModelLoader.setCustomModelResourceLocation(troll.helmet, 0, new ModelResourceLocation("iceandfire:"  + troll.name().toLowerCase() + "_troll_leather_helmet", "inventory"));
			ModelLoader.setCustomModelResourceLocation(troll.chestplate, 0, new ModelResourceLocation("iceandfire:"  + troll.name().toLowerCase() + "_troll_leather_chestplate", "inventory"));
			ModelLoader.setCustomModelResourceLocation(troll.leggings, 0, new ModelResourceLocation("iceandfire:"  + troll.name().toLowerCase() + "_troll_leather_leggings", "inventory"));
			ModelLoader.setCustomModelResourceLocation(troll.boots, 0, new ModelResourceLocation("iceandfire:"  + troll.name().toLowerCase() + "_troll_leather_boots", "inventory"));
		}
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.silver_helmet, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.silver_chestplate, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.silver_leggings, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(IafItemRegistry.silver_boots, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_boots", "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(IafBlockRegistry.myrmex_resin), new ResourceLocation("iceandfire:desert_myrmex_resin"), new ResourceLocation("iceandfire:jungle_myrmex_resin"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(IafBlockRegistry.myrmex_resin_sticky), new ResourceLocation("iceandfire:desert_myrmex_resin_sticky"), new ResourceLocation("iceandfire:jungle_myrmex_resin_sticky"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.myrmex_resin), 0, new ModelResourceLocation("iceandfire:desert_myrmex_resin", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.myrmex_resin), 1, new ModelResourceLocation("iceandfire:jungle_myrmex_resin", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.myrmex_resin_sticky), 0, new ModelResourceLocation("iceandfire:desert_myrmex_resin_sticky", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(IafBlockRegistry.myrmex_resin_sticky), 1, new ModelResourceLocation("iceandfire:jungle_myrmex_resin_sticky", "inventory"));
		for(int i = 0; i < 5; i++){
			ModelLoader.setCustomModelResourceLocation(IafItemRegistry.myrmex_desert_egg, i, new ModelResourceLocation("iceandfire:myrmex_desert_egg", "inventory"));
			ModelLoader.setCustomModelResourceLocation(IafItemRegistry.myrmex_jungle_egg, i, new ModelResourceLocation("iceandfire:myrmex_jungle_egg", "inventory"));
		}
		for (EnumSkullType skull : EnumSkullType.values()) {
			ModelLoader.setCustomModelResourceLocation(skull.skull_item, 0, new ModelResourceLocation("iceandfire:" + skull.itemResourceName, "inventory"));
		}
		registerItemModelsViaReflection();
		registerBlockModelsViaReflection();
		registerStateMappers();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderWorldLast(RenderWorldLastEvent event) {
		ParticleLightningRenderer.onRenderWorldLast(event);
	}

	@SideOnly(Side.CLIENT)
	public static void renderDragonArmors(EnumDragonArmor armor) {
		ModelLoader.setCustomModelResourceLocation(armor.helmet, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.chestplate, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.leggings, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.boots, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_boots", "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void renderBloodedDragonArmors(EnumBloodedDragonArmor armor) {
		ModelLoader.setCustomModelResourceLocation(armor.helmet, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_helmet_blooded", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.chestplate, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_chestplate_blooded", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.leggings, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_leggings_blooded", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.boots, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_boots_blooded", "inventory"));
	}

	@SideOnly(Side.CLIENT)
	public static void renderSeaSerpentArmors(EnumSeaSerpent armor) {
		ModelLoader.setCustomModelResourceLocation(armor.scale, 0, new ModelResourceLocation("iceandfire:sea_serpent_scales_" + armor.resourceName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.pile.getItemBlock(), 0, new ModelResourceLocation("iceandfire:sea_serpent_scales_" + armor.resourceName + "_pile", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.helmet, 0, new ModelResourceLocation("iceandfire:tide_" + armor.resourceName + "_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.chestplate, 0, new ModelResourceLocation("iceandfire:tide_" + armor.resourceName + "_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.leggings, 0, new ModelResourceLocation("iceandfire:tide_" + armor.resourceName + "_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.boots, 0, new ModelResourceLocation("iceandfire:tide_" + armor.resourceName + "_boots", "inventory"));
	}

	@SideOnly(Side.CLIENT)
	public static void renderDragonEggs(EnumDragonEgg egg) {
		ModelLoader.setCustomModelResourceLocation(egg.egg, 0, new ModelResourceLocation("iceandfire:dragonegg_" + egg.resourceName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(egg.scales, 0, new ModelResourceLocation("iceandfire:dragonscales_" + egg.resourceName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(egg.pile.getItemBlock(), 0, new ModelResourceLocation("iceandfire:dragonscales_" + egg.resourceName + "_pile", "inventory"));
	}

	@SideOnly(Side.CLIENT)
	private static void registerStateMappers() {
		ModelLoader.setCustomStateMapper(IafBlockRegistry.charedDirt, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.charedGrass, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.charedStone, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.charedCobblestone, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.charedGravel, (new StateMap.Builder()).ignore(BlockFallingReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.charedGrassPath, (new StateMap.Builder()).ignore(BlockPath.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.frozenDirt, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.frozenGrass, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.frozenStone, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.frozenCobblestone, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.frozenGravel, (new StateMap.Builder()).ignore(BlockFallingReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.frozenGrassPath, (new StateMap.Builder()).ignore(BlockPath.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.crackledDirt, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.crackledGrass, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.crackledStone, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.crackledCobblestone, (new StateMap.Builder()).ignore(BlockReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.crackledGravel, (new StateMap.Builder()).ignore(BlockFallingReturningState.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.crackledGrassPath, (new StateMap.Builder()).ignore(BlockPath.REVERTS).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_bricks, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_bricks_chiseled, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_bricks_cracked, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_bricks_mossy, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_tile, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_face, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_bricks_slab, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dread_stone_bricks_double_slab, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dreadwood_planks, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
		ModelLoader.setCustomStateMapper(IafBlockRegistry.dreadwood_planks_lock, (new StateMap.Builder()).ignore(IDreadBlock.PLAYER_PLACED).build());
	}

	@SideOnly(Side.CLIENT)
	private static void registerBlockModelsViaReflection() {
		try {
			for (Field f : IafBlockRegistry.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Block && !(obj instanceof ICustomRendered)) {
					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block)obj), 0, new ModelResourceLocation("iceandfire:" + ((Block)obj).getRegistryName().getPath(), "inventory"));
				} else if (obj instanceof Block[]) {
					for (Block block : (Block[]) obj) {
						if(!(block instanceof ICustomRendered)){
							ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation("iceandfire:" + block.getRegistryName().getPath(), "inventory"));
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SideOnly(Side.CLIENT)
	private static void registerItemModelsViaReflection() {
		try {
			for (Field f : IafItemRegistry.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Item && !(obj instanceof ICustomRendered)) {
					ModelLoader.setCustomModelResourceLocation((Item)obj, 0, new ModelResourceLocation("iceandfire:" + ((Item)obj).getRegistryName().getPath(), "inventory"));
				} else if (obj instanceof Item[]) {
					for (Item item : (Item[]) obj) {
						if (!(item instanceof ICustomRendered)) {
							ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("iceandfire:" + item.getRegistryName().getPath(), "inventory"));
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings("deprecation")
	public void render() {
		this.bestiaryFontRenderer = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("iceandfire:textures/font/bestiary.png"), Minecraft.getMinecraft().renderEngine, false);
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this.bestiaryFontRenderer);
		ModKeys.init();
		MinecraftForge.EVENT_BUS.register(new RenderModArmor());
		MinecraftForge.EVENT_BUS.register(new RenderModCapes());
		MinecraftForge.EVENT_BUS.register(new EventClient());
		MinecraftForge.EVENT_BUS.register(new EventNewMenu());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHead.class, new RenderGorgonHead(false));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHeadActive.class, new RenderGorgonHead(true));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDreadSpawner.class, new RenderMobSpawner());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMonsterSpawner.class, new RenderMobSpawner());
		ForgeHooksClient.registerTESRItemStack(IafItemRegistry.gorgon_head, 0, TileEntityDummyGorgonHead.class);
		ForgeHooksClient.registerTESRItemStack(IafItemRegistry.gorgon_head, 1, TileEntityDummyGorgonHeadActive.class);
		EntityRenderRegistry.registerAll();
		BaublesCompatBridge.loadBaublesClientModels();
	}

	@SideOnly(Side.CLIENT)
	public void postRender() {
		EventClient.initializeStoneLayer();
		for(EnumTroll.Weapon weapon : EnumTroll.Weapon.values()) {
			weapon.item.setTileEntityItemStackRenderer(TEISR);
		}
	}

	@SideOnly(Side.CLIENT)
	public void spawnLightningEffect(World world, ParticleLightningVector sourceVec, ParticleLightningVector targetVec, boolean isProjectile) {
		Particle particle = new ParticleLightning(world, sourceVec, targetVec, isProjectile);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	@SideOnly(Side.CLIENT)
	public void spawnLightningEffect(World world, ParticleLightningVector sourceVec, ParticleLightningVector targetVec, int colorOuter, int colorInner, boolean isProjectile) {
		Particle particle = new ParticleLightning(world, sourceVec, targetVec, colorOuter, colorInner, isProjectile);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	@SideOnly(Side.CLIENT)
	public void spawnDragonParticle(EntityDragonBase dragon) {
		Vec3d headPos;
		Particle particle;
		switch (dragon.dragonType) {
			case FIRE:
				headPos = dragon.getHeadPosition();
				particle = new ParticleTargetedDragonFlame(dragon.world, headPos.x, headPos.y, headPos.z, dragon);
				break;
			case ICE:
				headPos = dragon.getHeadPosition();
				particle = new ParticleTargetedDragonFrost(dragon.world, headPos.x, headPos.y, headPos.z, dragon);
				break;
			default:
				particle = null;
		}
		if (particle != null) {
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

	@SideOnly(Side.CLIENT)
	public void spawnParticle(EnumParticle type, World world, double x, double y, double z, double motX, double motY, double motZ) {
		spawnParticle(type, world, x, y, z, motX, motY, motZ, 1);
	}
	@SideOnly(Side.CLIENT)
	public void spawnParticle(EnumParticle type, World world, double x, double y, double z, double motX, double motY, double motZ, float size) {
		Particle particle;
		switch (type) {
			case DRAGON_FIRE:
				particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ);
				break;
			case DRAGON_ICE:
				particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ);
				break;
			case FLAME:
				particle = new ParticleFlame.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
				break;
			case LAVA:
				particle = new ParticleLava.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
				break;
			case SNOWFLAKE:
				particle = new ParticleSnowflake(world, x, y, z, motX, motY, motZ);
				break;
			case SPARK:
				particle = new ParticleSpark(world, x, y, z, motX, motY, motZ);
				break;
			case SMOKE_NORMAL:
				particle = new ParticleSmokeNormal.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
				break;
			case SMOKE_LARGE:
				particle = new ParticleSmokeLarge.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
				break;
			case HYDRA_BREATH:
				particle = new ParticleHydraBreath(world, x, y, z, (float) motX,(float) motY, (float) motZ);
				break;
			case BLOOD:
				particle = new ParticleBlood(world, x, y, z);
				break;
			case PIXIE_DUST:
				particle = new ParticlePixieDust(world, x, y, z, (float) motX, (float) motY, (float) motZ);
				break;
			case SIREN_APPEARANCE:
				particle = new ParticleSirenAppearance(world, x, y, z);
				break;
			case SIREN_MUSIC:
				particle = new ParticleSirenMusic(world, x, y, z, motX, motY, motZ);
				break;
			case SERPENT_BUBBLE:
				particle = new ParticleSerpentBubble(world, x, y, z, motX, motY, motZ);
				break;
			case EXPLOSION:
				particle = new ParticleExplosion.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
				break;
			case CLOUD:
				particle = new ParticleCloud.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
				break;
			case DREAD_TORCH:
				particle = new ParticleDreadTorch(world, x, y, z, motX, motY, motZ, size);
				break;
			case GHOST_APPEARANCE:
				particle = new ParticleGhostAppearance(world, x, y, z);
				break;
			case REDSTONE:
				particle = new ParticleRedstone.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
				break;
			default:
				particle = new ParticleSmokeNormal.Factory().createParticle(0, world, x, y, z, motX, motY, motZ);
		}
		if (particle != null) {
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void openBestiaryGui(ItemStack book) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiBestiary(book));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void openMyrmexStaffGui(ItemStack staff) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiMyrmexStaff(staff));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, EnumFacing facing) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiMyrmexAddRoom(staff, pos, facing));
	}


	@SideOnly(Side.CLIENT)
	public Object getArmorModel(int armorId) {
		return armorId >= 0 && armorId < ARMOR_MODELS.length ? ARMOR_MODELS[armorId] : null;
	}

	public Object getFontRenderer() {
		return this.bestiaryFontRenderer;
	}

	public int getDragon3rdPersonView() {
		return ClientStateManager.getDragon3rdPersonView();
	}

	public void setDragon3rdPersonView(int view) {
		ClientStateManager.setDragon3rdPersonView(view);
	}

	public void updateDragonArmorRender(String clear){
		RenderDragonBase.clearCache(clear);
	}

	public static MyrmexHive getReferedClientHive(){
		return ClientStateManager.getReferedClientHive();
	}

	public static void setReferedClientHive(MyrmexHive hive){
		ClientStateManager.setReferedClientHive(hive);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderLivingEventPre(RenderLivingEvent.Pre<EntityLivingBase> event) {
		EntityLivingBase entity = event.getEntity();
		if(entity == null) return;
		if(((IEntityLivingBaseRenderContext)entity).iceAndFire$getStoned()) {
			//Reset invis right before render again to fix other entities/mods changing values themselves (lycanite)
			entity.setInvisible(!(entity instanceof EntityStoneStatue));
			entity.swingProgress = 0;
			entity.limbSwing = 0;
			if(entity instanceof EntityLiving) {
				((EntityLiving)entity).livingSoundTime = 0;
			}
			if(entity instanceof EntityHorse) {
				EntityHorse horse = (EntityHorse)entity;
				horse.tailCounter = 0;
			}
			if(entity instanceof EntityGuardian) {
				EntityGuardian guardian = (EntityGuardian)entity;
				((IEntityGuardianAccessor)guardian).iceAndFire$setClientSideTailAnimation(0);
				((IEntityGuardianAccessor)guardian).iceAndFire$setClientSideTailAnimation0(0);
				((IEntityGuardianAccessor)guardian).iceAndFire$setClientSideSpikesAnimation(1);
				((IEntityGuardianAccessor)guardian).iceAndFire$setClientSideSpikesAnimation0(1);
			}
		}
	}
}