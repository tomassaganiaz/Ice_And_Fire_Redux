package com.github.Redux.iceandfire.item;

import com.github.Redux.iceandfire.api.ChainLightningUtils;
import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.api.InFCapabilities;
import com.github.Redux.iceandfire.entity.EntityDeathWorm;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
/** IHitEffect — I Hit Effect */


public interface IHitEffect {

    Item.ToolMaterial getMaterial();

    /**
     * Handle the default weapon effects
     */
    default void doHitEffect(EntityLivingBase target, EntityLivingBase attacker) {
        if (getMaterial() == IafItemRegistry.silverTools) {
            if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                target.attackEntityFrom(DamageSource.MAGIC, 3.0F + getMaterial().getAttackDamage() + 2.0F);
            }
        }
        else if (getMaterial() == IafItemRegistry.myrmexChitin) {
            if (target.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD) {
                target.attackEntityFrom(DamageSource.GENERIC, 3.0F + getMaterial().getAttackDamage() + 4.0F);
            }
            if (target instanceof EntityDeathWorm) {
                target.attackEntityFrom(DamageSource.GENERIC, 3.0F + getMaterial().getAttackDamage() + 4.0F);
            }
        }
        else if (getMaterial() == IafItemRegistry.fireBoneTools) {
            if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToFire()) {
                target.attackEntityFrom(DamageSource.IN_FIRE, 3.0F + getMaterial().getAttackDamage() + 8F);
            }
            target.setFire(5);
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
        else if (getMaterial() == IafItemRegistry.iceBoneTools  || getMaterial() == IafItemRegistry.dread_queen_sword_tools) {
            if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToIce()) {
                target.attackEntityFrom(DamageSource.DROWN, 3.0F + getMaterial().getAttackDamage() + 8F);
            }
            if (!target.world.isRemote) {
                IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(target);
                if (capability != null) capability.setFrozen(200);
            }
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
            target.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100, 2));
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
        else if (getMaterial() == IafItemRegistry.lightningBoneTools) {
            if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToLightning()) {
                target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 3.0F + getMaterial().getAttackDamage() + 4F);
            }
            ChainLightningUtils.createChainLightningFromTarget(target.world, target, attacker);
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
		else if (getMaterial() == IafItemRegistry.fireDragonsteelTools) {
			if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToFire()) {
				target.attackEntityFrom(DamageSource.IN_FIRE, 3.0F + getMaterial().getAttackDamage() + 12F);
			}
			target.setFire(10);
			target.knockBack(target, 1.5F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		else if (getMaterial() == IafItemRegistry.iceDragonsteelTools) {
			if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToIce()) {
				target.attackEntityFrom(DamageSource.DROWN, 3.0F + getMaterial().getAttackDamage() + 12F);
			}
			if (!target.world.isRemote) {
				target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 4));
				target.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 4));
			}
			target.knockBack(target, 1.5F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		else if (getMaterial() == IafItemRegistry.lightningDragonsteelTools) {
			if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToLightning()) {
				target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 3.0F + getMaterial().getAttackDamage() + 8F);
			}
			ChainLightningUtils.createChainLightningFromTarget(target.world, target, attacker);
			target.knockBack(target, 1.5F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		if (this == IafItemRegistry.myrmex_desert_sword_venom || this == IafItemRegistry.myrmex_jungle_sword_venom) {
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 2));
		}
	}

    /**
     * Apply effects and return modifier amount for mod compat
     */
    default float getHitEffectModifier(EntityLivingBase target, EntityLivingBase attacker) {
        float mod = 0.0F;
        if (getMaterial() == IafItemRegistry.silverTools) {
            if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                mod += 2.0F;
            }
        }
        else if (getMaterial() == IafItemRegistry.myrmexChitin) {
            if (target.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD) {
                mod += 4.0F;
            }
            if (target instanceof EntityDeathWorm) {
                mod += 4.0F;
            }
        }
        else if (getMaterial() == IafItemRegistry.fireBoneTools) {
            if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToFire()) {
                mod += 8F;
            }
            target.setFire(5);
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
        else if (getMaterial() == IafItemRegistry.iceBoneTools || getMaterial() == IafItemRegistry.dread_queen_sword_tools) {
            if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToIce()) {
                mod += 8F;
            }
            if (!target.world.isRemote) {
                IEntityEffectCapability capability = InFCapabilities.getEntityEffectCapability(target);
                if (capability != null) capability.setFrozen(200);
            }
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
            target.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100, 2));
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
        else if (getMaterial() == IafItemRegistry.lightningBoneTools) {
            if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToLightning()) {
                mod += 4F;
            }
            ChainLightningUtils.createChainLightningFromTarget(target.world, target, attacker);
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
		else if (getMaterial() == IafItemRegistry.fireDragonsteelTools) {
			if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToFire()) {
				mod += 12F;
			}
			target.setFire(10);
			target.knockBack(target, 1.5F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		else if (getMaterial() == IafItemRegistry.iceDragonsteelTools) {
			if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToIce()) {
				mod += 12F;
			}
			if (!target.world.isRemote) {
				target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 4));
				target.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 4));
			}
			target.knockBack(target, 1.5F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		else if (getMaterial() == IafItemRegistry.lightningDragonsteelTools) {
			if (target instanceof EntityDragonBase && ((EntityDragonBase)target).isWeakToLightning()) {
				mod += 8F;
			}
			ChainLightningUtils.createChainLightningFromTarget(target.world, target, attacker);
			target.knockBack(target, 1.5F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		if (this == IafItemRegistry.myrmex_desert_sword_venom || this == IafItemRegistry.myrmex_jungle_sword_venom) {
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 2));
		}
		return mod;
	}
}