package com.gouaultlucas.gellule.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import com.gouaultlucas.gellule.R;
import com.gouaultlucas.gellule.modele.Echantillon;
import com.gouaultlucas.gellule.modele.dao.BdAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.boutonAjoutEchantillon);
        Button button2 = findViewById(R.id.boutonListeEchantillons);
        Button button3 = findViewById(R.id.boutonMajEchantillon);
        Button button4 = findViewById(R.id.boutonMouvements);
        Button button5 = findViewById(R.id.boutonReset);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, AjoutEchantillon.class);
            startActivity(intent);
        });

        button2.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListeEchantillons.class);
            startActivity(intent);
        });

        button3.setOnClickListener(v -> {
            Intent intent = new Intent(this, MajEchantillon.class);
            startActivity(intent);
        });

        button4.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConsulterMouvements.class);
            startActivity(intent);
        });

        button5.setOnClickListener(v -> {
            BdAdapter bd = new BdAdapter(this);
            bd.open();
            bd.resetBd();
            jeuEssaiBd();
            bd.close();
        });
    }

    public void jeuEssaiBd(){
        //Création d'une instance de la classe echantBDD
        BdAdapter echantBdd = new BdAdapter(this);

        //On ouvre la base de données pour écrire dedans
        echantBdd.open();
        //On insère DES ECHANTILLONS DANS LA BD
        echantBdd.insererEchantillon(new Echantillon("code1", "lib1", "3"));
        echantBdd.insererEchantillon(new Echantillon("code2", "lib2", "5"));
        echantBdd.insererEchantillon(new Echantillon("code3", "lib3", "7"));
        echantBdd.insererEchantillon(new Echantillon("code4", "lib4", "6"));
        Cursor unCurseur = echantBdd.getData();
        System.out.println("il y a "+unCurseur.getCount()+" echantillons dans la BD");
        echantBdd.close();
    }

}