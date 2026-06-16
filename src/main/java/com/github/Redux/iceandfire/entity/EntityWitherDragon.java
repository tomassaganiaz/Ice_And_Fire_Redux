package com.github.Redux.iceandfire.entity;

import com.github.Redux.iceandfire.IceAndFireConfig;
import com.github.Redux.iceandfire.entity.ai.*;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonProjectile;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonFire;
import com.github.Redux.iceandfire.entity.projectile.EntityDragonFireCharge;
import com.github.Redux.iceandfire.enums.EnumDragonType;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

/** Dragón de Wither — Nether, aliento de Wither II, enemigo de todo */
public class EntityWitherDragon extends EntityDragonBase {

    public static final float[] growth_stage_1 = new float[]{1F, 3F};
    public static final float[] growth_stage_2 = new float[]{3F, 7F};
    public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
    public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
    public static final float[] growth_stage_5 = new float[]{20F, 30F};

    public EntityWitherDragon(World worldIn) {
        super(worldIn, EnumDragonType.WITHER, 1, 1 + IceAndFireConfig.DRAGON_SETTINGS.dragonAttackDamage, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth * 0.04, IceAndFireConfig.DRAGON_SETTINGS.dragonHealth, 0.15F, 0.4F);
        this.setSize(0.78F, 1.2F);
        this.isImmuneToFire = true;
        this.ignoreFrustumCheck = true;
        this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
        this.stepHeight = 1;
    }

    @Override
    protected void addCustomTasks() {
        this.tasks.addTask(2, new DragonAISwim(this));
    }

    @Override
    protected Item getStewItem() { return IafItemRegistry.fire_stew; }

    public String getVariantName(int variant) {
        switch (variant) {
            default: return "black_";
            case 1: return "dark_";
            case 2: return "obsidian_";
            case 3: return "shadow_";
        }
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    @Override
    protected EntityDragonProjectile createChargeProjectile(World world, double x, double y, double z) {
        return new EntityDragonFireCharge(world, this, x, y, z);
    }

    @Override
    protected EntityDragonProjectile createBreathProjectile(World world, double x, double y, double z) {
        return new EntityDragonFire(world, this, x, y, z);
    }
}
