package com.github.Redux.iceandfire.integration;

import bettercombat.mod.event.RLCombatModifyDamageEvent;
import com.github.Redux.iceandfire.CommonProxy;
import com.github.Redux.iceandfire.entity.EntityDragonBase;
import com.github.Redux.iceandfire.item.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/** RLCombatCompat — RL Combat Compat */


public class RLCombatCompat {

    @SubscribeEvent
    public static void modifyAttackDamagePre(RLCombatModifyDamageEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        Entity target = event.getTarget();
        if (player == null || !(target instanceof EntityLivingBase) || event.getStack().isEmpty()) return;

        Item item = event.getStack().getItem();
        if (item instanceof IHitEffect) {
            event.setDamageModifier(event.getDamageModifier() + ((IHitEffect) item).getHitEffectModifier((EntityLivingBase)target, player));
        }

        // Dragon Slayer enchantment
        if (target instanceof EntityDragonBase) {
            int slayerLevel = EnchantmentHelper.getEnchantmentLevel(CommonProxy.DRAGON_SLAYER, event.getStack());
            if (slayerLevel > 0) {
                event.setDamageModifier(event.getDamageModifier() * (1.0F + 0.05F + slayerLevel * 0.10F));
            }
        }
    }
}