# TP2

**Noémie PHAN / Léo LEPLAT**<br />
*le 14/06/2020*<br /><br />

## Introduction

Le but de ce TP est d'améliorer l'application de to-do liste en récupérant les listes et les items des utilisateurs depuis une API Rest. Pour cela, la librairie **Retrofit** a été utilisée en effectuant des requêtes à l'API http://tomnab.fr/todo-api/.<br /><br />

## Analyse

### Hiérarchie du projet
Voici la hiérarchie du projet des fichiers sources :<br />
<img src="images/Screenshot from 2020-06-14 15-02-15.png" alt="mainActivity" height="400" />

Par rapport à la précédente version de l'application, le package `api` a été créé. Il contient l'interface *ServiceAPI* qui s'occupe des requêtes vers l'API. Un package `model` contient également les classes de données servant à lire les réponses des requêtes.<br />
Il y a également eu la création de l'objet *DataProvider* qui fait le lien le programme et l'interface *ServiceAPI*, ainsi que la classe *MyApp* qui permet de récupérer le contexte de l'application n'importe où dans le code.<br />
Enfin, on utilise toujours une *GenericActivity* qui permet d'éviter de réécrire le même code dans les trois activités principales.<br />

### Fonctionnement de l'API
Pour chaque requête à l'API, on utiliser une *coroutine scope* qui permet de faire des appels asynchrones. On utilise également dans chaque coroutine un *runCatching* permettant de gérer certaines exceptions. Par exemple, on veut afficher un certain message quand les informations de connexion entrées par l'utilisateur sont incorrectes.<br />
Dans les activités principales de l'application, les appels à l'API sont faits par le biais de l'objet *DataProvider*. Celui-ci appelle les fonctions de l'interface *ServiceAPI*, en convertissant les éventuelles réponses de l'API en des objets internes à l'application (*ItemToDo*, *ListeToDo*).<br />
L'interface *ServiceAPI* s'occupe des requêtes vers l'API.<br />
Pour tester la connectivité du téléphone, l'application effectue une requête non paramétrée vers l'API. Si l'API renvoie une *httpException*, c'est que la connectivité entre le téléphone et l'API est fonctionnelle et que l'on peut donc activer le bouton de connexion.<br />

### Fonctionnalités
Les fonctionnalités demandées ont été implémentées, ainsi que :
* L'ajout d'une nouvelle liste, qui marche comme l'ajout d'un nouvel item.
* L'application stocke dans les préférences le pseudo de l'utilisateur actuel et le mot de passe associé. Ainsi, la connection va automatiquement se faire au démarrage de l'application si l'utilisateur était connecté lors de la fermeture de l'application. Un bouton "Déconnexion" est présent dans les paramètres de l'application qui efface le pseudo et le mot de passe des préférences et qui fait revenir à l'activité de connexion.<br /><br />


## Perspectives et conclusion
Ce TP2 a permis d'effectuer des requêtes asynchrones avec les coroutines et de communiquer avec une API Rest. Il resterait toutefois quelques pistes d'améliorations :
* Ajouter la possibilité de supprimer les todo list et les items.
* Implémenter une fonction de recherche.
* Ajouter la possibilité de créer ou supprimer un utilisateur.
* Améliorer l'affichage des items, permettre de les classer.<br /><br />


## Bibliographie
* [Cours de M.Boukadir](https://moodle1920.centralelille.fr/mod/page/view.php?id=20281)
* [Cours d'Isabelle Le Glaz](https://docs.google.com/presentation/d/1zjKsdSIA5QnP-BG9z07fZyhrOKpogm_4XxCewUVg3x0/edit#slide=id.g89770eca0e_0_137)
* [Librairie Retrofit](https://square.github.io/retrofit/)
* [StackOverflow](https://stackoverflow.com/)
