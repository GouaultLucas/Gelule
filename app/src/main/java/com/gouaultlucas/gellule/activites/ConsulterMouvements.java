package com.gouaultlucas.gellule.activites;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gouaultlucas.gellule.R;
import com.gouaultlucas.gellule.modele.Mouvement;
import com.gouaultlucas.gellule.modele.dao.BdAdapter;

import java.util.ArrayList;

public class ConsulterMouvements extends AppCompatActivity {

    private BdAdapter bdAdapter;
    private ListView listViewMouvements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_mouvements);

        // Initialisation de l'adaptateur de base de données
        bdAdapter = new BdAdapter(this);
        bdAdapter.open();

        // Récupération de la ListView
        listViewMouvements = findViewById(R.id.listViewMouvements);

        // Affichage des mouvements dans la ListView
        afficherMouvements();
    }

    private void afficherMouvements() {
        ArrayList<Mouvement> mouvements = bdAdapter.getMouvements();

        ArrayList<String> lignes = new ArrayList<>();

        for(Mouvement mouvement : mouvements) {
            lignes.add("[" + mouvement.getType().toString() + " : " + mouvement.getDate() + "] " + mouvement.getMessage());
        }

        // Création de l'adaptateur pour peupler la ListView avec les données de la base de données
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lignes);

        // Liaison de l'adaptateur à la ListView
        listViewMouvements.setAdapter(arrayAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermeture de l'adaptateur de base de données lorsque l'activité est détruite
        bdAdapter.close();
    }
}