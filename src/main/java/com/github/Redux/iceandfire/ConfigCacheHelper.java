package com.github.Redux.iceandfire;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.github.Redux.iceandfire.util.IafMathHelper;

import java.util.*;
import java.util.function.Supplier;
/** ConfigCacheHelper — Config Cache Helper */


public class ConfigCacheHelper {

    private static LazyCache<HashSet<ResourceLocation>> stoneBlacklist = new LazyCache<>();
    private static LazyCache<HashSet<ResourceLocation>> chainLightningBlacklist = new LazyCache<>();
    private static LazyCache<HashSet<ResourceLocation>> dreadTargetingBlacklist = new LazyCache<>();
    private static LazyCache<HashSet<String>> myrmexDisabledNames = new LazyCache<>();
    private static LazyCache<HashMap<String, Integer>> trollSpawnCheckHeight = new LazyCache<>();
    private static LazyCache<HashMap<String, String>> trollSpawnCheckType = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> myrmexDisabledTypes = new LazyCache<>();
    private static LazyCache<HashSet<String>> dragonDisabledNames = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> dragonDisabledTypes = new LazyCache<>();
    private static LazyCache<HashSet<String>> fireDragonEnabledNames = new LazyCache<>();
    private static LazyCache<HashSet<String>> iceDragonEnabledNames = new LazyCache<>();
    private static LazyCache<HashSet<String>> lightningDragonEnabledNames = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> fireDragonEnabledTypes = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> iceDragonEnabledTypes = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> lightningDragonEnabledTypes = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> fireDragonDisabledTypes = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> iceDragonDisabledTypes = new LazyCache<>();
    private static LazyCache<HashSet<BiomeDictionary.Type>> lightningDragonDisabledTypes = new LazyCache<>();
    private static LazyCache<HashMap<String, Integer>> dragonRoostChance = new LazyCache<>();
    private static LazyCache<HashMap<String, Integer>> dragonDenChance = new LazyCache<>();
    private static LazyCache<HashMap<Block, Integer>> dragonGriefingBlockChance = new LazyCache<>();
    private static LazyCache<HashMap<Block, Integer>> dragonGriefingEffectChance = new LazyCache<>();

    public static void invalidateAll() {
        stoneBlacklist.invalidate();
        chainLightningBlacklist.invalidate();
        dreadTargetingBlacklist.invalidate();
        myrmexDisabledNames.invalidate();
        myrmexDisabledTypes.invalidate();
        trollSpawnCheckHeight.invalidate();
        trollSpawnCheckType.invalidate();
        dragonDisabledNames.invalidate();
        dragonDisabledTypes.invalidate();
        fireDragonEnabledNames.invalidate();
        iceDragonEnabledNames.invalidate();
        lightningDragonEnabledNames.invalidate();
        fireDragonEnabledTypes.invalidate();
        iceDragonEnabledTypes.invalidate();
        lightningDragonEnabledTypes.invalidate();
        fireDragonDisabledTypes.invalidate();
        iceDragonDisabledTypes.invalidate();
        lightningDragonDisabledTypes.invalidate();
        dragonRoostChance.invalidate();
        dragonDenChance.invalidate();
        dragonGriefingBlockChance.invalidate();
        dragonGriefingEffectChance.invalidate();
    }

    private static HashSet<ResourceLocation> getStoneEntityBlacklist() {
        return stoneBlacklist.get(() -> {
            HashSet<ResourceLocation> set = new HashSet<>();
            for (String s : IceAndFireConfig.ENTITY_SETTINGS.stoneEntityBlacklist) set.add(new ResourceLocation(s));
            return set;
        });
    }

    public static boolean isEntityBlacklistedFromBeingStoned(Entity entity) {
        ResourceLocation id = EntityList.getKey(entity);
        if (id == null) {
            return false;
        }
        HashSet<ResourceLocation> blacklist = getStoneEntityBlacklist();
        if (blacklist.contains(id)) {
            return true;
        }
        ResourceLocation wildcard = new ResourceLocation(id.getNamespace(), "*");
        return blacklist.contains(wildcard);
    }

    public static HashSet<ResourceLocation> getChainLightningEntityBlacklist() {
        return chainLightningBlacklist.get(() -> {
            HashSet<ResourceLocation> set = new HashSet<>();
            for (String s : IceAndFireConfig.MISC_SETTINGS.chainLightningEntityBlacklist) set.add(new ResourceLocation(s));
            return set;
        });
    }

    public static HashSet<ResourceLocation> getDreadTargetingEntityBlacklist() {
        return dreadTargetingBlacklist.get(() -> {
            HashSet<ResourceLocation> set = new HashSet<>();
            for (String s : IceAndFireConfig.ENTITY_SETTINGS.dreadTargetingEntityBlacklist) set.add(new ResourceLocation(s));
            return set;
        });
    }

    public static HashSet<String> getMyrmexDisabledNames() {
        return myrmexDisabledNames.get(() -> new HashSet<>(Arrays.asList(IceAndFireConfig.WORLDGEN.generateMyrmexDisabledBiomeNames)));
    }

