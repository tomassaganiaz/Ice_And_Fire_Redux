package com.github.Redux.iceandfire.api;
/** IEffectSetters — I Effect Setters */


public interface IEffectSetters {
    void setCharmed(int entityID);
    void setCharmed(int time, int entityID);
    void setFrozen();
    void setFrozen(int time);
    void setFrozen(int time, int severity);
    void setBlazed();
    void setBlazed(int time);
    void setBlazed(int time, int severity);
    void setShocked();
    void setShocked(int time);
    void setShocked(int time, int severity);
    void setShivaxiBlazed();
    void setShivaxiBlazed(int time);
    void setShivaxiBlazed(int time, int severity);
    void setSpooked(int entityID);
    void setSpooked(int time, int entityID);
    void setStoned();
}
