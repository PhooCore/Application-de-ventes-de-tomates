# 🍅 TomaT'es BEELLLE

Une application e-commerce en Java Swing pour la vente de graines de tomates. Cette application permet aux utilisateurs de parcourir un catalogue de variétés de tomates, de les filtrer par type et couleur, et de passer commande.

## Table des matières

* [Présentation du projet](#présentation-du-projet)

  * [Contexte pédagogique](#contexte-pédagogique)
  * [Choix du sujet](#choix-du-sujet)
* [Fonctionnalités](#fonctionnalités)

  * [Pour l'utilisateur](#pour-lutilisateur)
  * [Données](#données)
* [Architecture](#architecture)

  * [Package `modèle`](#package-modèle)
  * [Package `ihm`](#package-ihm)
* [Utilisation](#utilisation)

  * [Parcours utilisateur](#parcours-utilisateur)
* [Tests](#tests)
* [Technologies utilisées](#technologies-utilisées)
* [Charte graphique](#charte-graphique)
* [Format des données](#format-des-données)

  * [Structure JSON (tomates.json)](#structure-json-tomatesjson)
* [Auteurs](#auteurs)
* [Licence](#licence)

## Présentation du projet

Ce projet a été réalisé durant la **1ʳᵉ année de BUT Informatique**, en groupe de 4, dans le cadre de la **SAÉ 2.01 : Développement d'une application**.

### Contexte pédagogique

L'objectif de cette SAÉ était de mettre en pratique les compétences acquises en programmation orientée objet, en conception d'interfaces graphiques et en gestion de projet informatique. Le projet nous a permis de :

- **Concevoir une architecture logicielle** structurée (pattern MVC)
- **Développer une interface graphique** complète avec Java Swing
- **Manipuler des données** persistantes au format JSON
- **Travailler en équipe** selon une méthodologie de développement
- **Appliquer les bonnes pratiques** de programmation et de documentation
- **Mettre en place des tests unitaires** avec JUnit

### Choix du sujet

Le thème de la vente de graines de tomates a été choisi pour son côté ludique et original, tout en permettant d'implémenter toutes les fonctionnalités requises d'une application e-commerce complète : catalogue produits, système de filtrage, gestion de panier, processus de commande et facturation.

## Fonctionnalités

### Pour l'utilisateur

- **Navigation du catalogue** : Visualisation de toutes les variétés de tomates disponibles
- **Filtrage avancé** : Filtres par type (cerises/cocktails ou autres tomates) et par couleur (rouge, orange, jaune, vert, bleu, noir, multicolore)
- **Détails des produits** : Affichage détaillé de chaque tomate avec image, description, prix et stock disponible
- **Suggestions intelligentes** : Recommandations de tomates similaires en cas de rupture de stock
- **Gestion du panier** : 
  - Ajout/modification/suppression d'articles
  - Calcul automatique des totaux
  - Frais de livraison (5,50 €)
- **Processus de commande complet** :
  - Saisie des coordonnées client
  - Choix du mode de paiement (Carte, PayPal, Chèque)
  - Option d'abonnement à la newsletter
  - Génération de facture HTML
  - Impression de la facture

### Données

- **63 variétés de tomates** différentes
- **Tomates apparentées** : Chaque tomate a 4 suggestions de variétés similaires
- **Persistance des données** : Sauvegarde automatique des stocks après validation de commande

## Architecture

L'application suit une architecture MVC (Modèle-Vue-Contrôleur) :

### Package `modèle`

Contient les classes métier de l'application :

- **Tomate** : Représente une variété de tomate avec ses caractéristiques
- **Tomates** : Collection de tomates avec méthodes de filtrage
- **Panier** : Gestion du panier d'achat
- **FactureClient** : Informations client et génération de facture
- **TypeTomate** : Énumération des types (TOMATES, TOMATES_CERISES)
- **Couleur** : Énumération des couleurs disponibles
- **OutilsBaseDonneesTomates** : Utilitaires pour charger/sauvegarder les données JSON

### Package `ihm`

Contient les interfaces graphiques :

- **Accueil** : Fenêtre principale avec liste et filtres
- **Description** : Détails d'une tomate et ajout au panier
- **PagePanier** : Visualisation et modification du panier
- **Coordonnées** : Saisie des informations client
- **Facture** : Affichage et impression de la facture


## Utilisation

### Parcours utilisateur

1. **Page d'accueil** : 
   - Utilisez les filtres pour affiner votre recherche
   - Double-cliquez sur une tomate pour voir ses détails

2. **Page de description** :
   - Consultez la description, le prix et le stock
   - Sélectionnez la quantité désirée
   - Ajoutez au panier ou continuez vos achats
   - En cas de rupture, consultez les suggestions

3. **Panier** :
   - Modifiez les quantités directement dans le tableau
   - Vérifiez le sous-total et le total avec livraison
   - Validez ou continuez vos achats

4. **Coordonnées** :
   - Remplissez tous les champs obligatoires
   - Choisissez votre mode de paiement
   - Option newsletter

5. **Facture** :
   - Vérifiez votre commande
   - Imprimez la facture si nécessaire
   - Validez pour confirmer (sauvegarde automatique des stocks)

## Tests

- **TomatesTest** : Teste le chargement, la récupération et le filtrage des tomates
- **PanierTest** : Teste l'ajout, la suppression et le recalcul du panier
- **FactureClientTest** : Teste la gestion de la facture client
- **TomatesApparenteesTest** : Teste l'ajout de tomates apparentées

## Technologies utilisées

- **Java Swing** : Interface graphique
- **JSON** : Format de stockage des données
- **Maven** : Gestion des dépendances et build
- **JUnit 4** : Tests unitaires
- **org.json** : Bibliothèque de manipulation JSON

## Charte graphique

L'application utilise une palette de couleurs rose/rouge pour une ambiance "tomate" :

- Fond principal : `RGB(255, 204, 204)`
- Accent rouge : `RGB(153, 51, 51)` / `RGB(255, 51, 51)`
- Texte clair : `RGB(255, 102, 102)` / `RGB(255, 153, 153)`

## Format des données

### Structure JSON (tomates.json)

```json
[
  {
    "type": "Autres Tomates (47)",
    "couleur": "Orange",
    "désignation": "Tomate Russian Persimmon",
    "sousTitre": "Variété Russe",
    "nomImage": "Tomate-Russian-Persimmon",
    "description": "Description de la tomate...",
    "stock": 10,
    "nbGrainesParSachet": 15,
    "prixTTC": 4.95
  }
]
```

## Auteurs

* **NGUYEN Phuong** | [GitHub-PhooCore](https://github.com/PhooCore)
* **CORBILLÉ Iris**  | [GitHub-iriscrbl](https://github.com/iriscrbl)
* **AHMAD FAISAL Aneesa**
* **MUNKH ERDENE Dulguun**

## Licence

Projet académique réalisé à des fins pédagogiques.

⭐ *Si ce projet vous a été utile, n’hésitez pas à lui donner une étoile !*

---

**Bon jardinage avec TomaT'es BEELLLE ! 🍅**
