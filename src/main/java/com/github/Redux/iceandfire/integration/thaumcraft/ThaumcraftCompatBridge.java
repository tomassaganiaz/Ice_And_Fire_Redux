package com.github.Redux.iceandfire.integration.thaumcraft;

import com.github.Redux.iceandfire.integration.CompatLoadUtil;

/**
 * Created by Joseph on 6/23/2018.
 */
public class ThaumcraftCompatBridge {

	public static void loadThaumcraftCompat() {
		if (CompatLoadUtil.isThaumcraftLoaded()) {
			ThaumcraftCompat.register();
		}
	}
}