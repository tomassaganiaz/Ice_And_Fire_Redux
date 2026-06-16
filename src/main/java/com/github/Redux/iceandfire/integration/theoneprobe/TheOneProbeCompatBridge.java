package com.github.Redux.iceandfire.integration.theoneprobe;

import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.integration.theoneprobe.TheOneProbeCompat;
/** TheOneProbeCompatBridge — The One Probe Compat Bridge */


public class TheOneProbeCompatBridge {

    public static void loadTheOneProbeCompat() {
        if (CompatLoadUtil.isTheOneProbeLoaded()) {
            TheOneProbeCompat.register();
        }
    }
}
