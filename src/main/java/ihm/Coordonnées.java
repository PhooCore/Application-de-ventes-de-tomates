package ihm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import modèle.FactureClient;
import modèle.Panier;
import modèle.Tomates;

/**
 * Fenêtre Coordonnées
 * Cette interface permet à l'utilisateur d'insérer ses informations personnelles.
 */
public class Coordonnées extends JFrame {
    private static final long serialVersionUID = 1L;

    // Champs de texte pour saisir les coordonnées du client
    private JTextField textFieldPrenom, textFieldCP, textFieldAdresse2,
                       textFieldNom, textFieldAdresse1, textFieldVille,
                       textFieldTel, textFieldMail;

    // Boutons radio pour choisir le mode de paiement
    private JRadioButton rdbCarte, rdbPaypal, rdbCheque;

    // Case à cocher pour s'abonner à la newsletter
    private JCheckBox chckbxNewsLetter;

    // Références aux objets métier : panier et base de tomates
    private final Panier panier;
    private final Tomates baseTomates;

    // Constructeur : création de la fenêtre avec ses composants
    public Coordonnées(Panier panier, Tomates baseTomates) {
        this.panier = panier;
        this.baseTomates = baseTomates;

        setTitle("Coordonnées client");  // Titre de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Ferme juste cette fenêtre à la fermeture
        setBounds(100, 100, 518, 453);  // Position et taille

        JPanel contentPane = creerPanneauPrincipal();  // Création du panneau principal
        setContentPane(contentPane);  // On l'ajoute à la fenêtre

        // Ajout des différentes parties dans le panneau principal (en haut, centre, bas)
        contentPane.add(creerEntête(), BorderLayout.NORTH);
        contentPane.add(creerFormulaire(), BorderLayout.CENTER);
        contentPane.add(creerPied(), BorderLayout.SOUTH);
    }

