package ihm;

import modèle.Panier;
import modèle.Tomate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fenêtre affichant la description détaillée d'une tomate sélectionnée,
 * avec son image, description, prix, stock, et options d'ajout au panier.
 */
public class Description extends JFrame {

    private static final long serialVersionUID = 1L;

    // Constantes de style utilisées pour la cohérence visuelle
    private static final Color COULEUR_FOND = new Color(255, 204, 204);
    private static final Color COULEUR_ACCENT = new Color(153, 51, 51);
    
    public Description(Tomate tomate, Panier panier, Accueil accueil, List<Tomate> listeCompleteTomates) {
        setTitle(tomate.getDésignation()); // Titre fenêtre = nom de la tomate
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fermeture simple sans quitter l'app
        setSize(700, 400);
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran
        setMinimumSize(new Dimension(600, 350)); // Taille minimale

        // Panneau principal avec BorderLayout et marges
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(COULEUR_FOND);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Label titre en haut avec style
        JLabel labelNomTomate = new JLabel(tomate.getDésignation());
        labelNomTomate.setForeground(COULEUR_ACCENT);
        labelNomTomate.setFont(new Font("Tahoma", Font.BOLD, 22));
        contentPane.add(labelNomTomate, BorderLayout.NORTH);

        // Panneau central avec deux colonnes : image + stock à gauche, description à droite
        JPanel panelCentre = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCentre.setBackground(new Color(255, 153, 153));
        panelCentre.add(creerPanelGauche(tomate));  // Image + stock
        panelCentre.add(creerPanelDroite(tomate));  // Description + prix
        contentPane.add(panelCentre, BorderLayout.CENTER);

        // En fonction du stock, affichage différent en bas
        if (tomate.getStock() == 0) {
            // Si rupture, affiche suggestions de tomates alternatives
            contentPane.add(creerPanelRuptureStock(tomate, panier, accueil, listeCompleteTomates), BorderLayout.SOUTH);
        } else {
            // Sinon affiche les boutons pour ajouter au panier ou continuer
            contentPane.add(creerPanelBoutons(tomate, panier, accueil), BorderLayout.SOUTH);
        }
    }

