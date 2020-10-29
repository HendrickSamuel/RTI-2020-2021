/*
CREATE DATABASE IF NOT EXISTS BD_MOUVEMENTS;
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
present boolean DEFAULT(false),
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

INSERT into Destinations (ville) VALUES ("Paris");
INSERT into Destinations (ville) VALUES ("Madrid");
INSERT into Destinations (ville) VALUES ("Liege");
INSERT into Destinations (ville) VALUES ("Vise");
INSERT into Destinations (ville) VALUES ("Berlin");

INSERT into Logins (username, userpassword) VALUES ("Samuel","superSecurePass123");
INSERT into Logins (username, userpassword) VALUES ("Kevin","superSecurePass123");
INSERT into Logins (username, userpassword) VALUES ("ServeurContainer","password");

INSERT into Societes (nom, email, telephone, adresse) VALUES ("Inpres","Inpres@Email.com","0472/42.16.88","MonAdresse Rue de l'adresse");
INSERT into Societes (nom, email, telephone, adresse) VALUES ("Microsoft","Microsoft@Email.com","0472/42.16.88","MonAdresse Rue de l'adresse");
INSERT into Societes (nom, email, telephone, adresse) VALUES ("Autre","Autre@Email.com","0472/42.16.88","MonAdresse Rue de l'adresse");

INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("YABB-CHARL-A1B2C3", "Inpres", "Motos", 200, "aucun", 500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("ZACB-XBOXO-Z3CE24", "Inpres", "Boissons", 200, "aucun", 500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("DMTE-GMELF-A4G8G4", "Microsoft", "Autre", 200, "aucun", 500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("CLSQ-DMAPO-D0D6M3", "Microsoft", "Choses", 200, "aucun", 500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("XNVB-DPZOD-D1J0E5", "Microsoft", "Trucs", 200, "Nuisancees sonores", 500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("DEDE-JPOLE-D7J0M4", "Microsoft", "Trucs", 200, "", 500);

INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("YABB-CHARL-A1B2C3", 1, 1, 2, STR_TO_DATE("15,10,2020", "%d,%m,%Y"), "Res123456", STR_TO_DATE("25,10,2020", "%d,%m,%Y"), "Paris","Train");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("ZACB-XBOXO-Z3CE24", 1, 2, 1, STR_TO_DATE("02,09,2020", "%d,%m,%Y"), "Reservation123",NULL, "Liege","Bateau");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("DMTE-GMELF-A4G8G4", 1, 3, 2, NULL, "", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "Paris","Bateau");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("CLSQ-DMAPO-D0D6M3", 1, 4, 1, STR_TO_DATE("02,09,2020", "%d,%m,%Y"), "Reservation2345", NULL , "Madrid","Train");
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

INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques) VALUES ("1-VIL-007", "Inpres", 20, NULL);
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques) VALUES ("2-KEV-123", "Inpres", 20, NULL);
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques) VALUES ("3-FAB-123", "Inpres", 20, NULL);
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques) VALUES ("4-ABC-456", "Inpres", 20, NULL);
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques) VALUES ("5-CAL-333", "Inpres", 20, NULL);

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YABB-CHARL-A1B2C3", "1-VIL-007", STR_TO_DATE("25,10,2020", "%d,%m,%Y"), NULL, 500, NULL, "Paris");
INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DMTE-GMELF-A4G8G4", "3-FAB-123", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), NULL, 250, NULL, "Liege");