    // Création du panneau principal avec fond coloré et bordure
    private JPanel creerPanneauPrincipal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 204, 204));  
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));   
        return panel;
    }

    // Création du label en haut avec titre et icône
    private JLabel creerEntête() {
        JLabel label = new JLabel("VOS COORDONNÉES");
        label.setFont(new Font("Tahoma", Font.BOLD, 22)); // Police en gras, taille 22
        label.setForeground(new Color(153, 51, 51));      // Couleur texte rouge foncé
        String cheminImage = "/images/img_icones/coordonnees.png";
        ImageIcon image = new ImageIcon(getClass().getResource(cheminImage));
        label.setIcon(image);
        return label;
    }

    // Création du formulaire avec 8 lignes et 2 colonnes pour étiquettes + champs
    private JPanel creerFormulaire() {
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5)); // grille avec espacements
        formPanel.setBackground(new Color(255, 204, 204));

        // Ajout des champs avec leurs labels, et récupération des JTextField
        textFieldNom = addField(formPanel, "Nom :");
        textFieldPrenom = addField(formPanel, "Prénom :");
        textFieldAdresse1 = addField(formPanel, "Adresse 1 :");
        textFieldAdresse2 = addField(formPanel, "Adresse 2 :");
        textFieldCP = addField(formPanel, "Code Postal :");
        textFieldVille = addField(formPanel, "Ville :");
        textFieldTel = addField(formPanel, "Téléphone :");
        textFieldMail = addField(formPanel, "Mail :");

        return formPanel;
    }

    // Méthode auxiliaire pour ajouter un label et un champ texte au formulaire
    private JTextField addField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(51, 0, 0)); 
        JTextField textField = new JTextField();
        panel.add(label);
        panel.add(textField);
        return textField;  // On retourne le JTextField pour pouvoir y accéder ensuite
    }

    // Création du panneau en bas avec paiement, newsletter, et boutons
    private JPanel creerPied() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS)); // disposition verticale
        footer.setBackground(new Color(255, 204, 204));

        footer.add(creerPanelPayement());    // options paiement
        footer.add(creerNewsletterPanel()); // checkbox newsletter
        footer.add(creerBoutonPanel());     // boutons OK / Annuler

        return footer;
    }

    // Création du panneau pour les boutons radio de paiement
    private JPanel creerPanelPayement() {
        JPanel paymentPanel = new JPanel();
        paymentPanel.setBackground(new Color(255, 204, 204));

        rdbCarte = creerRadioBouton("Carte de crédit");
        rdbPaypal = creerRadioBouton("Paypal");
        rdbCheque = creerRadioBouton("Chèque");

        // Groupe pour que seul un bouton radio soit sélectionné à la fois
        ButtonGroup group = new ButtonGroup();
        group.add(rdbCarte);
        group.add(rdbPaypal);
        group.add(rdbCheque);

        // Ajout des boutons radio au panneau
        paymentPanel.add(rdbCarte);
        paymentPanel.add(rdbPaypal);
        paymentPanel.add(rdbCheque);

        return paymentPanel;
    }

    // Méthode auxiliaire pour créer un bouton radio avec style
    private JRadioButton creerRadioBouton(String text) {
        JRadioButton button = new JRadioButton(text);
        button.setForeground(new Color(51, 0, 0));
        button.setBackground(new Color(255, 204, 204));
        return button;
    }

    // Création du panneau pour la case à cocher newsletter
    private JPanel creerNewsletterPanel() {
        JPanel newsletterPanel = new JPanel();
        newsletterPanel.setBackground(new Color(255, 204, 204));
        chckbxNewsLetter = new JCheckBox("M'abonner à la news letter");
        chckbxNewsLetter.setForeground(new Color(51, 0, 0));
        chckbxNewsLetter.setBackground(new Color(255, 204, 204));
        newsletterPanel.add(chckbxNewsLetter);
        return newsletterPanel;
    }

    // Création du panneau pour les boutons OK et Annuler
    private JPanel creerBoutonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 204, 204));

        // Bouton OK valide le formulaire
        JButton btnValider = new JButton("OK");
        btnValider.addActionListener(this::validerFormulaire); // méthode appelée au clic
        buttonPanel.add(btnValider);

        // Bouton Annuler ferme la fenêtre sans rien faire
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.addActionListener(e -> dispose()); // ferme la fenêtre
        buttonPanel.add(btnAnnuler);

        return buttonPanel;
    }

    // Méthode appelée lors du clic sur OK : validation et création commande
    private void validerFormulaire(ActionEvent e) {
        if (!champsValides()) return;  // On arrête si les champs ne sont pas valides

        // Création d'une commande (FactureClient) avec les données du formulaire
        FactureClient commande = creerCommandeDepuisFormulaire();

        // Ouvre la fenêtre de facture en lui passant le panier, la commande et la base de tomates
        new Facture(panier, commande, baseTomates).setVisible(true);

        // Ferme la fenêtre des coordonnées
        dispose();
    }

    // Vérifie que tous les champs sont remplis et un mode de paiement sélectionné
    private boolean champsValides() {
        if (textFieldNom.getText().trim().isEmpty() ||
            textFieldPrenom.getText().trim().isEmpty() ||
            textFieldAdresse1.getText().trim().isEmpty() ||
            textFieldAdresse2.getText().trim().isEmpty() ||
            textFieldCP.getText().trim().isEmpty() ||
            textFieldVille.getText().trim().isEmpty() ||
            textFieldTel.getText().trim().isEmpty() ||
            textFieldMail.getText().trim().isEmpty() ||
            (!rdbCarte.isSelected() && !rdbPaypal.isSelected() && !rdbCheque.isSelected())) {
            
            // Affiche un message d'erreur si un champ est vide ou pas de paiement sélectionné
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs et sélectionner un mode de paiement.",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;  // Tous les champs sont valides
    }

    // Crée un objet FactureClient avec les valeurs saisies dans le formulaire
    private FactureClient creerCommandeDepuisFormulaire() {
        // Récupère le mode de paiement sélectionné
        String paiement = rdbCarte.isSelected() ? "Carte de crédit"
                       : rdbPaypal.isSelected() ? "Paypal"
                       : rdbCheque.isSelected() ? "Chèque"
                       : "Non précisé";

        // Construction de la FactureClient avec toutes les données du formulaire et le panier actuel
        return new FactureClient(
                textFieldNom.getText(),
                textFieldPrenom.getText(),
                textFieldAdresse1.getText(),
                textFieldAdresse2.getText(),
                textFieldCP.getText(),
                textFieldVille.getText(),
                textFieldTel.getText(),
                textFieldMail.getText(),
                paiement,
                chckbxNewsLetter.isSelected(),
                panier
        );
    }
}
