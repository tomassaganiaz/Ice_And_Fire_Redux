package com.github.Redux.iceandfire.world.village;

import java.util.List;
/** AbstractVillagePieces — Abstract Village Pieces */


public class AbstractVillagePieces {

    public static class PieceWeight {
        public final int villagePieceWeight;
        public Class<?> villagePieceClass;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;

        public PieceWeight(Class<?> p_i2098_1_, int p_i2098_2_, int p_i2098_3_) {
            this.villagePieceClass = p_i2098_1_;
            this.villagePieceWeight = p_i2098_2_;
            this.villagePiecesLimit = p_i2098_3_;
        }

        public boolean canSpawnMoreVillagePiecesOfType(int componentType) {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }

        public boolean canSpawnMoreVillagePieces() {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }

    protected static int updatePieceWeight(List<PieceWeight> p_75079_0_) {
        boolean flag = false;
        int i = 0;
        for (PieceWeight pieceweight : p_75079_0_) {
            if (pieceweight.villagePiecesLimit > 0 && pieceweight.villagePiecesSpawned < pieceweight.villagePiecesLimit) {
                flag = true;
            }
            i += pieceweight.villagePieceWeight;
        }
        return flag ? i : -1;
    }
}
