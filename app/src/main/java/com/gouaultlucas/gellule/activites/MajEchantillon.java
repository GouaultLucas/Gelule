package com.gouaultlucas.gellule.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gouaultlucas.gellule.R;
import com.gouaultlucas.gellule.modele.Mouvement;
import com.gouaultlucas.gellule.modele.MouvementType;
import com.gouaultlucas.gellule.modele.dao.BdAdapter;
import com.gouaultlucas.gellule.utils.DateUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MajEchantillon extends AppCompatActivity {

    private BdAdapter bdAdapter;
    private EditText editTextCodeMaj, editTextQteMaj;
    private Button buttonAjouterMaj, buttonRetirerMaj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maj_echantillon);

        // Initialisation de l'adaptateur de base de données
        bdAdapter = new BdAdapter(this);
        bdAdapter.open();

        // Récupération des références des EditTexts et des boutons
        editTextCodeMaj = findViewById(R.id.editTextCodeMaj);
        editTextQteMaj = findViewById(R.id.editTextQteMaj);
        buttonAjouterMaj = findViewById(R.id.buttonAjouterMaj);
        buttonRetirerMaj = findViewById(R.id.buttonRetirerMaj);

        // Configuration des listeners pour les boutons "Ajouter" et "Retirer"
        buttonAjouterMaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                majQuantite(true); // True indique une addition de quantité
            }
        });

        buttonRetirerMaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                majQuantite(false); // False indique une soustraction de quantité
            }
        });
    }

    /**
     * Méthode pour mettre à jour la quantité d'échantillon
     * @param ajouter True pour ajouter, False pour retirer
     */
    private void majQuantite(boolean ajouter) {
        // Récupération des valeurs saisies dans les EditTexts
        String code = editTextCodeMaj.getText().toString().trim();
        String qteStr = editTextQteMaj.getText().toString().trim();

        // Vérification si le champ du code est vide
        if (code.isEmpty()) {
            Toast.makeText(MajEchantillon.this, "Veuillez saisir un code", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification si le champ de la quantité est vide
        if (qteStr.isEmpty()) {
            Toast.makeText(MajEchantillon.this, "Veuillez saisir une quantité", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversion de la quantité en entier
        int qte;
        try {
            qte = Integer.parseInt(qteStr);
        } catch (NumberFormatException e) {
            Toast.makeText(MajEchantillon.this, "La quantité saisie n'est pas valide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification si l'échantillon n'existe pas dans la base de données
        if (!bdAdapter.verifierSiCodeExiste(code)) {
            Toast.makeText(MajEchantillon.this, "Échantillon non trouvé dans la base de données", Toast.LENGTH_SHORT).show();
        }
        else {
            // Mise à jour de la quantité dans la base de données
            int nouvelleQte = ajouter ? bdAdapter.ajouterQuantite(code, qte) : bdAdapter.retirerQuantite(code, qte);

            if (nouvelleQte != -1) {
                Toast.makeText(MajEchantillon.this, "Quantité mise à jour avec succès", Toast.LENGTH_SHORT).show();

                // Création d'un mouvement
                bdAdapter.insererMouvement(new Mouvement(MouvementType.MODIFICATION, DateUtils.currentSqlDate(), (ajouter ? "Ajout" : "Retrait") + " d'une quantité de " + qte + " à l'échantillon " + code));
            } else {
                Toast.makeText(MajEchantillon.this, "Échantillon non trouvé dans la base de données", Toast.LENGTH_SHORT).show();
            }

            // Réinitialisation des EditTexts après la mise à jour
            editTextCodeMaj.setText("");
            editTextQteMaj.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermeture de l'adaptateur de base de données lorsque l'activité est détruite
        bdAdapter.close();
    }
}
