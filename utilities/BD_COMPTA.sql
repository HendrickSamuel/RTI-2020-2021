/*
CREATE DATABASE IF NOT EXISTS BD_Compta;
USE BD_MOUVEMENTS;
*/

SET storage_engine=INNODB;

DROP TABLE IF EXISTS Salaires;
DROP TABLE IF EXISTS Primes;
DROP TABLE IF EXISTS Tarifs;
DROP TABLE IF EXISTS Items_Facture;
DROP TABLE IF EXISTS Facture;
DROP TABLE IF EXISTS Personnel;
DROP TABLE IF EXISTS Baremes;

CREATE TABLE Baremes(
id varchar(255) PRIMARY KEY,
montant INT
);

INSERT INTO Baremes VALUES("chef-comptable", 3000);
INSERT INTO Baremes VALUES("comptable", 2500);
INSERT INTO Baremes VALUES("manutentionnaire", 1800);
INSERT INTO Baremes VALUES("directeur", 5000);
INSERT INTO Baremes VALUES("mecanicien", 2200);
INSERT INTO Baremes VALUES("developpeur", 3000);

CREATE TABLE Personnel (
matricule varchar(255) PRIMARY KEY,
nom varchar(255),
prenom varchar(255),
username varchar(255),
userpassword varchar(255),
email varchar(255),
fonction varchar(255), /*manutentionnaire, chef d'équipe, préposé(e), chef de poste, comptable, chefcomptable, directeur(rice), …)*/
FOREIGN KEY (fonction) REFERENCES Baremes(id)
);

INSERT INTO Personnel VALUES ("test1","Hendrick","Samuel", "Samuel", "sam", "email", "chef-comptable");
INSERT INTO Personnel VALUES ("test2","Delaval","Kevin", "Kevin", "kev", "email", "chef-comptable");
INSERT INTO Personnel VALUES ("test3","Donald","Duck", "Donald", "duck", "email", "comptable");
INSERT INTO Personnel VALUES ("test4","Larue","Hortance", "Hortance", "hort", "email", "manutentionnaire");

CREATE TABLE Facture(
id INT AUTO_INCREMENT PRIMARY KEY,
societe varchar(255),
date_facture Date,
tva float,
facture_validee boolean,
comptable_validateur varchar(255),
facture_envoyee boolean,
moyen_payement varchar(255),
facture_payee boolean
);

INSERT INTO Facture VALUES (1, "test", STR_TO_DATE("15,11,2020", "%d,%m,%Y"), 21, 0, null, 0, null, 0);

CREATE TABLE Items_Facture(
id INT AUTO_INCREMENT PRIMARY KEY,
facture INT,
mouvement INT, /*comprend pas comment on ira le chercher*/
container varchar(255), /*idem*/
destination varchar(255), /*idem*/
prix_htva float,
FOREIGN KEY (facture) REFERENCES Facture(id)
);

INSERT INTO Items_Facture VALUES (1, 1, 1, "cont", "dest", 22);

CREATE TABLE Tarifs(
id VARCHAR(255) PRIMARY KEY,
type_tarif varchar(255),
materiel varchar(255),
produit_petrolier boolean,
derniere_update DATE
);

CREATE TABLE Salaires(
id INT AUTO_INCREMENT PRIMARY KEY,
nom varchar(255),
prenom varchar(255),
mois_annee DATE,
montant_brut float,
ret_ONSS float,
ret_prec float,
fich_env boolean,
sal_vers boolean,
sal_val boolean
);

CREATE TABLE Primes(
id INT AUTO_INCREMENT PRIMARY KEY,
montant float,
octroi DATE,
motif varchar(255),
octroie_par varchar(255),
octroie_a varchar(255),
payee boolean,
FOREIGN KEY (octroie_par) REFERENCES Personnel(matricule),
FOREIGN KEY (octroie_a) REFERENCES Personnel(matricule)
);