package ihm;

import modèle.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors; //La méthode .stream() en Java sert à créer un flux à partir d’une collection

/**
 * L'application "TomaT'es BEELLLE".
 * Cette interface permet de visualiser les tomates disponibles,
 * de les filtrer par type et couleur, et d'accéder au panier ou à la description d'une tomate.
 */
public class Accueil extends JFrame {
    private static final long serialVersionUID = 1L;

    // Composants de l'IHM
    private JPanel panneauContenu;
    private JComboBox<String> comboType;     // Filtre de type de tomate
    private JComboBox<String> comboCouleur;  // Filtre de couleur
    private JList<String> listeTomates;      // Liste affichant les tomates filtrées
    private JButton boutonPanier;            // Bouton pour accéder au panier


    private List<Tomate> tomatesDisponibles; // Liste complète des tomates
    private List<Tomate> tomatesFiltrees;    // Liste filtrée selon les critères sélectionnés
    private Tomates baseDeTomates;           // Objet contenant toutes les tomates
    private final Panier panier = new Panier(); // Instance du panier

    /**
     * Constructeur de la fenêtre principale.
     * Initialise les données, construit l'interface et met en place les listeners.
     */
    public Accueil() {
        setTitle("TomaT'es BEELLLE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 714, 449);

        // Chargement des données depuis un fichier JSON
        baseDeTomates = chargerTomatesDepuisFichier("src/main/resources/data/tomates.json");
        tomatesDisponibles = baseDeTomates.getTomates();

        construireInterface(); // Construction des éléments graphiques
        listeListener();       // Ajout des écouteurs d’événements

        mettreAJourListe(tomatesDisponibles); // Affichage initial de toutes les tomates
    }

    /** Crée et organise les différents composants de la fenêtre */
    private void construireInterface() {
        panneauContenu = new JPanel(new BorderLayout(0, 0));
        panneauContenu.setBackground(new Color(255, 204, 204));
        panneauContenu.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panneauContenu);

        construireEnTete();      // Haut de la fenêtre : titre + filtres
        construireZoneListe();   // Centre de la fenêtre : liste des tomates
        construirePiedPage();    // Bas de la fenêtre : bouton panier
    }

