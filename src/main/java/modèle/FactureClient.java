package modèle;

/**
 * La classe FactureClient représente une facture associée à un client.
 * Elle contient les informations personnelles du client ainsi que
 * le contenu de son panier.
 */
public class FactureClient {

    private String nom, prenom, adresse1, adresse2, codePostal, ville, telephone, email, modePaiement;
    private boolean newsletter;
    private Panier panier;

    /**
     * Constructeur pour initialiser une facture client complète.
     *
     * @param nom           le nom du client
     * @param prenom        le prénom du client
     * @param adresse1      l'adresse principale
     * @param adresse2      le complément d'adresse
     * @param codePostal    le code postal
     * @param ville         la ville
     * @param telephone     le numéro de téléphone
     * @param email         l'adresse email
     * @param modePaiement  le moyen de paiement
     * @param newsletter    l'abonnement à la newsletter
     * @param panier        le panier contenant les articles achetés
     */
    public FactureClient(String nom, String prenom, String adresse1, String adresse2, String codePostal,
                         String ville, String telephone, String email, String modePaiement,
                         boolean newsletter, Panier panier) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse1 = adresse1;
        this.adresse2 = adresse2;
        this.codePostal = codePostal;
        this.ville = ville;
        this.telephone = telephone;
        this.email = email;
        this.modePaiement = modePaiement;
        this.newsletter = newsletter;
        this.panier = panier;
    }

    /**
     * Génère le texte de la facture du client avec toutes les informations
     * personnelles et le détail du panier.
     *
     * @return la facture sous forme de chaîne de caractères
     */
    public String getFacture() {
        StringBuilder sb = new StringBuilder();

        sb.append("----- FACTURE CLIENT -----\n");
        sb.append("Nom : ").append(nom).append("\n");
        sb.append("Prénom : ").append(prenom).append("\n");
        sb.append("Adresse : ").append(adresse1);
        if (!adresse2.isEmpty()) sb.append(", ").append(adresse2);
        sb.append("\n");
        sb.append("Code Postal : ").append(codePostal).append("\n");
        sb.append("Ville : ").append(ville).append("\n");
        sb.append("Téléphone : ").append(telephone).append("\n");
        sb.append("Email : ").append(email).append("\n");
        sb.append("Paiement : ").append(modePaiement).append("\n");
        sb.append("Abonnement newsletter : ").append(newsletter ? "Oui" : "Non").append("\n\n");

        sb.append("----- CONTENU DU PANIER -----\n");
        for (int i = 0; i < panier.getTomates().size(); i++) {
            Tomate t = panier.getTomates().get(i);
            int qte = panier.getQuantites().get(i);
            sb.append("- ").append(t.getDésignation())
              .append(" x").append(qte)
              .append(" = ").append(String.format("%.2f €", t.getPrixTTC() * qte))
              .append("\n");
        }

        sb.append("\nSous-total : ").append(String.format("%.2f €", panier.getSousTotal())).append("\n");
        if (!panier.getTomates().isEmpty()) {
            sb.append("Frais d'expédition : 5.50 €\n");
        }
        sb.append("Total TTC : ").append(String.format("%.2f €", panier.getTotal())).append("\n");

        return sb.toString();
    }

    /**
     * Retourne le panier associé à la facture.
     *
     * @return le panier du client
     */
    public Panier getPanier() {
        return panier;
    }

    /**
     * Retourne le nom du client.
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le prénom du client.
     *
     * @return le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Retourne la première ligne d'adresse du client.
     *
     * @return l'adresse principale
     */
    public String getAdresse1() {
        return adresse1;
    }

    /**
     * Retourne le complément d'adresse.
     *
     * @return le complément d'adresse
     */
    public String getAdresse2() {
        return adresse2;
    }

    /**
     * Retourne le code postal du client.
     *
     * @return le code postal
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * Retourne la ville du client.
     *
     * @return la ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * Retourne le numéro de téléphone du client.
     *
     * @return le téléphone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Retourne l'adresse email du client.
     *
     * @return l'email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retourne le mode de paiement choisi par le client.
     *
     * @return le moyen de paiement
     */
    public String getMoyenPaiement() {
        return modePaiement;
    }

    /**
     * Indique si le client est abonné à la newsletter.
     *
     * @return true s'il est abonné, false sinon
     */
    public boolean isAbonnement() {
        return newsletter;
    }
}
