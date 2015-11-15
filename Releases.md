﻿#summary Planning des releases.
#labels Releases,Featured

# Versions rilizées #
Ces versions sont censées être stables. Vous pouvez les récupérer rapidement les `war` et archives sources depuis la [page de téléchargement](http://code.google.com/p/jplop/downloads/list). Vous pouvez également aller sur le [dépôt des releases](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop).

Si vous voulez vous aventurer dans le bizarre, vous pouvez essayer les [toutes dernières versions](http://tifauv.homeip.net/~jplop/repos/snapshots/tifauv/jplop). C'est en développement, à vos risques et périls. C'est vous qui voyez !

## JPlop 1.1.1 ##
Corrections:
  * Suppression de la dépendance sur wicket (vestige d'un essai reporté)
  * Suppression d'une méthode inutile dans UserBase
  * Nettoyage d'imports
Dépôt : http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1.1
  * [Archive Web](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1.1/jplop-1.1.1.war)
  * [Descripteur du projet](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1.1/jplop-1.1.1.pom)
  * [Sources](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1.1/jplop-1.1.1-sources.jar)
  * [Javadoc](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1.1/jplop-1.1.1-javadoc.jar)

## JPlop 1.1 ##
Fritures:
  * Réécriture de la couche de stockage pour permettre un backend en base (pour la 1.2)
Dépôt : http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1
  * [Archive Web](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1/jplop-1.1.war)
  * [Descripteur du projet](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1/jplop-1.1.pom)
  * [Sources](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1/jplop-1.1-sources.jar)
  * [Javadoc](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.1/jplop-1.1-javadoc.jar)

## JPlop 1.0 ##
Fritures:
  * Support du /nick
  * Affichage des paramètres (login, UA)
  * Modification du style du bloc de message
Dépôt : http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.0
  * [Archive Web](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.0/jplop-1.0.war)
  * [Descripteur du projet](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/1.0/jplop-1.0.pom)

## JPlop 0.9 ##
Fritures:
  * Suppression de la page d'accueil, la navigation revient directement sur la tribune
  * Ajout des liens de navigation dans l'en-tête
  * Révision du fonctionnement interne du backup de l'historique et des utilisateurs
Dépôt : http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.9
  * [Archive Web](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.9/jplop-0.9.war)
  * [Descripteur du projet](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.9/jplop-0.9.pom)
  * [Sources](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.9/jplop-0.9-sources.jar)
  * [Javadoc](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.9/jplop-0.9-javadoc.jar)

## JPlop 0.8.2 ##
Fritures:
  * Style différent pour ses propres messages
  * Style des posts (un peu) modifié
Correction :
  * Plantage lors du chargement d'un historique avec des posts anonymes
Dépôt : http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.2
  * [Archive Web](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.2/jplop-0.8.2.war)
  * [Descripteur du projet](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.2/jplop-0.8.2.pom)
  * [Sources](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.2/jplop-0.8.2-sources.jar)
  * [Javadoc](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.2/jplop-0.8.2-javadoc.jar)

## JPlop 0.8.1 ##
Correction :
  * Flux XML de configuration des coincoins corrigé
Dépôt : http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.1
  * [Archive Web](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.1/jplop-0.8.1.war)
  * [Descripteur du projet](http://tifauv.homeip.net/~jplop/repos/releases/tifauv/jplop/0.8.1/jplop-0.8.1.pom)

## JPlop 0.8 ##
Fritures :
  * Création de compte
  * Authentification

## JPlop 0.6 ##
Fritures :
  * Ajout d'une page À Propos
Corrections :
  * Renommage de JBoard en JPlop (un projet JBoard existe déjà sur SourceForge)
  * Correction d'un bug en cas de post sans User-Agent
  * Afficher le backend dans un `<object>`
  * Véritable reload.

## JBoard 0.5.1 ##
Corrections :
  * Gestion du champ `<login>` du backend

## JBoard 0.5 ##
Fritures :
  * Backend basique
  * Interface web avec traitement XSLT côté client
    * Post Ajax
    * Reconnaissance des norloges et surlignage
    * Éditeur basique (gras, italique, souligné, barré, chasse fixe)


# Versions futures #

## JPlop 1.0 ##
Fritures :
  * Reconnaissance des [:totoz]

## JPlop 1.1 ##
Fritures :
  * Page(s) d'administration