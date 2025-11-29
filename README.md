# gestion-cours-en-ligne

# 1. ⚙️ Technologies et Architecture
   
# Composant : Contrôleur
Rôle Principal : Gestion des requêtes (Routing)
Technologie(s) : Jakarta Servlets (@WebServlet)

# Composant : Vue
Rôle Principal : Affichage de l'interface utilisateur
Technologie(s) : JSP (JavaServer Pages) & JSTL

# Composant : Modèle
Rôle Principal : Accès et Manipulation des données (DAO)
Technologie(s) : JDBC, PostgreSQL

# Composant : Sécurité
Rôle Principal : Filtrage et Autorisation
Technologie(s) : Jakarta Filters (@WebFilter)

# Composant : Build Tool
Rôle Principal : Gestion des dépendances
Technologie : Maven

# 2. Fonctionnalités Implémentées
Le système supporte deux rôles principaux : Professeur (PROF) et Étudiant (ETUDIANT).

 Sécurité et Accès
 Authentification : Les utilisateurs se connectent via /login. Le mot de passe et l'email sont vérifiés dans la base de données.

 Protection des Routes : Le AuthFilter protège l'accès aux ressources /cours et /utilisateurs si l'utilisateur n'a pas de session active.

 Ouverture d'Inscription : L'accès au formulaire de création d'un nouvel utilisateur (GET /utilisateurs?action=new) et sa soumission (POST /utilisateurs) est public pour permettre l'inscription initiale.

 Gestion des Cours
CRUD pour les Cours :

Le rôle PROF a un accès complet aux opérations de Création, Lecture, Mise à jour et Suppression (CUD) des cours.

Le rôle ETUDIANT a uniquement un accès en Lecture (Read).

# Inscription aux Cours :

Les étudiants peuvent s'inscrire aux cours disponibles via l'action /cours?action=enroll.
Le système gère la relation N-à-N (inscription) entre utilisateur et cours.

# Gestion des Utilisateurs
Gestion des Comptes : Création initiale de comptes (PROF/ETUDIANT).

Autorisation : Seuls les utilisateurs avec le rôle PROF sont autorisés à accéder aux fonctions d'administration des utilisateurs (liste, suppression).

# URLS
Connexion : http://localhost:8080/gestion-cours-en-ligne/login
Lister les cours : http://localhost:8080/gestion-cours-en-ligne/cours
Ajouter un cours : http://localhost:8080/gestion-cours-en-ligne/cours?action=new
Ajouter un utilisateur : http://localhost:8080/gestion-cours-en-ligne/utilisateurs?action=new

# 3. Structure de la Base de Données
Les relations N-à-N entre utilisateur et cours sont gérées par la table de liaison inscription.

# Schéma des Tables
SQL

# -- Table Utilisateur
CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('PROF', 'ETUDIANT')) NOT NULL
);

# -- Table Cours
CREATE TABLE cours (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    description TEXT,
    niveau VARCHAR(50),
    duree_heures INTEGER
);

# -- Table de Liaison Inscription
CREATE TABLE inscription (
    utilisateur_id INTEGER NOT NULL,
    cours_id INTEGER NOT NULL,
    date_inscription TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    PRIMARY KEY (utilisateur_id, cours_id)
);
