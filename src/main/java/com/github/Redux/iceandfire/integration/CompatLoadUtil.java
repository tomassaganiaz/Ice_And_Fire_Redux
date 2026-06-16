package com.github.Redux.iceandfire.integration;

import net.minecraftforge.fml.common.Loader;
/** CompatLoadUtil — Compat Load Util */


public abstract class CompatLoadUtil {

    private static final String BAUBLES_MODID = "baubles";
    private static Boolean baublesLoaded;
    private static final String CLAIMIT_MODID = "claimit";
    private static Boolean claimitLoaded;
    private static final String VARIED_COMMODITIES_MODID = "variedcommodities";
    private static Boolean variedCommoditiesLoaded;
    private static final String LYCANITES_MOBS_MODID = "lycanitesmobs";
    private static Boolean lycanitesMobsLoaded;
    private static final String THAUMCRAFT_MODID = "thaumcraft";
    private static Boolean thaumcraftLoaded;
    private static final String RLCOMBAT_MODID = "bettercombatmod";
    private static Boolean rlcombatLoaded;
    private static final String THE_ONE_PROBE_MODID = "theoneprobe";
    private static Boolean theOneProbeLoaded;
    private static final String FIRST_AID_MODID = "firstaid";
    private static Boolean firstAidLoaded;
    private static final String CHARM_MODID = "charm";
    private static Boolean charmLoaded;
    private static final String SPARTAN_WEAPONRY_MODID = "spartanweaponry";
    private static Boolean spartanWeaponryLoaded;
    private static final String TCONSTRUCT_MODID = "tconstruct";
    private static Boolean tconstructLoaded;
    private static final String CONARM_MODID = "conarm";
    private static Boolean conarmLoaded;

    public static boolean isTConstructLoaded() {
        if(tconstructLoaded == null) tconstructLoaded = Loader.isModLoaded(TCONSTRUCT_MODID);
        return tconstructLoaded;
    }

    public static boolean isConstructsArmoryLoaded() {
        if(conarmLoaded == null) conarmLoaded = Loader.isModLoaded(CONARM_MODID);
        return conarmLoaded;
    }

    public static boolean isBaublesLoaded(){
        if(baublesLoaded == null) baublesLoaded = Loader.isModLoaded(BAUBLES_MODID);
        return baublesLoaded;
    }

    public static boolean isClaimItLoaded() {
        if(claimitLoaded == null) claimitLoaded = Loader.isModLoaded(CLAIMIT_MODID);
        return claimitLoaded;
    }

    public static boolean isVariedCommoditiesLoaded() {
        if(variedCommoditiesLoaded == null) variedCommoditiesLoaded = Loader.isModLoaded(VARIED_COMMODITIES_MODID);
        return variedCommoditiesLoaded;
    }

    public static boolean isLycanitesMobsLoaded() {
        if(lycanitesMobsLoaded == null) lycanitesMobsLoaded = Loader.isModLoaded(LYCANITES_MOBS_MODID);
        return lycanitesMobsLoaded;
    }

    public static boolean isThaumcraftLoaded() {
        if(thaumcraftLoaded == null) thaumcraftLoaded = Loader.isModLoaded(THAUMCRAFT_MODID);
        return thaumcraftLoaded;
    }

    public static boolean isRLCombatLoaded() {
        if(rlcombatLoaded == null) rlcombatLoaded = Loader.isModLoaded(RLCOMBAT_MODID) && isRLCombatCorrectVersion();
        return rlcombatLoaded;
    }

    public static boolean isTheOneProbeLoaded() {
        if(theOneProbeLoaded == null) theOneProbeLoaded = Loader.isModLoaded(THE_ONE_PROBE_MODID);
        return theOneProbeLoaded;
    }

    public static boolean isFirstAidLoaded() {
        if(firstAidLoaded == null) firstAidLoaded = Loader.isModLoaded(FIRST_AID_MODID);
        return firstAidLoaded;
    }

    public static boolean isSpartanWeaponryLoaded() {
        if(spartanWeaponryLoaded == null) spartanWeaponryLoaded = Loader.isModLoaded(SPARTAN_WEAPONRY_MODID);
        return spartanWeaponryLoaded;
    }

    //RLCombat is 2.x.x, BetterCombat is 1.x.x
    private static boolean isRLCombatCorrectVersion() {
        String[] arrOfStr = Loader.instance().getIndexedModList().get(RLCOMBAT_MODID).getVersion().split("\\.");
        try {
            int i = Integer.parseInt(String.valueOf(arrOfStr[0]));
            if(i == 2) return true;
        }
        catch(Exception ignored) { }
        return false;
    }
}