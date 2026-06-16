package com.github.Redux.iceandfire.integration.theoneprobe;

import mcjty.theoneprobe.TheOneProbe;
/** TheOneProbeCompat — The One Probe Compat */


public class TheOneProbeCompat {

    public static void register() {
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DragonInfoProvider());
    }
}