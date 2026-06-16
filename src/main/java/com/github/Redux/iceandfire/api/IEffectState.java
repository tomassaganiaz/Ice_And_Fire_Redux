package com.github.Redux.iceandfire.api;

import com.github.Redux.iceandfire.capability.entityeffect.EntityEffectCapability;
/** IEffectState — I Effect State */


public interface IEffectState {
    EntityEffectCapability.EntityEffectEnum getEffect();
    EntityEffectCapability.EntityEffectEnum getPreviousEffect();
    int getTime();
    int getAdditionalData();

    void setEffect(EntityEffectCapability.EntityEffectEnum effect, int time, int additional);
    void reset();
}
