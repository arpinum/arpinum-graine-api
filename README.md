# Arpinum Seed - API

## Le but 

Ce projet est la base de travail pour le serveur de certaines applications Arpinum.
 
La philosophie principale est d'utiliser des bibliothèques qui font le job plutôt que de se faire utiliser par un framework

## WARNING

Nous ne prétendons pas que cette graîne est parfaite, encore moins capable de gérer le cas ou la charge spécifique que vous avez en tête. 
Nous la codons et faisons évoluer selon nos besoins, et pour le moment, elle ne nous a pas trahis en production.

## Concepts

Les postulats sont les suivants : 


* Nous voulons coder le métier en ignorance de toute notion technique
* L'ignorance de la persistance est obtenue via le pattern Repository
* La communication avec le métier se fait via des cas d'utilisation capturés dans des commandes
* Les commandes sont passées via un bus
* La lecture et l'écriture sont séparées dans deux modèles distincts
* Le modèle de lecture est généré par des projections depuis les évènements métiers
* La persistance est assurée par par de l'EventSourcing. L'EventStore est pour le moment MongoDB
* Les projections sont écrites et lues via JongoDB
* Le serveur ne doit exposer qu'une API Rest, la présentation est déléguée aux applications
* Ratpack sert à la fois à fournir un serveur http réactif, à charger et configurer l'application

## Licence

La licence reste pour le moment à définir, faites ce que vous voulez. 
 
 


