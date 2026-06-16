package com.github.Redux.iceandfire.entity;
/** Marca entidades que temen a aldeanos */


public interface IVillagerFear {
	
	default boolean shouldFear(){
        return true;
    }
}
