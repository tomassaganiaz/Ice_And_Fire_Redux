package com.github.Redux.iceandfire.item;

import com.github.Redux.iceandfire.entity.projectile.EntityDragonArrow;
/** Ítem Dragon Arrow */


public class ItemDragonArrow extends ItemGeneric {
    private final EntityDragonArrow.Type type;

    public ItemDragonArrow(String gameName, String name, EntityDragonArrow.Type type) {
        super(gameName, name);
        this.type = type;
    }

    public EntityDragonArrow.Type getType() {
        return this.type;
    }
}