    /**
     * Crée le panneau gauche contenant l'image de la tomate et le stock.
     */
    private JPanel creerPanelGauche(Tomate tomate) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COULEUR_FOND);

        JLabel labelImage = new JLabel("", SwingConstants.CENTER);
        try {
            // Chargement de l'image depuis ressources
            String cheminImage = "/images/Tomates200x200/" + tomate.getNomImage() + ".jpg";
            ImageIcon image = new ImageIcon(getClass().getResource(cheminImage));
            labelImage.setIcon(image);
        } catch (Exception e) {
            // Si image introuvable, affiche un message texte
            labelImage.setText("Image non trouvée");
        }

        // Label pour afficher le stock restant
        JLabel labelStock = new JLabel("Stock : " + tomate.getStock() + " paquets", SwingConstants.CENTER);
        labelStock.setForeground(new Color(0, 153, 0)); // Vert pour stock dispo

        panel.add(labelImage, BorderLayout.CENTER);
        panel.add(labelStock, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Crée le panneau droit contenant la description et le prix de la tomate.
     */
    private JPanel creerPanelDroite(Tomate tomate) {
        JPanel panel = new JPanel();
        panel.setBackground(COULEUR_FOND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        // Titre "Description"
        JLabel labelDescription = new JLabel("Description");
        labelDescription.setForeground(COULEUR_ACCENT);
        labelDescription.setFont(new Font("Tahoma", Font.BOLD, 16));
        labelDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Zone texte non éditable pour la description, avec scroll
        JTextPane textPane = new JTextPane();
        textPane.setForeground(new Color(255, 102, 102)); // couleur rose clair
        textPane.setText(tomate.getDescription());
        textPane.setEditable(false);
        textPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textPane.setCaretPosition(0); // afficher le début du texte

        JScrollPane scrollDescription = new JScrollPane(textPane);
        scrollDescription.setPreferredSize(new Dimension(100, 120));
        scrollDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panneau affichant le prix, aligné à gauche
        JPanel panelPrix = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelPrix.setBackground(COULEUR_FOND);
        JLabel labelPrix = new JLabel("Prix :");
        labelPrix.setForeground(COULEUR_ACCENT);
        labelPrix.setFont(new Font("Tahoma", Font.PLAIN, 15));
        JLabel labelPrixValeur = new JLabel(String.format("%.2f €", tomate.getPrixTTC()));
        labelPrixValeur.setForeground(COULEUR_ACCENT);
        labelPrixValeur.setFont(new Font("Tahoma", Font.BOLD, 15));
        panelPrix.add(labelPrix);
        panelPrix.add(labelPrixValeur);
        panelPrix.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Assemblage final
        panel.add(labelDescription);
        panel.add(scrollDescription);
        panel.add(panelPrix);

        return panel;
    }

    /**
     * Crée le panneau affiché lorsque la tomate est en rupture de stock.
     * Propose des suggestions alternatives parmi la liste complète.
     */
    private JPanel creerPanelRuptureStock(Tomate tomate, Panier panier, Accueil accueil, List<Tomate> listeCompleteTomates) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COULEUR_FOND);

        // Label rupture
        JLabel labelRupture = new JLabel("En rupture de stock");
        labelRupture.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelRupture.setForeground(Color.BLACK);
        panel.add(labelRupture, BorderLayout.NORTH);

        // Préparation liste suggestions (max 5 tomates disponibles différentes)
        List<Tomate> suggestions = new ArrayList<>();
        for (Tomate t : listeCompleteTomates) {
            if (t.getStock() > 0 && !t.equals(tomate)) {
                suggestions.add(t);
            }
        }
        Collections.shuffle(suggestions); // mélanger pour varier

        List<Tomate> finalSuggestions = new ArrayList<>(suggestions.subList(0, Math.min(5, suggestions.size())));

        // ComboBox pour afficher les suggestions
        JComboBox<String> comboSuggestions = new JComboBox<>();
        for (Tomate t : finalSuggestions) {
            comboSuggestions.addItem(t.getDésignation() + " (stock : " + t.getStock() + ")");
        }
        panel.add(comboSuggestions, BorderLayout.CENTER);

        // Bouton pour voir la description d'une tomate sélectionnée dans les suggestions
        JButton boutonVoir = new JButton("Voir la sélection");
        boutonVoir.addActionListener(e -> {
            int idx = comboSuggestions.getSelectedIndex();
            if (idx >= 0 && idx < finalSuggestions.size()) {
                Tomate choix = finalSuggestions.get(idx);
                // Ouvre une nouvelle fenêtre Description pour la tomate choisie
                new Description(choix, panier, accueil, listeCompleteTomates).setVisible(true);
                dispose(); // ferme la fenêtre actuelle
            }
        });
        panel.add(boutonVoir, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crée le panneau des boutons d'action lorsque la tomate est en stock :
     * - sélection de quantité
     * - ajout au panier
     * - continuer les achats
     */
    private JPanel creerPanelBoutons(Tomate tomate, Panier panier, Accueil accueil) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setBackground(COULEUR_FOND);

        JLabel labelQuantite = new JLabel("Quantité : ");
        labelQuantite.setForeground(COULEUR_ACCENT);
        panel.add(labelQuantite);

        // Spinner pour choisir la quantité entre 1 et le stock disponible
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, tomate.getStock(), 1);
        JSpinner spinnerQuantite = new JSpinner(spinnerModel);
        spinnerQuantite.setPreferredSize(new Dimension(60, spinnerQuantite.getPreferredSize().height));
        panel.add(spinnerQuantite);

        // Bouton "Ajouter au panier"
        JButton boutonAjouter = new JButton("Ajouter au panier");
        boutonAjouter.addActionListener(e -> {
            int quantite = (Integer) spinnerQuantite.getValue();
            panier.ajouterAuPanier(tomate, quantite); // ajoute la quantité choisie au panier
            JOptionPane.showMessageDialog(this,
                    quantite + " sachet(s) de " + tomate.getDésignation() + " ajouté(s) au panier.",
                    "Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);
            // Mise à jour de l'affichage panier dans la fenêtre principale
            if (accueil != null) {
                accueil.mettreAJourTextePanier();
            }
            dispose(); // ferme la fenêtre description
        });
        panel.add(boutonAjouter);

        // Bouton "Continuer les achats" qui ferme simplement la fenêtre
        JButton boutonContinuer = new JButton("Continuer les achats");
        boutonContinuer.addActionListener(e -> dispose());
        panel.add(boutonContinuer);

        return panel;
    }
}
