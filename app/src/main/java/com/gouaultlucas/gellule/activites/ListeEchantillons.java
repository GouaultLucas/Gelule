package com.gouaultlucas.gellule.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.gouaultlucas.gellule.R;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gouaultlucas.gellule.modele.Mouvement;
import com.gouaultlucas.gellule.modele.MouvementType;
import com.gouaultlucas.gellule.modele.dao.BdAdapter;
import com.gouaultlucas.gellule.utils.DateUtils;

public class ListeEchantillons extends AppCompatActivity {

    private BdAdapter bdAdapter;
    private ListView listViewEchantillons;
    private String dernierCodeClique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_echantillons);

        // Initialisation de l'adaptateur de base de données
        bdAdapter = new BdAdapter(this);
        bdAdapter.open();

        // Récupération de la référence de la ListView
        listViewEchantillons = findViewById(R.id.ListViewEchantillons);

        // Evènement quand on clique sur un élément de la ListView
        listViewEchantillons.setOnItemClickListener((parent, view, position, id) -> {
            TextView textViewCode = (TextView) view.findViewById(R.id.textViewCode);

            // Vérifie si le dernier code cliqué est le même
            if (dernierCodeClique != null && dernierCodeClique.equals(textViewCode.getText().toString())) {
                // Récupération du code de l'échantillon
                String code = textViewCode.getText().toString();

                // Lancement de l'action de suppression de l'échantillon
                bdAdapter.removeEchantillonWithCode(code);

                // Création du mouvement
                bdAdapter.insererMouvement(new Mouvement(MouvementType.SUPPRESSION, DateUtils.currentSqlDate(), "Suppression de l'échantillon " + code));

                afficherEchantillons();

                dernierCodeClique = null;

                // Affichage d'un toast indiquant que l'échantillon a été supprimé
                Toast.makeText(this, "L'échantillon " + textViewCode.getText().toString() + " a été supprimé", Toast.LENGTH_SHORT).show();
            }
            else {
                dernierCodeClique = textViewCode.getText().toString();

                // Affichage d'un toast indiquant que l'échantillon a été sélectionné pour la suppression
                Toast.makeText(this, "Appuyez à nouveau pour supprimer l'échantillon", Toast.LENGTH_LONG).show();
            }
        });

        // Affichage des échantillons dans la ListView
        afficherEchantillons();
    }

    private void afficherEchantillons() {
        Cursor cursor = bdAdapter.getData();

        // Configuration du mapping entre les colonnes de la base de données et les vues dans la ListView
        String[] columns = new String[] { BdAdapter.COL_CODE, BdAdapter.COL_LIB, BdAdapter.COL_STOCK };
        int[] to = new int[] { R.id.textViewCode, R.id.textViewLibelle, R.id.textViewStock };

        // Création de l'adaptateur pour peupler la ListView avec les données de la base de données
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.listview_echantillon, cursor, columns, to, 0);

        // Liaison de l'adaptateur à la ListView
        listViewEchantillons.setAdapter(dataAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermeture de l'adaptateur de base de données lorsque l'activité est détruite
        bdAdapter.close();
    }
}
