package com.gouaultlucas.gellule.modele.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gouaultlucas.gellule.modele.Echantillon;
import com.gouaultlucas.gellule.modele.Mouvement;
import com.gouaultlucas.gellule.modele.MouvementType;

import java.util.ArrayList;

public class BdAdapter {
    static final int VERSION_BDD = 2;
    private static final String NOM_BDD = "gsb.db";
    private static final String TABLE_ECHANT = "echantillons";
    public static final String COL_ID = "_id";
    public static final int NUM_COL_ID = 0;
    public static final String COL_CODE = "CODE";
    public static final int NUM_COL_CODE = 1;
    public static final String COL_LIB = "LIB";
    public static final int NUM_COL_LIB = 2;
    public static final String COL_STOCK = "STOCK";
    public static final int NUM_COL_STOCK = 3;
    public static final String TABLE_MOUVEMENT = "logs";
    public static final String COL_ID_MOUVEMENT = "_id";
    public static final int NUM_COL_ID_MOUVEMENT = 0;
    public static final String COL_DATE_MOUVEMENT = "DATE";
    public static final int NUM_COL_DATE_MOUVEMENT = 1;
    public static final String COL_TYPE_MOUVEMENT = "TYPE";
    public static final int NUM_COL_TYPE_MOUVEMENT = 2;
    public static final String COL_MESSAGE_MOUVEMENT = "MESSAGE";
    public static final int NUM_COL_MESSAGE_MOUVEMENT = 3;

    private CreateBd bdArticles;
    private Context context;
    private SQLiteDatabase db;

    public BdAdapter(Context context){
        this.context = context;
        bdArticles = new CreateBd(context, NOM_BDD, null, VERSION_BDD);
    }

    // si la base n’existe pas, l’objet SQLiteOpenHelper exécute la méthode onCreate
    // si la version de la base a changé, la méthode onUpgrade sera lancée, dans les 2 cas l’appel
    // à getWritableDatabase permet d’ouvrir une connexion à la base en écriture
    public BdAdapter  open() {
        db = bdArticles.getWritableDatabase();
        return this;
    }
    public BdAdapter close() {
        db.close();
        return null;
    }

