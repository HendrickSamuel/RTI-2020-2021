/*
CREATE DATABASE IF NOT EXISTS BD_Compta;
USE BD_MOUVEMENTS;
*/

SET storage_engine=INNODB;

DROP TABLE IF EXISTS Tarifs;
DROP TABLE IF EXISTS Items_Facture;
DROP TABLE IF EXISTS Facture;
DROP TABLE IF EXISTS Personnel;

CREATE TABLE Personnel (
matricule varchar(255) PRIMARY KEY,
nom varchar(255),
prenom varchar(255),
username varchar(255),
userpassword varchar(255),
email varchar(255),
fonction varchar(255) /*manutentionnaire, chef d'équipe, préposé(e), chef de poste, comptable, chefcomptable, directeur(rice), …)*/
);

CREATE TABLE Facture(
id INT AUTO_INCREMENT PRIMARY KEY,
societe varchar(255),
mois INT CHECK (mois BETWEEN 1 AND 12),
annee INT,
tva float,
facture_validee boolean,
comptable_validateur varchar(255),
facture_envoyee boolean,
moyen_envoi varchar(255),
facture_payee boolean
);

CREATE TABLE Items_Facture(
id INT AUTO_INCREMENT PRIMARY KEY,
facture INT,
mouvement INT, /*comprend pas comment on ira le chercher*/
container varchar(255), /*idem*/
destination varchar(255), /*idem*/
prix_htva float,
FOREIGN KEY (facture) REFERENCES Facture(id)
);

CREATE TABLE Tarifs(
id VARCHAR(255) PRIMARY KEY,
type_tarif varchar(255),
materiel varchar(255),
produit_petrolier boolean,
derniere_update DATE
);