    /** Crée la zone d'en-tête contenant le titre et les filtres */
    private void construireEnTete() {
        JPanel entete = new JPanel(new BorderLayout());
        entete.setBackground(new Color(255, 204, 204));
        panneauContenu.add(entete, BorderLayout.NORTH);

        // Titre avec image
        JLabel titre = new JLabel("TomaT'es BEELLLE", SwingConstants.CENTER);
        titre.setForeground(new Color(255, 51, 51));
        titre.setFont(new Font("Freestyle Script", Font.BOLD, 30));
        //Charger l'icône de notre si belle tomate
        String cheminImage = "/images/img_icones/IMG_6543.png";
        ImageIcon image = new ImageIcon(getClass().getResource(cheminImage));
        titre.setIcon(image);

        titre.setHorizontalTextPosition(SwingConstants.RIGHT);
        entete.add(titre, BorderLayout.NORTH);


        // Panneau de filtres
        JPanel panneauFiltres = new JPanel();
        panneauFiltres.setBackground(new Color(255, 204, 204));
        panneauFiltres.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.RED, 2),
            "Filtres", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Tahoma", Font.BOLD, 15), Color.RED
        ));

        // ComboBox pour filtrer par type
        comboType = new JComboBox<>();
        comboType.addItem("Toutes les tomates");
        for (TypeTomate type : TypeTomate.values()) {
            comboType.addItem(type.getDénomination());
        }
        comboType.setForeground(new Color(255, 102, 102));
        comboType.setBackground(Color.WHITE);
        panneauFiltres.add(comboType);

        // ComboBox pour filtrer par couleur
        comboCouleur = new JComboBox<>();
        comboCouleur.addItem("Toutes les couleurs");
        for (Couleur couleur : Couleur.values()) {
            comboCouleur.addItem(couleur.getDénomination());
        }
        comboCouleur.setForeground(new Color(255, 102, 102));
        comboCouleur.setBackground(Color.WHITE);
        panneauFiltres.add(comboCouleur);

        entete.add(panneauFiltres, BorderLayout.CENTER);
    }

    /** Crée la zone centrale avec la liste des tomates */
    private void construireZoneListe() {
        listeTomates = new JList<>();
        listeTomates.setForeground(new Color(255, 153, 153));
        JScrollPane defilement = new JScrollPane(listeTomates);
        panneauContenu.add(defilement, BorderLayout.CENTER);
    }

    /** Crée le bas de page avec le bouton "PANIER" */
    private void construirePiedPage() {
        JPanel pied = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pied.setBackground(new Color(255, 204, 204));
        boutonPanier = new JButton("PANIER : vide");
        boutonPanier.setForeground(new Color(255, 102, 102));
        boutonPanier.setBackground(Color.WHITE);
        String cheminImage = "/images/img_icones/sac-en-papier.png";
        ImageIcon image = new ImageIcon(getClass().getResource(cheminImage));
        boutonPanier.setIcon(image);
        boutonPanier.setFont(new Font("Tahoma", Font.BOLD, 20));
        pied.add(boutonPanier);
        panneauContenu.add(pied, BorderLayout.SOUTH);
    }

    /** Ajoute tous les listeners d’événements (comboBox, clics, bouton) */
    private void listeListener() {
        // Mise à jour de la liste quand on change un filtre
        comboType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrerEtMettreAJour();
            }
        });

        comboCouleur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrerEtMettreAJour();
            }
        });

        // Ouvrir la page du panier
        boutonPanier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PagePanier page = new PagePanier(panier, baseDeTomates);
                page.setVisible(true);
            }
        });

        // Double-clic sur une tomate → ouvre la fenêtre de description
        listeTomates.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listeTomates.locationToIndex(e.getPoint());
                    if (index >= 0 && index < tomatesFiltrees.size()) {
                        Tomate tomate = tomatesFiltrees.get(index);
                        Description description = new Description(tomate, panier, Accueil.this, tomatesDisponibles);
                        description.setVisible(true);
                    }
                }
            }
        });
    }


    /** Charge les tomates depuis un fichier JSON via la classe outil */
    private Tomates chargerTomatesDepuisFichier(String cheminFichier) {
        return OutilsBaseDonneesTomates.générationBaseDeTomates(cheminFichier);
    }

    /** Applique les filtres sélectionnés dans les comboBox */
    private void filtrerEtMettreAJour() {
        // Récupération du type sélectionné dans le comboBox type
        String typeChoisi = (String) comboType.getSelectedItem();

        // Récupération de la couleur sélectionnée dans le comboBox couleur
        String couleurChoisie = (String) comboCouleur.getSelectedItem();

        // Création d'un prédicat (condition de filtrage) avec une classe anonyme
        java.util.function.Predicate<Tomate> filtre = new java.util.function.Predicate<Tomate>() {
            @Override
            public boolean test(Tomate tomate) {
                // Vérifie si le type de la tomate correspond au filtre choisi,
                // ou si l'utilisateur a sélectionné "Toutes les tomates"
                boolean typeCorrespond = typeChoisi.equals("Toutes les tomates") ||
                                         tomate.getType().getDénomination().equals(typeChoisi);

                // Vérifie si la couleur de la tomate correspond au filtre choisi,
                // ou si l'utilisateur a sélectionné "Toutes les couleurs"
                boolean couleurCorrespond = couleurChoisie.equals("Toutes les couleurs") ||
                                            tomate.getCouleur().getDénomination().equals(couleurChoisie);

                // La tomate doit correspondre à la fois au type et à la couleur
                return typeCorrespond && couleurCorrespond;
            }
        };

        // Application du filtre sur la liste des tomates disponibles,
        // puis collecte des résultats dans une nouvelle liste
        tomatesFiltrees = tomatesDisponibles.stream()
                                           .filter(filtre)
                                           .collect(Collectors.toList());

        // Mise à jour de la JList avec la nouvelle liste filtrée
        mettreAJourListe(tomatesFiltrees);
    }



    /** Met à jour le contenu affiché dans la JList selon la liste donnée */
    private void mettreAJourListe(List<Tomate> liste) {
        // On stocke la nouvelle liste de tomates filtrées dans l'attribut tomatesFiltrees
        tomatesFiltrees = liste;

        // On crée un tableau de String contenant les désignations des tomates,
        // en utilisant une lambda plus simple et une référence de méthode
        String[] noms = liste.stream()
                             .map(Tomate::getDésignation)
                             .toArray(String[]::new);

        // On met à jour les données affichées dans la JList avec ce tableau de noms
        listeTomates.setListData(noms);
    }




    /** Met à jour le texte du bouton "PANIER" selon le total actuel */
    public void mettreAJourTextePanier() {
        // Récupère le total actuel du panier (prix total des articles ajoutés)
        float total = panier.getTotal();

        // Si le total est supérieur à zéro, on affiche le montant avec deux décimales
        if (total > 0) {
            // Met à jour le texte du bouton avec le montant formaté en euros (exemple : "PANIER : 15.50 €")
            boutonPanier.setText(String.format("PANIER : %.2f €", total));
        } else {
            // Si le panier est vide (total = 0), on affiche simplement "PANIER : vide"
            boutonPanier.setText("PANIER : vide");
        }
    }


    /** Méthode principale : lance l'application */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Accueil fenetre = new Accueil();
                    fenetre.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
