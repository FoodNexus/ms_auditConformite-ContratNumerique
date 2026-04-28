📝 Description

Le microservice Audit Conformité & Contrat Numérique est un composant central de la plateforme FoodNexus. Il a pour but de garantir la qualité et la traçabilité des produits alimentaires à travers un processus d'audit rigoureux assisté par l'Intelligence Artificielle.

Ce service gère l'inspection des produits alimentaires, détermine leur verdict sanitaire, gère leur destination (recyclage ou consommation), et automatise la génération de contrats numériques pour les dons alimentaires.

✨ Fonctionnalités Principales

🔍 Inspection Intelligente (AI-Powered) : Analyse automatisée d'images de produits alimentaires à l'aide d'un modele du deep learning pour générer un verdict sanitaire (PROPRE_A_LA_CONSOMMATION, DESTRUCTION_RECYCLAGE).

♻️ Gestion du Recyclage : Si un produit est jugé impropre, le service automatise la création d'un log de recyclage (Destinations: COMPOST ou AGRICULTEUR). 📄 Contrats Numériques : Génération, suivi, et sécurisation de contrats numériques entre les donneurs (restaurants/supermarchés) et les récepteurs (associations/banques alimentaires).

📊 Statistiques & Tableaux de Bord : Calculs avancés (JPQL) pour fournir des KPIs complets aux auditeurs (répartition des verdicts, volumes recyclés, taux de conversion, etc.).

🔔 Notifications Intégrées : Service de suivi d'activité et de notification in-app pour informer les utilisateurs des changements d'états.

🔐 Sécurité Avancée : Sécurisation totale des API via OAuth2 / JWT via Keycloak.

🛠️ Technologies Utilisées

Backend Framework : Java 17, Spring Boot 3.5.x Base de données : MySQL 8 ORM : Spring Data JPA / Hibernate Sécurité : Spring Security, OAuth2 Resource Server (Keycloak) Cloud & Découverte : Spring Cloud Netflix Eureka Client Intelligence Artificielle : CNN Deep Learning Qualité de code & Tests : JUnit 5, Mockito, JaCoCo (Couverture > 77%), SonarQube Scanner

⚙️ Architecture & Dépendances

Ce microservice dépend de l'écosystème global FoodNexus :

Eureka Server : Accessible sur http://localhost:8761/eureka/ Keycloak Server : Accessible sur http://localhost:8180/realms/foodnexus Base de données MySQL : foodnexus_audit (port 3306)

🚀 Installation & Lancement Rapide

Prérequis JDK 17 Maven 3.8+ Serveur MySQL en cours d'exécution Serveurs Eureka et Keycloak fonctionnels (si vous testez les endpoints sécurisés)

Cloner le repository git clone https://github.com/votre-organisation/ms_auditConformite-ContratNumerique.git cd ms_auditConformite-ContratNumerique

Configuration (application.properties) Vérifiez ou modifiez le fichier src/main/resources/application.properties si nécessaire. La base de données foodnexus_audit sera créée automatiquement grâce au paramètre createDatabaseIfNotExist=true.

Compiler et lancer mvn clean install mvn spring-boot:run Le service démarrera par défaut sur le port 8083.

🧪 Tests & Qualité (SonarQube)

Ce projet maintient une couverture de test à 77%. Pour lancer les tests et générer le rapport JaCoCo :

mvn clean test jacoco:report

Pour envoyer l'analyse vers un serveur SonarQube local :

mvn clean verify sonar:sonar -D"sonar.projectKey=FoodNexus-Audit" -D"sonar.host.url=http://localhost:9000" -D"sonar.login=VOTRE_TOKEN"

🌐 Endpoints Principaux (API REST)

Toutes les routes nécessitent un token Bearer valide (Keycloak).

📁 Cas d'Inspection (/api/inspection-cases)

POST /scan : Analyser une image avec l'IA et créer un dossier d'inspection. (Nécessite le rôle AUDITOR) GET /auditor/{auditorId} : Récupérer toutes les inspections d'un auditeur spécifique. PATCH /{id}/verdict : Mettre à jour manuellement le verdict sanitaire. 📄 Contrats Numériques (/api/contracts)

POST / : Générer un nouveau contrat de don. (Nécessite le rôle ADMIN) GET / : Liste complète des contrats. PATCH /{id}/status : Mettre à jour l'état d'un contrat (GENERE, ENVOYE, ARCHIVE).

♻️ Recyclage (/api/recycling-products) GET /inspection-case/{caseId} : Obtenir les données de recyclage associées à une inspection rejetée.

📊 Statistiques (/api/audit/statistics) GET /?auditorId={id} : Récupérer toutes les métriques et KPIs d'un auditeur ou de la plateforme globale.
