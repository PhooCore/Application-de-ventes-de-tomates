package ihm;

import modèle.FactureClient;
import modèle.Panier;
import modèle.Tomate;
import modèle.Tomates;
import modèle.OutilsBaseDonneesTomates;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;

/**
 * Fenêtre affichant la facture du client dà l'aide d'un code HTML
 * avec la possibilté d'imprimer
 */
public class Facture extends JFrame {

    private static final long serialVersionUID = 1L;

    // Le panier contenant les tomates commandées
    private final Panier panier;
    // Les informations client associées à la commande (nom, adresse, etc.)
    private final FactureClient commande;

    // Constructeur avec le panier, la commande (infos client), et la base des tomates (catalogue)
    public Facture(Panier panier, FactureClient commande, Tomates baseTomates) {
        this.panier = panier;
        this.commande = commande;

        // Initialisation de la fenêtre principale
        pagePrincipale(baseTomates);
    }

    // Création et organisation des composants graphiques principaux de la fenêtre
    private void pagePrincipale(Tomates baseTomates) {
        setTitle("Facture"); // Titre de la fenêtre
        setSize(600, 500); // Taille initiale
        setLocationRelativeTo(null); // Centrer la fenêtre à l'écran
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fermer seulement cette fenêtre à la fermeture

        // Zone de texte avec support HTML pour afficher le récapitulatif de la facture
        JTextPane txtFacture = new JTextPane();
        txtFacture.setContentType("text/html"); // On autorise le rendu HTML
        txtFacture.setText(genererRecapitulatifHtml()); // On insère le contenu HTML généré
        txtFacture.setEditable(false); // Non modifiable par l'utilisateur

        // On place la zone texte dans une JScrollPane pour pouvoir scroller si le contenu est long
        JScrollPane scrollPane = new JScrollPane(txtFacture);
        getContentPane().add(scrollPane, BorderLayout.CENTER); // Zone centrale de la fenêtre

        // Panel bas avec des boutons alignés à droite (Modifier, Imprimer, Valider)
        JPanel pied = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pied.setBackground(new Color(255, 204, 204)); // Couleur de fond douce

        // Boutons créés par des méthodes dédiées pour garder le code clair
        pied.add(creerBoutonModifier(baseTomates));
        pied.add(creerBoutonImprimer(txtFacture));
        pied.add(creerBoutonValider(baseTomates));

        getContentPane().add(pied, BorderLayout.SOUTH); // Placement en bas
        setVisible(true); // Rendre la fenêtre visible
    }

    // Bouton pour revenir à la fenêtre des coordonnées (modifier la commande)
    private JButton creerBoutonModifier(Tomates baseTomates) {
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> {
            dispose(); // Ferme la fenêtre facture
            Coordonnées fenetreCoordonnees = new Coordonnées(panier, baseTomates); // Ouvre la fenêtre coordonnées
            fenetreCoordonnees.setVisible(true);
        });
        return btnModifier;
    }

    // Bouton pour imprimer la facture affichée dans le JTextPane
    private JButton creerBoutonImprimer(JTextPane txtFacture) {
        JButton btnImprimer = new JButton("Imprimer");
        btnImprimer.addActionListener(e -> {
            try {
                boolean complete = txtFacture.print(); // Lance la boîte d'impression
                String message = complete ? "Impression terminée !" : "Impression annulée.";
                JOptionPane.showMessageDialog(this, message);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Erreur d'impression : " + ex.getMessage());
            }
        });
        return btnImprimer;
    }

    // Bouton pour valider définitivement la commande, sauvegarder les données, puis fermer l'application
    private JButton creerBoutonValider(Tomates baseTomates) {
        JButton btnValider = new JButton("Valider la commande");
        btnValider.addActionListener(e -> {
            // Sauvegarde des données tomates dans un fichier JSON
            OutilsBaseDonneesTomates.sauvegarderBaseDeTomates(baseTomates, "src/main/resources/data/tomates.json");
            JOptionPane.showMessageDialog(this, "Merci pour votre commande !");
            System.exit(0); // Ferme toute l'application
        });
        return btnValider;
    }

    // Génère le contenu HTML complet du récapitulatif de la facture
    private String genererRecapitulatifHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif; margin: 20px;'>")
            .append("<h1>Facture de votre commande</h1>")
            .append(genererInformationsClient()) // Infos client
            .append(genererTableauArticles())   // Tableau avec détails des articles
            .append("</body></html>");
        return html.toString();
    }

    // Génère la partie HTML contenant les informations du client
    private String genererInformationsClient() {
        StringBuilder html = new StringBuilder();
        html.append("<p><strong>Nom :</strong> ").append(commande.getNom()).append("</p>")
            .append("<p><strong>Prénom :</strong> ").append(commande.getPrenom()).append("</p>")
            .append("<p><strong>Adresse 1 :</strong> ").append(commande.getAdresse1()).append("</p>");
        if (!commande.getAdresse2().isBlank()) {
            html.append("<p><strong>Adresse 2 :</strong> ").append(commande.getAdresse2()).append("</p>");
        }
        html.append("<p><strong>Code Postal & Ville :</strong> ")
            .append(commande.getCodePostal()).append(" ").append(commande.getVille()).append("</p>")
            .append("<p><strong>Téléphone :</strong> ").append(commande.getTelephone()).append("</p>")
            .append("<p><strong>Email :</strong> ").append(commande.getEmail()).append("</p>")
            .append("<p><strong>Mode de paiement :</strong> ").append(commande.getMoyenPaiement()).append("</p>")
            .append("<p><strong>Abonnement newsletter :</strong> ")
            .append(commande.isAbonnement() ? "Oui" : "Non").append("</p>");
        return html.toString();
    }

    // Génère la table HTML des articles commandés (désignation, prix, quantité, total)
    private String genererTableauArticles() {
        StringBuilder html = new StringBuilder();
        html.append("<h2>Détails des articles</h2>")
            .append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>")
            .append("<tr><th>Désignation</th><th>Prix unitaire</th><th>Quantité</th><th>Total</th></tr>")
            .append(genererLignesArticles())
            .append("</table>");

        // Si le panier n'est pas vide, on affiche les frais de livraison
        if (!panier.getTomates().isEmpty()) {
            html.append("<p><strong>Frais de livraison :</strong> 5.50 €</p>");
        }
        // Affiche le total global
        html.append("<p><strong>Total :</strong> ")
            .append(String.format("%.2f €", panier.getTotal()))
            .append("</p>");

        return html.toString();
    }

    // Génère les lignes HTML du tableau des articles à partir des listes dans le panier
    private String genererLignesArticles() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < panier.getTomates().size(); i++) {
            Tomate t = panier.getTomates().get(i);
            int qte = panier.getQuantites().get(i);
            double total = t.getPrixTTC() * qte;

            sb.append("<tr>")
              .append("<td>").append(t.getDésignation()).append("</td>")
              .append("<td>").append(String.format("%.2f €", t.getPrixTTC())).append("</td>")
              .append("<td>").append(qte).append("</td>")
              .append("<td>").append(String.format("%.2f €", total)).append("</td>")
              .append("</tr>");
        }
        return sb.toString();
    }
}
