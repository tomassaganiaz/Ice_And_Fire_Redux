package com.github.Redux.iceandfire.client.particle.lightning;

/**
 * Creado por ChickenBones para Botania.
 * ParticleLightningBoltPoint — punto de rayo con vector de offset
 */
public class ParticleLightningBoltPoint {

    public final ParticleLightningVector point;
    public final ParticleLightningVector basepoint;
    public final ParticleLightningVector offsetvec;

    public ParticleLightningBoltPoint(ParticleLightningVector basepoint, ParticleLightningVector offsetvec) {
        point = basepoint.add(offsetvec);
        this.basepoint = basepoint;
        this.offsetvec = offsetvec;
    }
}
