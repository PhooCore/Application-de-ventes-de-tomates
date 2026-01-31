package modèle;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Panier représente le panier d’un client, contenant des tomates
 * et leur quantité associée. Elle permet d'ajouter, supprimer, recalculer
 * les articles, et de calculer le total de la commande.
 */
public class Panier {

    private List<Tomate> tomates;
    private List<Integer> quantites;
    private static final float EXPEDITION = 5.50f;

    /**
     * Constructeur de Panier. Initialise les listes de tomates et de quantités.
     */
    public Panier() {
        tomates = new ArrayList<>();
        quantites = new ArrayList<>();
    }

    /**
     * Retourne la liste des tomates présentes dans le panier.
     *
     * @return la liste des tomates
     */
    public List<Tomate> getTomates() {
        return tomates;
    }

    /**
     * Retourne la liste des quantités correspondantes aux tomates.
     *
     * @return la liste des quantités
     */
    public List<Integer> getQuantites() {
        return quantites;
    }

    /**
     * Ajoute une tomate au panier avec une certaine quantité.
     * Si la tomate est déjà présente, la quantité est cumulée sans dépasser le stock.
     * Le stock de la tomate est mis à jour en conséquence.
     *
     * @param tomate   la tomate à ajouter
     * @param quantite la quantité à ajouter
     */
    public void ajouterAuPanier(Tomate tomate, int quantite) {
        if (tomate != null && quantite > 0) {
            int stock = tomate.getStock();
            int index = tomates.indexOf(tomate);

            int quantiteActuelle = (index >= 0) ? quantites.get(index) : 0;
            int quantiteFinale = quantiteActuelle + quantite;

            if (quantiteFinale > stock) {
                quantiteFinale = stock;
            }

            if (index >= 0) {
                quantites.set(index, quantiteFinale);
            } else {
                tomates.add(tomate);
                quantites.add(quantiteFinale);
            }

            tomate.setStock(stock - quantite);
        }
    }

    /**
     * Supprime une tomate du panier.
     *
     * @param tomate la tomate à retirer du panier
     */
    public void supprimerArticle(Tomate tomate) {
        int index = tomates.indexOf(tomate);
        if (index >= 0) {
            tomates.remove(index);
            quantites.remove(index);
        }
    }

    /**
     * Supprime toutes les tomates du panier dont le stock est tombé à zéro.
     */
    public void recalculerPanier() {
        for (int i = tomates.size() - 1; i >= 0; i--) {
            if (tomates.get(i).getStock() == 0) {
                tomates.remove(i);
                quantites.remove(i);
            }
        }
    }

    /**
     * Calcule le sous-total (hors frais d'expédition) du panier.
     *
     * @return le montant total des articles
     */
    public float getSousTotal() {
        float sousTotal = 0.0f;
        for (int i = 0; i < tomates.size(); i++) {
            sousTotal += tomates.get(i).getPrixTTC() * quantites.get(i);
        }
        return sousTotal;
    }

    /**
     * Calcule le total TTC en ajoutant les frais d'expédition,
     * sauf si le panier est vide.
     *
     * @return le montant total TTC
     */
    public float getTotal() {
        if (tomates.isEmpty()) {
            return 0.0f; // Pas de frais si panier vide
        }
        return getSousTotal() + EXPEDITION;
    }

    /**
     * Vide complètement le panier.
     */
    public void reinitialiserPanier() {
        tomates.clear();
        quantites.clear();
    }
}
