package com.github.Redux.iceandfire.api;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
/** SenseEvaluator — Sense Evaluator */


public class SenseEvaluator {

    public boolean isBlind(EntityLivingBase entity) {
        if (entity == null) {
            return false;
        }
        if (entity.isPotionActive(MobEffects.BLINDNESS)) {
            return true;
        }
        ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helmet.getItem() == IafItemRegistry.blindfold) {
            return true;
        }
        if (CompatLoadUtil.isBaublesLoaded() && entity instanceof EntityPlayer) {
            return BaublesApi.getBaublesHandler((EntityPlayer) entity)
                    .getStackInSlot(BaubleType.HEAD.getValidSlots()[0])
                    .getItem() == IafItemRegistry.blindfold;
        }
        return false;
    }

    public boolean isDeaf(EntityLivingBase entity) {
        if (entity == null) {
            return false;
        }
        ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helmet.getItem() == IafItemRegistry.earplugs
                || (!helmet.isEmpty() && helmet.getItem().getTranslationKey().contains("earmuff"))) {
            return true;
        }
        if (CompatLoadUtil.isBaublesLoaded() && entity instanceof EntityPlayer) {
            return BaublesApi.getBaublesHandler((EntityPlayer) entity)
                    .getStackInSlot(BaubleType.HEAD.getValidSlots()[0])
                    .getItem() == IafItemRegistry.earplugs;
        }
        return false;
    }
}
