package com.github.Redux.iceandfire;

import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.capability.CapabilityHandler;
import com.github.Redux.iceandfire.capability.entityeffect.EntityEffectCapability;
import com.github.Redux.iceandfire.capability.entityeffect.EntityEffectStorage;
import com.github.Redux.iceandfire.entity.IafEntityRegistry;
import com.github.Redux.iceandfire.entity.IafVillagerRegistry;
import com.github.Redux.iceandfire.event.DragonBlockHandler;
import com.github.Redux.iceandfire.event.DragonBloodHandler;
import com.github.Redux.iceandfire.event.DragonEntityHandler;
import com.github.Redux.iceandfire.event.DragonRidingHandler;
import com.github.Redux.iceandfire.event.EventLiving;
import com.github.Redux.iceandfire.event.EventWorld;
import com.github.Redux.iceandfire.event.StructureGenerator;
import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.integration.RLCombatCompat;
import com.github.Redux.iceandfire.integration.crafttweaker.CraftTweakerCompatBridge;
import com.github.Redux.iceandfire.integration.firstaid.FirstAidCompat;
import com.github.Redux.iceandfire.integration.thaumcraft.ThaumcraftCompatBridge;
import com.github.Redux.iceandfire.integration.theoneprobe.TheOneProbeCompatBridge;
import com.github.Redux.iceandfire.loot.CustomizeToDragon;
import com.github.Redux.iceandfire.loot.CustomizeToSeaSerpent;
import com.github.Redux.iceandfire.message.*;
import com.github.Redux.iceandfire.misc.CreativeTab;
import com.github.Redux.iceandfire.world.village.ComponentAnimalFarm;
import com.github.Redux.iceandfire.world.village.MapGenSnowVillage;
import com.github.Redux.iceandfire.world.village.VillageAnimalFarmCreator;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/** IceAndFire — Ice And Fire */

@Mod(modid = IceAndFire.MODID, dependencies = "required-after:llibrary@[" + IceAndFire.LLIBRARY_VERSION + ",)", version = IceAndFire.VERSION, name = IceAndFire.NAME)
public class IceAndFire {

    public static final String MODID = "iceandfire";
    public static final String VERSION = "2.2.8";
    public static final String LLIBRARY_VERSION = "1.7.9";
    public static final String NAME = "Ice And Fire";
    public static final Logger logger = LogManager.getLogger(NAME);
    @Instance(value = MODID)
    public static IceAndFire INSTANCE;
    @NetworkWrapper({
            MessageDragonArmor.class, MessageDragonControl.class, MessageHippogryphArmor.class,
            MessageUpdatePixieHouse.class, MessageUpdatePodium.class, MessageUpdatePixieHouseModel.class,
            MessageUpdatePixieJar.class, MessageSirenSong.class, MessageDeathWormHitbox.class,
            MessageMultipartInteract.class, MessageGetMyrmexHive.class, MessageSetMyrmexHiveNull.class,
            MessagePlayerHitMultipart.class, MessageChainLightningFX.class, MessageEntityEffect.class,
            MessageResetEntityEffect.class, MessageParticleFX.class, MessageParticleVanillaFX.class,
            MessageSwingGhostSword.class, MessageUpdateSpawner.class, MessageDragonSyncFire.class
    })
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    @SidedProxy(clientSide = "com.github.Redux.iceandfire.ClientProxy", serverSide = "com.github.Redux.iceandfire.CommonProxy")
    public static CommonProxy PROXY;
    public static CreativeTabs TAB_ITEMS;
    public static CreativeTabs TAB_BLOCKS;
    public static DamageSource acid;
    public static DamageSource dragon;
    public static DamageSource dragonFire;
    public static DamageSource dragonIce;
    public static DamageSource dragonLightning;
    public static DamageSource gorgon;
    public static Biome GLACIER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(IEntityEffectCapability.class, new EntityEffectStorage(), EntityEffectCapability::new);
        MinecraftForge.EVENT_BUS.register(new DragonBloodHandler());
        MinecraftForge.EVENT_BUS.register(new DragonRidingHandler());
        MinecraftForge.EVENT_BUS.register(new DragonEntityHandler());
        MinecraftForge.EVENT_BUS.register(new DragonBlockHandler());
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(new EventWorld());
        if(CompatLoadUtil.isRLCombatLoaded()) MinecraftForge.EVENT_BUS.register(RLCombatCompat.class);
        if(CompatLoadUtil.isFirstAidLoaded()) MinecraftForge.EVENT_BUS.register(FirstAidCompat.class);
        TAB_ITEMS = new CreativeTab(MODID + "_items");
        TAB_BLOCKS = new CreativeTab(MODID + "_blocks");
        MinecraftForge.EVENT_BUS.register(PROXY);
        logger.info("A raven flies from the north to the sea");
        logger.info("A dragon whispers her name in the east");
        ThaumcraftCompatBridge.loadThaumcraftCompat();
        TheOneProbeCompatBridge.loadTheOneProbeCompat();
        CraftTweakerCompatBridge.loadCraftTweakerCompat();
        LootFunctionManager.registerFunction(new CustomizeToDragon.Serializer());
        LootFunctionManager.registerFunction(new CustomizeToSeaSerpent.Serializer());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        IafEntityRegistry.init();
        IafVillagerRegistry.INSTANCE.init();
        logger.info("The watcher waits on the northern wall");
        logger.info("A daughter picks up a warrior's sword");
        MapGenStructureIO.registerStructure(MapGenSnowVillage.Start.class, "SnowVillageStart");
        MapGenStructureIO.registerStructureComponent(ComponentAnimalFarm.class, "AnimalFarm");
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageAnimalFarmCreator());
        PROXY.render();
        GameRegistry.registerWorldGenerator(new StructureGenerator(), 0);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new com.github.Redux.iceandfire.client.GuiHandler());
        dragon = createDamageSource("dragon");
        dragonFire = createDamageSource("dragon_fire");
        if (!IceAndFireConfig.DRAGON_SETTINGS.breathDamageBypassImmunities) {
            dragonFire.setFireDamage();
        }
        dragonIce = createDamageSource("dragon_ice");
        dragonLightning = createDamageSource("dragon_lightning");
        acid = createDamageSource("acid").setDamageBypassesArmor();
        gorgon = createDamageSource("gorgon").setDamageBypassesArmor();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postRender();

        logger.info("A brother bound to a love he must hide");
        logger.info("The younger's armor is worn in the mind");

        logger.info("A cold iron throne holds a boy barely grown");
        logger.info("And now it is known");
        logger.info("A claim to the prize, a crown laced in lies");
        logger.info("You win or you die");
    }

    private static DamageSource createDamageSource(String name) {
        return new DamageSource(name) {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entity) {
                String s = "death.attack." + name;
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entity.getDisplayName().getFormattedText() + " ")
                        .appendSibling(new TextComponentTranslation(s1, entity.getDisplayName()));
            }
        };
    }
}
