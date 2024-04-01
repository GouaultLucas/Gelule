package com.gouaultlucas.gellule.modele;

import androidx.annotation.Nullable;

public enum MouvementType {
    AJOUT,
    SUPPRESSION,
    MODIFICATION;

    public static MouvementType fromInt(int i) {
        switch (i) {
            case 0:
                return AJOUT;
            case 1:
                return SUPPRESSION;
            case 2:
                return MODIFICATION;
            default:
                return null;
        }
    }

    @Nullable
    public String toString() {
        switch (this) {
            case AJOUT:
                return "AJOUT";
            case SUPPRESSION:
                return "SUPPRESSION";
            case MODIFICATION:
                return "MODIFICATION";
            default:
                return null;
        }
    }
}
