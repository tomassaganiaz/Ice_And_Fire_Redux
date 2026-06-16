package com.github.Redux.iceandfire.api;

import net.minecraft.entity.EntityLivingBase;
/** SensesUtils — Senses Utils */


public class SensesUtils {

    private static final SenseEvaluator EVALUATOR = new SenseEvaluator();

    public static boolean isBlind(EntityLivingBase entity) {
        return EVALUATOR.isBlind(entity);
    }

    public static boolean isDeaf(EntityLivingBase entity) {
        return EVALUATOR.isDeaf(entity);
    }
}
