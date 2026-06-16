package com.github.Redux.iceandfire.api;

/**
 * Full capability interface combining all effect sub-interfaces.
 * For narrower contracts, use IEffectState, IEffectSetters,
 * IEffectQueries, IEffectTickable, or IEffectEntityLookup as needed.
 */
public interface IEntityEffectCapability extends IEffectState, IEffectSetters, IEffectQueries, IEffectTickable, IEffectEntityLookup {
}