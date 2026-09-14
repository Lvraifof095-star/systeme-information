# Système d'information intelligent de gestion des stocks

Squelette de projet correspondant aux chapitres 3 et 4 du mémoire :
backend Spring Boot (JDK 17), frontend Angular, base MySQL.

## 1. Base de données MySQL

Configurer MySQL pour écouter sur le port **4490** (fichier `my.ini` / `my.cnf`) :

```
[mysqld]
port=4490
```

Aucun script SQL à lancer manuellement : `spring.jpa.hibernate.ddl-auto=update`
dans `application.properties` crée et met à jour les tables automatiquement
au démarrage du backend, à partir des entités JPA.

Identifiants utilisés (développement uniquement) :
- utilisateur : `root`
- mot de passe : `12345`

## 2. Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

L'API démarre sur `http://localhost:8080`. Le préfixe de toutes les routes
est `/api` (ex. `/api/produits`, `/api/auth/login`, `/api/dashboard/kpi`).

Avant la première connexion, insérer manuellement (ou via un script
d'initialisation `data.sql`) au moins :
- une ligne dans `role` (ex. `ADMINISTRATEUR`, `OPERATEUR`)
- un utilisateur avec un mot de passe encodé en BCrypt

## 3. Frontend (Angular)

```bash
cd frontend
npm install
ng serve
```

L'application démarre sur `http://localhost:4200` et consomme l'API
définie dans `src/environments/environment.ts`.

## 4. Structure du projet

```
backend/
  entity/      -> classes JPA (Produit, Stock, MouvementStock, Alerte, ...)
  repository/  -> accès aux données (Spring Data JPA)
  service/     -> règles de gestion (mouvements, alertes, KPI)
  controller/  -> API REST
  security/    -> Spring Security + JWT
  dto/         -> objets de transfert (login, KPI, mouvement)

frontend/
  app/core/       -> authentification, intercepteur JWT, guard
  app/produits/   -> liste des produits
  app/dashboard/  -> tableau de bord décisionnel et KPI
  app/auth/login/ -> écran de connexion
```

## 5. Points à compléter avant la soutenance

- Écrans manquants : catégories, fournisseurs, saisie des mouvements,
  gestion des utilisateurs, page des alertes (les services et
  contrôleurs backend existent déjà pour tout cela).
- Graphiques du tableau de bord (ex. avec `ng2-charts` déjà ajouté au
  `package.json`) : évolution des mouvements, répartition par catégorie.
- Export PDF/Excel des rapports.
- Remplacer le mot de passe et la clé JWT en clair par des variables
  d'environnement avant tout déploiement réel.