    public static HashSet<BiomeDictionary.Type> getMyrmexDisabledTypes() {
        return myrmexDisabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateMyrmexDisabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashMap<String, Integer> getTrollSpawnHeight() {
        return trollSpawnCheckHeight.get(() -> mapNameInteger(IceAndFireConfig.ENTITY_SPAWNING.trollSpawnCheckHeightForBiome));
    }

    public static HashMap<String, String> getTrollSpawnType() {
        return trollSpawnCheckType.get(() -> mapNameString(IceAndFireConfig.ENTITY_SPAWNING.trollSpawnTypeForBiome));
    }

    public static HashSet<String> getDragonDisabledNames() {
        return dragonDisabledNames.get(() -> new HashSet<>(Arrays.asList(IceAndFireConfig.WORLDGEN.generateDragonDisabledBiomeNames)));
    }

    public static HashSet<BiomeDictionary.Type> getDragonDisabledTypes() {
        return dragonDisabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateDragonDisabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashSet<String> getFireDragonEnabledNames() {
        return fireDragonEnabledNames.get(() -> new HashSet<>(Arrays.asList(IceAndFireConfig.WORLDGEN.generateFireDragonEnabledBiomeNames)));
    }

    public static HashSet<String> getIceDragonEnabledNames() {
        return iceDragonEnabledNames.get(() -> new HashSet<>(Arrays.asList(IceAndFireConfig.WORLDGEN.generateIceDragonEnabledBiomeNames)));
    }

    public static HashSet<String> getLightningDragonEnabledNames() {
        return lightningDragonEnabledNames.get(() -> new HashSet<>(Arrays.asList(IceAndFireConfig.WORLDGEN.generateLightningDragonEnabledBiomeNames)));
    }

    public static HashSet<BiomeDictionary.Type> getFireDragonEnabledTypes() {
        return fireDragonEnabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateFireDragonEnabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashSet<BiomeDictionary.Type> getIceDragonEnabledTypes() {
        return iceDragonEnabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateIceDragonEnabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashSet<BiomeDictionary.Type> getLightningDragonEnabledTypes() {
        return lightningDragonEnabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateLightningDragonEnabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashSet<BiomeDictionary.Type> getFireDragonDisabledTypes() {
        return fireDragonDisabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateFireDragonDisabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashSet<BiomeDictionary.Type> getIceDragonDisabledTypes() {
        return iceDragonDisabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateIceDragonDisabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashSet<BiomeDictionary.Type> getLightningDragonDisabledTypes() {
        return lightningDragonDisabledTypes.get(() -> {
            HashSet<BiomeDictionary.Type> set = new HashSet<>();
            for (String s : IceAndFireConfig.WORLDGEN.generateLightningDragonDisabledBiomeTypes) set.add(BiomeDictionary.Type.getType(s));
            return set;
        });
    }

    public static HashMap<String, Integer> getDragonRoostChance() {
        return dragonRoostChance.get(() -> mapNameInteger(IceAndFireConfig.WORLDGEN.generateDragonRoostChanceForBiome));
    }

    public static HashMap<String, Integer> getDragonDenChance() {
        return dragonDenChance.get(() -> mapNameInteger(IceAndFireConfig.WORLDGEN.generateDragonDenChanceForBiome));
    }

    public static HashMap<Block, Integer> getDragonGriefingBlockChance() {
        return dragonGriefingBlockChance.get(() -> loadBlockChanceMapping(IceAndFireConfig.DRAGON_SETTINGS.dragonGriefingBlockChance));
    }

    public static HashMap<Block, Integer> getDragonGriefingEffectChance() {
        return dragonGriefingEffectChance.get(() -> loadBlockChanceMapping(IceAndFireConfig.DRAGON_SETTINGS.dragonGriefingEffectChance));
    }

    private static HashMap<String, Integer> mapNameInteger(String[] mappings) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String mapping : mappings) {
            if (StringUtils.isNullOrEmpty(mapping)) continue;
            String[] split = mapping.split("=");
            if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) {
                IceAndFire.logger.error("Failed to parse biome name mapping: " + mapping);
                continue;
            }
            try {
                map.put(split[0], Integer.parseInt(split[1]));
            } catch (NumberFormatException e) {
                IceAndFire.logger.error("Failed to parse biome name mapping, invalid integer: " + mapping);
            }
        }
        return map;
    }

    private static HashMap<String, String> mapNameString(String[] mappings) {
        HashMap<String, String> map = new HashMap<>();
        for (String mapping : mappings) {
            if (StringUtils.isNullOrEmpty(mapping)) continue;
            String[] split = mapping.split("=");
            if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) {
                IceAndFire.logger.error("Failed to parse biome name mapping: " + mapping);
                continue;
            }
            map.put(split[0], split[1]);
        }
        return map;
    }

    private static HashMap<Block, Integer> loadBlockChanceMapping(String[] mappings) {
        HashMap<Block, Integer> map = new HashMap<>();
        for (String mapping : mappings) {
            if (StringUtils.isNullOrEmpty(mapping)) continue;
            String[] split = mapping.split("=");
            if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) {
                IceAndFire.logger.error("Failed to parse block name mapping: " + mapping);
                continue;
            }
            Block block = Block.getBlockFromName(split[0]);
            if (block == null || block == Blocks.AIR) {
                IceAndFire.logger.error("Failed to parse block name mapping, invalid block or air: " + mapping);
                continue;
            }
            try {
                map.put(block, IafMathHelper.clamp(Integer.parseInt(split[1]), 0, 100));
            } catch (NumberFormatException e) {
                IceAndFire.logger.error("Failed to parse block name mapping, invalid chance: " + mapping);
            }
        }
        return map;
    }

    private static class LazyCache<T> {
        private T value;

        public T get(Supplier<T> factory) {
            if (value == null) {
                value = factory.get();
            }
            return value;
        }

        public void invalidate() {
            value = null;
        }
    }
}
