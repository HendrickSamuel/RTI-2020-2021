/*CREATE DATABASE IF NOT EXISTS BD_MOUVEMENTS;
USE BD_MOUVEMENTS;
*/
SET storage_engine=INNODB;

DROP TABLE IF EXISTS Mouvements;
DROP TABLE IF EXISTS Parc;
DROP TABLE IF EXISTS Containers;
DROP TABLE IF EXISTS Transporteurs;
DROP TABLE IF EXISTS Destinations;
DROP TABLE IF EXISTS Societes;
DROP TABLE IF EXISTS Logins;

CREATE TABLE Societes (
nom varchar(255) PRIMARY KEY,
email varchar(255),
telephone varchar(255),
adresse varchar(255)
);

CREATE TABLE Destinations (
ville varchar(255) PRIMARY KEY,
unite varchar(10),
distanceBateau FLOAT, 
distanceTrain FLOAT,
distanceRoute FLOAT
);

CREATE TABLE Containers (
idContainer varchar(255) PRIMARY KEY,
idSociete varchar(255),
contenu varchar(255),
capacite FLOAT NOT NULL,
dangers varchar(255),
poids FLOAT,
FOREIGN KEY (idSociete) REFERENCES Societes(nom)
);

CREATE TABLE Parc (
id INT AUTO_INCREMENT PRIMARY KEY,
idContainer varchar(255),
x int NOT NULL,
y int NOT NULL,
etat INT DEFAULT 0 CHECK (etat IN (0,1,2)),
dateReservation DATE,
numeroReservation varchar(255),
dateArrivee DATE,
destination varchar(255),
moyenTransport varchar(255),
FOREIGN KEY (idContainer) REFERENCES Containers(idContainer)
);

CREATE TABLE Transporteurs (
idTransporteur varchar(255) PRIMARY KEY,
idSociete varchar(255),
capacite FLOAT NOT NULL,
caracteristiques varchar(255),
FOREIGN KEY (idSociete) REFERENCES Societes(nom)
);

CREATE TABLE Mouvements (
id INT AUTO_INCREMENT PRIMARY KEY,
idContainer varchar(255), 
transporteurEntrant varchar(255),
dateArrivee DATE,
transporteurSortant varchar(255),
poidsTotal FLOAT,
dateDepart DATE,
destination varchar(255),
FOREIGN KEY (idContainer) REFERENCES Containers(idContainer),
FOREIGN KEY (transporteurEntrant) REFERENCES Transporteurs(idTransporteur),
FOREIGN KEY (transporteurSortant) REFERENCES Transporteurs(idTransporteur),
FOREIGN KEY (destination) REFERENCES Destinations(ville)
);

CREATE TABLE Logins(
id INT AUTO_INCREMENT PRIMARY KEY,
username varchar(255),
userpassword varchar(255)
);

INSERT into Logins (username, userpassword) VALUES ("Samuel","superSecurePass123");
INSERT into Logins (username, userpassword) VALUES ("Kevin","superSecurePass123");

INSERT into Societes (nom, email, telephone, adresse) VALUES ("MaSociete","MonEmail@Email.com","0472/42.16.88","MonAdresse Rue de l'adresse");
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("blabla", "MaSociete", "Xylophones", 200, "Nuisancees sonores", 0.0005);
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("blabla", 1, 1, 2, NULL, "test", NULL, "Paris","Train");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 2, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 3, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 4, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 5, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 6, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 7, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 8, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 9, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 1, 10, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 1, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 2, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 3, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 4, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 5, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 6, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 7, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 8, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 9, 0, NULL, "", NULL, "","");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES (NULL, 2, 10, 0, NULL, "", NULL, "","");



