# Pré-requis #

Vous devez configurer l'archive WAR de JPlop _avant_ de le déployer _via_ le Tomcat Manager.
Si vous préférez un déploiement direct, vous devez avoir déjà [installé](Installation.md) JPlop sur un serveur Tomcat.


# Détails #

JPlop dispose de 2 fichiers de configuration situés dans le `CLASSPATH` :
  1. `jplop.properties` : configuration de la tribune ;
  1. `log4.properties` : configuration des logs.
Ces deux fichiers sont des fichiers `properties` classiques en Java. Chaque ligne peut être soit vide, soit un commentaire, soit une déclaration de type _clé = valeur_.

## Configuration de la tribune ##

La configuration de la tribune se fait dans le fichier `WEB-INF/classes/tifauv/jplop/config.properties`. Les clés de configuration reconnues sont listées ci-dessous. Toute autre ligne est ignorée.
| `jplop.name`              | Le nom _court_ de la tribune                            |
|:--------------------------|:--------------------------------------------------------|
| `jplop.fullName`          | Un nom plus descriptif                                  |
| `jplop.url`               | L'URL complète de l'installation                        |
| `jplop.history.size`      | La taille maximale de l'historique                      |
| `jplop.backupEvery`       | La fréquence de sauvegarde des données                  |
| `jplop.post.maxLength`    | La longueur maximale d'un post                          |
| `storage.factory`         | le nom de la factory définissant le backend de stockage |
| `storage.datadir`         | Le répertoire des données (historique & utilisateurs)   |


### `jplop.name` ###
Le nom court de la tribune, affiché en haut des pages.

_Valeur par défaut_ : `JPlop`


### `jplop.fullName` ###
Un nom plus descriptif de la tribune.

_Valeur par défaut_ : `Da J2EE tribioune`


### `jplop.url` ###
L'URL complète de l'installation. Elle est utilisée dans l'attribut `board[@site]` du backend.

_Valeur par défaut_ : `http://localhost:8080/jplop`


### `jplop.history.size` ###
La taille maximale de l'historique. Une fois cette taille atteinte, les posts les plus anciens seront effacés lors de l'insertion des nouveaux.

_Valeur par défaut_ : 100


### `jplop.backupEvery` ###
La fréquence de sauvegarde de l'historique des messages et de la liste des utilisateurs.

_Valeur par défaut_ : 5


### `jplop.post.maxLength` ###
La longueur maximale d'un post. Tout message reçu sera tronqué à cette longueur. Les espaces en début et fin de post sont automatiquement effacés et ne comptent donc pas.

_Valeur par défaut_ : 512


### `storage.factory` ###
Le nom de la StorageFactory à utiliser. Une StorageFactory va définir comment seront enregistrées les données. Actuellement, il n'y a que le portage de l'ancien système reposant sur des fichiers.

_Valeur par défaut_ : tifauv.jplop.core.storage.file.FileStorageFactory


### `storage.datadir` ###
Le répertoire où sont enregistrées les données. Cela concerne l'historique ainsi que
la base d'utilisateurs. Le répertoire est créé au besoin. Si vous utilisez le Tomcat
Manager pour mettre à jour vos webapps, il faut que ce répertoire soit en-dehors du
contexte de la webapp. Sinon, il sera effacé à chaque mise à jour. Par défaut, c'est
le répertoire `jplop-data` à la racine de Tomcat.

_Valeur par défaut_ : `${catalina.base}/jplop-data`


## Configuration des logs ##

JPlop utilise [Log4j](http://logging.apache.org/log4j/) comme système de log. Cette section ne présente que quelques exemples de configuration. Elle n'a pas pour but d'être un guide de configuration exhaustif de Log4j.