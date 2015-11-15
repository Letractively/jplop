# Pré-requis #
Vous devez avoir [téléchargé](http://code.google.com/p/jplop/downloads/list) une archive web. Utilisez de préférence la dernière version.

Vous devez également avoir une JVM (1.6 mini) et un Tomcat (5.5 mini) installés et fonctionnels.


# Introduction #
Il y a plusieurs manières de déployer JPlop :
  1. via le Tomcat Manager (pas d'accès ssh au serveur nécesaire) ;
  1. directement dans le répertoire `webapps` de Tomcat.


## Tomcat Manager ##
C'est la manière la plus simple d'installer JPlop. Le Tomcat Manager est une webapp d'administration livrée en standard avec Tomcat. Elle permet de voir la liste des webapps déployées, et d'en ajouter ou retirer. Référez-vous au  [Manager HowTo](http://tomcat.apache.org/tomcat-5.5-doc/manager-howto.html#Introduction) pour plus d'informations.

Pour déployer une webapp, il faut avoir une archive WAR toute prête. Cela signifie qu'il faut modifier la configuration par défaut inclue dans l'archive WAR avant de faire le déploiement. Pour cela, décompressez l'archive, modifiez la [configuration](Configuration.md) puis recréez l'archive. Un WAR n'est qu'une archive de type ZIP avec l'extension WAR.

Une fois cela fait, allez sur [...]


## Déploiement direct ##
[...]