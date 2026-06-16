package com.github.Redux.iceandfire.enums;
/** EnumOrder — Enum Order */


public enum EnumOrder {
	WANDER, SIT, FOLLOW, SLEEP;

	public final EnumOrder next() {
		return EnumOrder.values()[(this.ordinal() + 1) % EnumOrder.values().length];
	}
}
