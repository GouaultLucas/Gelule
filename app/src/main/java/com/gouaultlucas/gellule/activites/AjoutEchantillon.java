package com.gouaultlucas.gellule.activites;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gouaultlucas.gellule.R;
import com.gouaultlucas.gellule.modele.Echantillon;
import com.gouaultlucas.gellule.modele.Mouvement;
import com.gouaultlucas.gellule.modele.MouvementType;
import com.gouaultlucas.gellule.modele.dao.BdAdapter;
import com.gouaultlucas.gellule.utils.DateUtils;

public class AjoutEchantillon extends AppCompatActivity {

    private BdAdapter bdAdapter;
    private EditText editTextCode, editTextLibelle, editTextQuantiteStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_echantillon);

        // Initialisation de l'adaptateur de base de données
        bdAdapter = new BdAdapter(this);
        bdAdapter.open();

        // Récupération des références des EditTexts et du bouton
        editTextCode = findViewById(R.id.editTextCode);
        editTextLibelle = findViewById(R.id.editTextLibelle);
        editTextQuantiteStock = findViewById(R.id.editTextQteStock);
        Button buttonAjouter = findViewById(R.id.buttonAjout);

        // Configuration du listener pour le bouton "Ajouter"
        buttonAjouter.setOnClickListener(v -> {
            // Récupération des valeurs saisies dans les EditTexts
            String code = editTextCode.getText().toString().trim();
            String libelle = editTextLibelle.getText().toString().trim();
            String quantiteStock = editTextQuantiteStock.getText().toString().trim();

            // Vérification si tous les champs sont remplis
            if (!code.isEmpty() && !libelle.isEmpty() && !quantiteStock.isEmpty()) {
                // Vérification si le code de l'échantillon existe de déjà dans la base de données
                if (!bdAdapter.verifierSiCodeExiste(code)) {
                    // Insertion de l'échantillon dans la base de données
                    bdAdapter.insererEchantillon(new Echantillon(code, libelle, quantiteStock));

                    // Création du mouvement
                    bdAdapter.insererMouvement(new Mouvement(MouvementType.AJOUT, DateUtils.currentSqlDate(), "Ajout de l'échantillon " + code + " (" + libelle + ")"));

                    // Affichage d'un message de succès ou de confirmation
                    Toast.makeText(AjoutEchantillon.this, "Echantillon ajouté avec succès", Toast.LENGTH_SHORT).show();

                    // Réinitialisation des EditTexts après l'ajout
                    editTextCode.setText("");
                    editTextLibelle.setText("");
                    editTextQuantiteStock.setText("");
                }
                else {
                    // Affichage d'un message d'erreur si le code de l'échantillon existe déjà dans la base de données
                    Toast.makeText(AjoutEchantillon.this, "Le code de l'échantillon existe déjà dans la base de données", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Affichage d'un message d'erreur si tous les champs ne sont pas remplis
                Toast.makeText(AjoutEchantillon.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermeture de l'adaptateur de base de données lorsque l'activité est détruite
        bdAdapter.close();
    }
}
