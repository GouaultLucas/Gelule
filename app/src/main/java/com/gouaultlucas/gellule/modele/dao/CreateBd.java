package com.gouaultlucas.gellule.modele.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateBd extends SQLiteOpenHelper {

    private static final String TABLE_ECHANT = "echantillons";
    static final String COL_ID = "_id";
    private static final String COL_CODE = "CODE";
    private static final String COL_LIB = "LIB";
    private static final String COL_STOCK = "STOCK";

    private static final String CREATE_TABLE_ECHANTILLON = "CREATE TABLE " + TABLE_ECHANT + " ("
            + COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_CODE + " TEXT NOT NULL, " + COL_LIB + " TEXT NOT NULL, "
            + COL_STOCK + " TEXT NOT NULL"
            + ");";

    private static final String TABLE_MOUVEMENT = "logs";
    private static final String COL_ID_MOUVEMENT = "_id";
    private static final String COL_DATE_MOUVEMENT = "DATE";
    private static final String COL_TYPE_MOUVEMENT = "TYPE";
    private static final String COL_MESSAGE_MOUVEMENT = "MESSAGE";
    private static final String CREATE_TABLE_MOUVEMENT = "CREATE TABLE " + TABLE_MOUVEMENT + " ("
            + COL_ID_MOUVEMENT +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_DATE_MOUVEMENT + " TEXT NOT NULL,"
            + COL_TYPE_MOUVEMENT + " INTEGER NOT NULL,"
            + COL_MESSAGE_MOUVEMENT + " TEXT NOT NULL"
            + ");";

    public CreateBd(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // appelée lorsqu’aucune base n’existe et que la classe utilitaire doit en créer une
        //on créé la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_TABLE_ECHANTILLON);
        db.execSQL(CREATE_TABLE_MOUVEMENT);
    }

    @Override // appelée si la version de la base a changé
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut  supprimer la table et de la recréer
        db.execSQL("DROP TABLE " + TABLE_ECHANT + ";");
        db.execSQL("DROP TABLE " + TABLE_MOUVEMENT + ";");
        onCreate(db);
    }
}

