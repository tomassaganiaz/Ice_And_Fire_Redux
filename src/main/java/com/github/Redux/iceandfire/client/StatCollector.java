package com.github.Redux.iceandfire.client;

import net.minecraft.client.resources.I18n;

/**
 * Helper de traducciones legacy — envuelve I18n.format()
 */
public class StatCollector {

	public static String translateToLocal(String s) {
		return I18n.format(s);
	}
}