    public long insererEchantillon(Echantillon unEchant){

        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne où on veut mettre la valeur)
        values.put(COL_CODE, unEchant.getCode());
        values.put(COL_LIB, unEchant.getLibelle());
        values.put(COL_STOCK, unEchant.getQuantiteStock());
        //on insère l'objet dans la BDD via le ContentValues
        return db.insert(TABLE_ECHANT, null, values);
    }

    private Echantillon cursorToEchant(Cursor c){
        //Cette méthode permet de convertir un cursor en un echantillon
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        Echantillon unEchant = null ;
        if (c.getCount() != 0) {
            c.moveToFirst();   // on se place sur le premier élément
            // On créé un echantillon
            // en lui affectant toutes les infos grâce aux infos contenues dans le Cursor
            unEchant = new Echantillon(
                    c.getString(NUM_COL_CODE),
                    c.getString(NUM_COL_LIB),
                    c.getString(NUM_COL_STOCK)
                    );
        }
        c.close();     		// On ferme le cursor
        return unEchant;   	// On retourne l'echantillon
    }

    public Echantillon getEchantillonWithCode(String unCode){
        // Récupère dans un Cursor les valeurs correspondant à un echantillon grâce à sa designation
        Cursor c = db.query(TABLE_ECHANT, new String[] {COL_ID,COL_CODE, COL_LIB,
                COL_STOCK}, COL_CODE + " LIKE \"" + unCode +"\"", null, null, null, null);
        return cursorToEchant(c);
    }

    public int updateEchantillon(String unCode, Echantillon unEchant){
        // La mise à jour d'un echantillon dans la BDD fonctionne plus ou moins  comme une insertion,
        // il faut simple préciser quel echantillon on doit mettre à jour grâce à son code
        ContentValues values = new ContentValues();
        values.put(COL_CODE, unEchant.getCode());
        values.put(COL_LIB, unEchant.getLibelle());
        values.put(COL_STOCK, unEchant.getQuantiteStock());
        return db.update(TABLE_ECHANT, values, COL_CODE + " = \"" +unCode+"\"", null);
    }

    public int removeEchantillonWithCode(String unCode){
        // Suppression d'un echantillon de la BDD grâce à son code
        return db.delete(TABLE_ECHANT, COL_CODE + " = \"" +unCode+"\"", null);
    }

    public boolean verifierSiCodeExiste(String unCode){
        Cursor c = db.query(TABLE_ECHANT, new String[] {COL_ID,COL_CODE, COL_LIB,
                COL_STOCK}, COL_CODE + " LIKE \"" + unCode +"\"", null, null, null, null);
        return c.getCount() != 0;
    }

    public int ajouterQuantite(String unCode, int qte){
        // Récupère la quantité d'un echantillon à partir de son code
        Echantillon unEchant = getEchantillonWithCode(unCode);
        int nouvelleQte;
        try {
            nouvelleQte = Integer.parseInt(unEchant.getQuantiteStock());
        } catch (NumberFormatException e) {
            nouvelleQte = 0;
        }

        nouvelleQte += qte;

        ContentValues values = new ContentValues();
        values.put(COL_STOCK, nouvelleQte);
        return db.update(TABLE_ECHANT, values, COL_CODE + " = \"" +unCode+"\"", null);
    }

    public int retirerQuantite(String unCode, int qte){
        // Récupère la quantité d'un echantillon à partir de son code
        Echantillon unEchant = getEchantillonWithCode(unCode);
        int nouvelleQte;
        try {
            nouvelleQte = Integer.parseInt(unEchant.getQuantiteStock());
        } catch (NumberFormatException e) {
            nouvelleQte = 0;
        }

        nouvelleQte -= qte;

        ContentValues values = new ContentValues();
        values.put(COL_STOCK, nouvelleQte);
        return db.update(TABLE_ECHANT, values, COL_CODE + " = \"" +unCode+"\"", null);
    }

    public Cursor getData(){
        return db.rawQuery("SELECT * FROM echantillons", null);
    }

    public Cursor getMouvementsData(){
        return db.rawQuery("SELECT * FROM logs", null);
    }

    public Mouvement cursorToMouvement(Cursor c){
        //Cette méthode permet de convertir un cursor en un Mouvement
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        Mouvement unMouvement = null ;
        if (c.getCount() != 0) {
            // On cherche un Mouvement
            // en lui affectant toutes les infos contenues dans le Cursor
            unMouvement = new Mouvement(
                    MouvementType.fromInt(c.getInt(NUM_COL_TYPE_MOUVEMENT)),
                    c.getString(NUM_COL_DATE_MOUVEMENT),
                    c.getString(NUM_COL_MESSAGE_MOUVEMENT)
            );
        }
        return unMouvement;   	//On retourne le Mouvement
    }

    public ArrayList<Mouvement> getMouvements(){
        ArrayList<Mouvement> lesMouvements = new ArrayList<>();
        Cursor c = getMouvementsData();
        while(c.moveToNext()){
            lesMouvements.add(cursorToMouvement(c));
        }
        return lesMouvements;
    }

    public long insererMouvement(Mouvement unMouvement){
        // Initialisation du ContentValues
        ContentValues values = new ContentValues();

        // Ajout des valeurs
        values.put(COL_TYPE_MOUVEMENT, unMouvement.getType().ordinal());
        values.put(COL_DATE_MOUVEMENT, unMouvement.getDate());
        values.put(COL_MESSAGE_MOUVEMENT, unMouvement.getMessage());

        // Insertion dans la table
        return db.insert(TABLE_MOUVEMENT, null, values);
    }

    // Réinitialisation de la BDD
    public void resetBd() {
        db.execSQL("DROP TABLE " + TABLE_ECHANT + ";");
        db.execSQL("DROP TABLE " + TABLE_MOUVEMENT + ";");
        bdArticles.onCreate(db);
    }
}

