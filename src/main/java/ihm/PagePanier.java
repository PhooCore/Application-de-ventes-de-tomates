package ihm;

import modèle.Panier;
import modèle.Tomate;
import modèle.Tomates;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PagePanier extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable table;                  // Tableau affichant le contenu du panier
    private DefaultTableModel modeleTable; // Modèle de données du tableau
    private JLabel labelSousTotal;        // Label affichant le sous-total
    private JLabel labelTotal;            // Label affichant le total (avec frais de livraison)
    private JButton btnValider;           // Bouton pour valider le panier / passer commande

    private final Panier panier;          // Panier courant (avec tomates et quantités)
    private final Tomates baseTomates;   // Base de données de toutes les tomates disponibles

    // Constructeur : initialisation de la fenêtre avec le panier et la base de tomates
    public PagePanier(Panier panier, Tomates baseTomates) {
        super("Mon Panier");
        getContentPane().setBackground(new Color(255, 204, 204)); // Couleur de fond rose clair

        this.panier = panier;
        this.baseTomates = baseTomates;

        pagePrincipale();    // Construction de l'interface principale
        chargerContenu();    // Chargement des données du panier dans le tableau
        ajouterListenerQuantite(); // Active le listener pour mise à jour des quantités
        recalculer();        // Calcul des totaux et mise à jour des labels

        setVisible(true);    // Affichage de la fenêtre
    }

    // Création de la structure principale de la fenêtre
    private void pagePrincipale() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fermer cette fenêtre sans quitter l'application
        setSize(700, 450);       // Taille de la fenêtre
        setLocationRelativeTo(null); // Centrer la fenêtre à l'écran

        // Création du modèle de table avec colonnes : Image, Nom, Prix unitaire, Quantité, Total
        modeleTable = new DefaultTableModel(
            new Object[]{"Image", "Nom", "Prix unitaire", "Quantité", "Total"}, 0) {

            private static final long serialVersionUID = 1L;

            // Seule la colonne Quantité (index 3) est modifiable
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }

            // Indiquer que la colonne 0 contient des icônes (images)
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? Icon.class : Object.class;
            }
        };

        // Création du JTable avec le modèle défini
        table = new JTable(modeleTable);
        table.setForeground(new Color(102, 51, 51));  // Couleur du texte marron foncé
        table.setBackground(new Color(255, 204, 204)); // Couleur de fond rose clair
        table.setRowHeight(50); // Hauteur des lignes pour bien afficher les images

        // Ajout du tableau dans une zone défilante
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Création d'un panneau en bas pour afficher les totaux et les boutons
        JPanel basPanel = new JPanel(new BorderLayout());
        basPanel.setBackground(new Color(255, 204, 204));
        basPanel.add(creerPanelTotaux(), BorderLayout.CENTER);   // Labels des totaux au centre
        basPanel.add(creerPanelBoutons(), BorderLayout.EAST);    // Boutons à droite

        getContentPane().add(basPanel, BorderLayout.SOUTH);
    }

    // Création du panneau affichant les sous-total et total
    private JPanel creerPanelTotaux() {
        labelSousTotal = new JLabel();  // Label pour le sous-total
        labelSousTotal.setBackground(new Color(240, 240, 240));
        labelSousTotal.setForeground(new Color(204, 0, 0));      // Texte en rouge foncé

        labelTotal = new JLabel();       // Label pour le total (avec frais de livraison)
        labelTotal.setForeground(new Color(204, 0, 0));         // Texte en rouge foncé

        JPanel panel = new JPanel(new GridLayout(2, 1)); // Disposition verticale (2 lignes)
        panel.setBackground(new Color(255, 204, 204));
        panel.add(labelSousTotal);
        panel.add(labelTotal);

        return panel;
    }

    // Création du panneau contenant les boutons "Continuer", "Vider", "Valider"
    private JPanel creerPanelBoutons() {
        JButton btnVider = new JButton("Vider le panier");
        btnVider.addActionListener(ev -> viderPanier());  // Action pour vider le panier

        btnValider = new JButton("Valider le panier");
        // Ouvre la fenêtre de saisie des coordonnées pour valider la commande
        btnValider.addActionListener(ev -> {
            new Coordonnées(panier, baseTomates).setVisible(true);
            dispose();  // Ferme la fenêtre panier
        });

        JButton btnContinuer = new JButton("Continuer les achats");
        btnContinuer.addActionListener(ev -> dispose());  // Ferme la fenêtre panier pour revenir aux achats

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alignement à droite
        panel.setForeground(Color.WHITE);
        panel.setBackground(new Color(255, 204, 204));
        panel.add(btnContinuer);
        panel.add(btnVider);
        panel.add(btnValider);

        return panel;
    }

    // Chargement du contenu du panier dans le tableau
    private void chargerContenu() {
        modeleTable.setRowCount(0); // Vide le tableau avant d'ajouter les lignes

        // Pour chaque tomate du panier, on ajoute une ligne avec l'image, nom, prix, quantité et total
        for (int i = 0; i < panier.getTomates().size(); i++) {
            Tomate t = panier.getTomates().get(i);
            int qte = panier.getQuantites().get(i);

            // Chargement de l'image de la tomate à partir du dossier images
            ImageIcon icon = new ImageIcon("src/main/resources/images/Tomates40x40/" + t.getNomImage() + ".jpg");

            // Ajout de la ligne dans le modèle
            modeleTable.addRow(new Object[]{
                icon,
                t.getDésignation(),
                String.format("%.2f €", t.getPrixTTC()),
                qte,
                String.format("%.2f €", t.getPrixTTC() * qte)
            });
        }

        // Mise à jour de l'état (activation/désactivation) du bouton valider
        majEtatBoutonValider();
    }

    // Ajoute un listener qui met à jour la quantité et les totaux quand on modifie la colonne Quantité
    private void ajouterListenerQuantite() {
        modeleTable.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 3) {
                int row = e.getFirstRow();
                Object value = modeleTable.getValueAt(row, 3);
                int nouvelleQte = 1;
                try {
                    nouvelleQte = Integer.parseInt(value.toString());
                    if (nouvelleQte < 1) {
                        nouvelleQte = 1;
                        modeleTable.setValueAt(nouvelleQte, row, 3);
                    }
                    panier.getQuantites().set(row, nouvelleQte);
                    Tomate t = panier.getTomates().get(row);
                    modeleTable.setValueAt(String.format("%.2f €", t.getPrixTTC() * nouvelleQte), row, 4);
                    recalculer();
                } catch (NumberFormatException ex) {
                    modeleTable.setValueAt(panier.getQuantites().get(row), row, 3);
                }
            }
        });
    }

    // Vide complètement le panier, efface le tableau et recalcule les totaux
    private void viderPanier() {
        panier.reinitialiserPanier();
        modeleTable.setRowCount(0);
        recalculer();
    }

    // Recalcule et met à jour les labels du sous-total et total avec expédition
    private void recalculer() {
        labelSousTotal.setText(String.format("Sous-total : %.2f €", panier.getSousTotal()));
        labelTotal.setText(String.format("Total (avec expédition) : %.2f €", panier.getTotal()));
        majEtatBoutonValider();
    }

    // Active ou désactive le bouton valider en fonction du contenu du panier
    private void majEtatBoutonValider() {
        btnValider.setEnabled(!panier.getTomates().isEmpty());
    }
}
