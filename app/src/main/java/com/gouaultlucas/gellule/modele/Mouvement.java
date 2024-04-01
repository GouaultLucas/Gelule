package com.gouaultlucas.gellule.modele;

import com.gouaultlucas.gellule.modele.dao.BdAdapter;

public class Mouvement {

    private String date;
    private MouvementType type;
    private String message;

    public Mouvement( MouvementType type, String date, String message) {
        this.date = date;
        this.type = type;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public MouvementType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(MouvementType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void enregistrer( BdAdapter bdAdapter ) {
        bdAdapter.insererMouvement(this);
    }
}
