package modèle;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FactureClientTest {

    private Panier panier;
    @Before
    public void setUp() throws Exception {
        this.panier = new Panier();
    }

    @After
    public void tearDown() throws Exception {
        this.panier = null;
    }

    @Test
    public void testAjouterArticle() {
        Tomate t = new Tomate(TypeTomate.TOMATES_CERISES, Couleur.ROUGE, "Tomate cerise", " ", "Tomate-voyageur-TEMP", "C'est une tomate cerise.", 3, 5, 100);
        this.panier.ajouterAuPanier(t, 2);
        assertTrue(this.panier.getTomates().contains(t));
        assertEquals(t.getStock(), 1);
    }

    @Test
    public void testSupprimerPanier() {
        Tomate t = new Tomate(TypeTomate.TOMATES_CERISES, Couleur.ROUGE, "Tomate cerise", " ", "Tomate-voyageur-TEMP", "C'est une tomate cerise.", 3, 5, 100);
        this.panier.ajouterAuPanier(t, 2);
        assertTrue(this.panier.getTomates().contains(t));
        this.panier.supprimerArticle(t);
        assertFalse(this.panier.getTomates().contains(t));
    }

    @Test
    public void testRecalculerPanier() {
        Tomate t = new Tomate(TypeTomate.TOMATES_CERISES, Couleur.ROUGE, "Tomate cerise", " ", "Tomate-voyageur-TEMP", "C'est une tomate cerise.", 3, 5, 100);
        this.panier.ajouterAuPanier(t, 2);
        this.panier.supprimerArticle(t);
        this.panier.recalculerPanier();
        assertEquals(0.0f, this.panier.getSousTotal(), 0.0f);
    }
}