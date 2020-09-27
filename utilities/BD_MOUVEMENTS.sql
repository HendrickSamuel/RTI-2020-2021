CREATE DATABASE IF NOT EXISTS BD_MOUVEMENTS;
USE BD_MOUVEMENTS;

DROP TABLE IF EXISTS Parc;
DROP TABLE IF EXISTS Societes;
DROP TABLE IF EXISTS Containers;
DROP TABLE IF EXISTS Transporteurs;
DROP TABLE IF EXISTS Mouvements;
DROP TABLE IF EXISTS Destinations;


CREATE TABLE Parc (
id INT AUTO_INCREMENT PRIMARY KEY,
x FLOAT NOT NULL,
y FLOAT NOT NULL,
etat INT DEFAULT 0 CHECK (etat IN (0,1,2)),
dateReservation DATE,
dateArrivee DATE,
poids FLOAT,
destination varchar(255),
moyenTransport varchar(255)
);

CREATE TABLE Societes (
id INT AUTO_INCREMENT PRIMARY KEY,
nom varchar(255),
email varchar(255),
telephone varchar(255),
adresse varchar(255)
);

CREATE TABLE Containers (
id INT AUTO_INCREMENT PRIMARY KEY,
societe INT, /* REFERENCES */
contenu varchar(255),
capacite FLOAT NOT NULL,
dangers varchar(255)
);

CREATE TABLE Transporteurs (
id varchar(255) PRIMARY KEY,
societe INT, /* REFERENCES */
capacite FLOAT NOT NULL,
caracteristiques varchar(255)
);

CREATE TABLE Mouvements (
id INT AUTO_INCREMENT PRIMARY KEY,
container INT, /* REFERENCES */
transporteurEntrant varchar(255), /* REFERENCES */
dateArrivee DATE,
transporteurSortant varchar(255), /* REFERENCES */
poidsTotal FLOAT,
dateDepart DATE,
destination varchar(255) /* REFERENCES */
);

CREATE TABLE Destinations (
ville varchar(255) PRIMARY KEY,
distanceBateau FLOAT, /* peut etre varchar ? si unite dedans */
distanceTrain FLOAT,
distanceRoute FLOAT
);