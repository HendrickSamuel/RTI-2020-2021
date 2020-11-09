/*
CREATE DATABASE IF NOT EXISTS BD_MOUVEMENTS;
USE BD_MOUVEMENTS;
*/
SET storage_engine=INNODB;

DROP TABLE IF EXISTS Dockers;
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
types char CHECK (types IN ('C', 'T', 'B')),
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
etape boolean DEFAULT(false),
FOREIGN KEY (idContainer) REFERENCES Containers(idContainer),
FOREIGN KEY (transporteurEntrant) REFERENCES Transporteurs(idTransporteur),
FOREIGN KEY (transporteurSortant) REFERENCES Transporteurs(idTransporteur),
FOREIGN KEY (destination) REFERENCES Destinations(ville)
);

CREATE TABLE Logins(
username varchar(255) PRIMARY KEY,
userpassword varchar(255)
);

CREATE TABLE Dockers(
id INT AUTO_INCREMENT PRIMARY KEY,
idDocker varchar(255),
idBateau varchar(255),
startBoatLoad TIMESTAMP DEFAULT '0000-00-00 00:00:00', 
endBoatLoad TIMESTAMP DEFAULT '0000-00-00 00:00:00',
startBoatUnload TIMESTAMP DEFAULT '0000-00-00 00:00:00',
endBoatUnload TIMESTAMP DEFAULT '0000-00-00 00:00:00',
containersCharges INT DEFAULT 0,
containersDecharges INT DEFAULT 0,
destination varchar(255),
FOREIGN KEY (idDocker) REFERENCES Logins(username),
FOREIGN KEY (idBateau) REFERENCES Transporteurs(idTransporteur),
FOREIGN KEY (destination) REFERENCES Destinations(ville)
);

INSERT into Destinations (ville) VALUES ("Paris");
INSERT into Destinations (ville) VALUES ("Madrid");
INSERT into Destinations (ville) VALUES ("Berlin");
INSERT into Destinations (ville) VALUES ("Strasbourg");
INSERT into Destinations (ville) VALUES ("Duisbourg");

INSERT into Logins (username, userpassword) VALUES ("Samuel","sam");
INSERT into Logins (username, userpassword) VALUES ("Kevin","kev");
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
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("ABCD-X12XO-12CE24", "Inpres", "Boissons", 200, "aucun", 500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("EFGH-GM3LF-A456G4", "Microsoft", "Autre", 200, "aucun", 500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("IJKL-DM3PO-12D6M3", "Autre", "Choses", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("MNOP-XB8XO-Z35624", "Inpres", "Boissons", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("QRST-GME6F-A489G4", "Autre", "Autre", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("UVWX-DM2PO-D069M3", "Autre", "Choses", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("ZZZZ-11111-A45699", "Microsoft", "Autre", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("XXXX-22222-12D699", "Autre", "Choses", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("WWWW-33333-Z35699", "Inpres", "Boissons", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("YYYY-44444-A48999", "Autre", "Autre", 200, "aucun", 1500);
INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES ("KKKK-55555-D06999", "Autre", "Choses", 200, "aucun", 1500);

INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("YABB-CHARL-A1B2C3", 1, 1, 2, STR_TO_DATE("15,10,2020", "%d,%m,%Y"), "Res123456", STR_TO_DATE("25,10,2020", "%d,%m,%Y"), "Paris","Train");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("ZACB-XBOXO-Z3CE24", 1, 2, 1, STR_TO_DATE("02,09,2020", "%d,%m,%Y"), "Reservation123",NULL, "Strasbourg","Bateau");
INSERT into Parc (idContainer, x, y, etat, dateReservation, numeroReservation, dateArrivee, destination, moyenTransport) 
VALUES ("DMTE-GMELF-A4G8G4", 1, 3, 2, NULL, "", STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Duisbourg","Bateau");
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

INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("1-VIL-007", "Inpres", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("2-KEV-123", "Inpres", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("3-FAB-123", "Inpres", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("4-ABC-456", "Autre", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("5-CAL-333", "Autre", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("6-DEL-161", "Autre", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("7-LAR-069", "Microsoft", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("8-CHA-666", "Microsoft", 20, NULL, "C");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("9-TST-001", "Autre", 60, NULL, "T");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("9-TST-002", "Microsoft", 60, NULL, "T");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("9-BTX-001", "Inpres", 40, NULL, "B");
INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES ("9-BTX-002", "Autre", 35, NULL, "B");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YABB-CHARL-A1B2C3", "1-VIL-007", STR_TO_DATE("25,10,2020", "%d,%m,%Y"), NULL, 500, NULL, "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DMTE-GMELF-A4G8G4", "3-FAB-123", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-TST-001", 250, STR_TO_DATE("14,10,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DMTE-GMELF-A4G8G4", "2-KEV-123", STR_TO_DATE("05,11,2020", "%d,%m,%Y"), NULL, 250, NULL, "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("04,01,2020", "%d,%m,%Y"), "9-TST-002", 290, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("02,01,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("01,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("02,01,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("02,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("02,01,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("02,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("02,01,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("02,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("02,01,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "7-LAR-069", STR_TO_DATE("11,01,2020", "%d,%m,%Y"), "9-TST-002", 270, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "8-CHA-666", STR_TO_DATE("15,01,2020", "%d,%m,%Y"), "9-TST-002", 220, STR_TO_DATE("20,01,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("08,02,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("20,02,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("08,02,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("20,02,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("08,02,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("20,02,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("08,02,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("20,02,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("08,02,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("20,02,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "7-LAR-069", STR_TO_DATE("12,02,2020", "%d,%m,%Y"), "9-TST-002", 310, STR_TO_DATE("22,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DEDE-JPOLE-D7J0M4", "2-KEV-123", STR_TO_DATE("04,11,2020", "%d,%m,%Y"), "9-TST-001", 250, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "3-FAB-123", STR_TO_DATE("11,02,2020", "%d,%m,%Y"), "9-TST-001", 444, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("25,02,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("20,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("25,02,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("20,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("25,02,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("20,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("25,02,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("20,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("25,02,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("20,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "5-CAL-333", STR_TO_DATE("12,03,2020", "%d,%m,%Y"), "9-TST-001", 333, STR_TO_DATE("16,03,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("25,03,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("30,03,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("25,03,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("30,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("25,03,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("30,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("25,03,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("01,04,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("25,03,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("01,04,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "3-FAB-123", STR_TO_DATE("04,04,2020", "%d,%m,%Y"), "9-TST-001", 230, STR_TO_DATE("05,04,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("10,04,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("30,04,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("05,04,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("30,04,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("05,04,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("30,04,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("07,04,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("25,04,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("09,04,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("16,04,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "8-CHA-666", STR_TO_DATE("12,05,2020", "%d,%m,%Y"), "9-TST-002", 250, STR_TO_DATE("05,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "3-FAB-123", STR_TO_DATE("13,05,2020", "%d,%m,%Y"), "9-TST-001", 444, STR_TO_DATE("05,06,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "5-CAL-333", STR_TO_DATE("15,05,2020", "%d,%m,%Y"), "9-TST-001", 333, STR_TO_DATE("16,05,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "3-FAB-123", STR_TO_DATE("22,05,2020", "%d,%m,%Y"), "9-TST-001", 230, STR_TO_DATE("01,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("10,05,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("30,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("05,05,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("30,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("05,05,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("30,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("07,05,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("25,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("09,05,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("16,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "8-CHA-666", STR_TO_DATE("12,06,2020", "%d,%m,%Y"), "9-TST-002", 250, STR_TO_DATE("25,06,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("04,06,2020", "%d,%m,%Y"), "9-TST-002", 290, STR_TO_DATE("05,06,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "7-LAR-069", STR_TO_DATE("11,06,2020", "%d,%m,%Y"), "9-TST-002", 270, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "8-CHA-666", STR_TO_DATE("15,06,2020", "%d,%m,%Y"), "9-TST-002", 220, STR_TO_DATE("20,06,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("01,07,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("01,07,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("01,07,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("01,07,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("01,07,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("07,07,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("20,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("07,07,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("20,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("07,07,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("20,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("07,07,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("20,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("07,07,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("20,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("07,07,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("20,07,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("01,08,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("15,08,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("01,08,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("15,08,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("01,08,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("15,08,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("01,08,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("15,08,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("16,08,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("17,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("16,08,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("17,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("16,08,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("17,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("16,08,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("17,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("16,08,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("17,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("18,08,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("25,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("18,08,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("25,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("18,08,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("25,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("18,08,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("25,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("18,08,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("25,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "7-LAR-069", STR_TO_DATE("12,08,2020", "%d,%m,%Y"), "9-TST-002", 310, STR_TO_DATE("22,08,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "8-CHA-666", STR_TO_DATE("13,08,2020", "%d,%m,%Y"), "9-BTX-002", 450, STR_TO_DATE("22,08,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("13,08,2020", "%d,%m,%Y"), "9-BTX-002", 500, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "6-DEL-161", STR_TO_DATE("15,08,2020", "%d,%m,%Y"), "9-BTX-002", 230, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "2-KEV-123", STR_TO_DATE("15,08,2020", "%d,%m,%Y"), "9-BTX-002", 350, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "8-CHA-666", STR_TO_DATE("25,08,2020", "%d,%m,%Y"), "9-BTX-002", 250, STR_TO_DATE("30,08,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "3-FAB-123", STR_TO_DATE("22,08,2020", "%d,%m,%Y"), "9-TST-002", 200, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("26,08,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("30,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("26,08,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("30,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("26,08,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("30,08,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("26,08,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("30,08,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("26,08,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("30,08,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("01,09,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("11,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("01,09,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("11,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("01,09,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("11,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("01,09,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("11,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("01,09,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("11,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("12,09,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("15,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("12,09,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("15,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("12,09,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("15,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("12,09,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("15,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("12,09,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("15,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("16,09,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("30,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("16,09,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("30,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("16,09,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("30,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("16,09,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("30,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("16,09,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("30,09,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("01,10,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("01,10,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("01,10,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("01,10,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("01,10,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "5-CAL-333", STR_TO_DATE("01,10,2020", "%d,%m,%Y"), "9-TST-002", 222, STR_TO_DATE("05,10,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("01,10,2020", "%d,%m,%Y"), "9-BTX-001", 252, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "3-FAB-123", STR_TO_DATE("04,10,2020", "%d,%m,%Y"), "9-BTX-001", 255, STR_TO_DATE("05,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "9-BTX-001", 375, STR_TO_DATE("21,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "7-LAR-069", STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "9-BTX-001", 250, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "6-DEL-161", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-BTX-001", 300, STR_TO_DATE("25,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "2-KEV-123", STR_TO_DATE("04,10,2020", "%d,%m,%Y"), "9-BTX-002", 400, STR_TO_DATE("05,10,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "8-CHA-666", STR_TO_DATE("09,10,2020", "%d,%m,%Y"), "9-BTX-002", 450, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("20,10,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("20,10,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("20,10,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("20,10,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("20,10,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("01,11,2020", "%d,%m,%Y"), "9-BTX-002", 500, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "6-DEL-161", STR_TO_DATE("01,11,2020", "%d,%m,%Y"), "9-BTX-002", 230, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "2-KEV-123", STR_TO_DATE("03,11,2020", "%d,%m,%Y"), "9-BTX-002", 350, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "8-CHA-666", STR_TO_DATE("04,11,2020", "%d,%m,%Y"), "9-BTX-002", 250, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZZZZ-11111-A45699", "2-KEV-123", STR_TO_DATE("21,10,2020", "%d,%m,%Y"), "9-TST-002", 790, STR_TO_DATE("08,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XXXX-22222-12D699", "4-ABC-456", STR_TO_DATE("21,10,2020", "%d,%m,%Y"), "9-TST-002", 540, STR_TO_DATE("08,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("WWWW-33333-Z35699", "8-CHA-666", STR_TO_DATE("21,10,2020", "%d,%m,%Y"), "9-TST-002", 1290, STR_TO_DATE("08,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("YYYY-44444-A48999", "1-VIL-007", STR_TO_DATE("21,10,2020", "%d,%m,%Y"), "9-TST-002", 1000, STR_TO_DATE("08,11,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("KKKK-55555-D06999", "3-FAB-123", STR_TO_DATE("21,10,2020", "%d,%m,%Y"), "9-TST-002", 990, STR_TO_DATE("08,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZACB-XBOXO-Z3CE24", "3-FAB-123", STR_TO_DATE("04,02,2020", "%d,%m,%Y"), "9-BTX-001", 255, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("CLSQ-DMAPO-D0D6M3", "2-KEV-123", STR_TO_DATE("11,02,2020", "%d,%m,%Y"), "9-BTX-001", 375, STR_TO_DATE("21,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XNVB-DPZOD-D1J0E5", "7-LAR-069", STR_TO_DATE("11,02,2020", "%d,%m,%Y"), "9-BTX-001", 250, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DEDE-JPOLE-D7J0M4", "6-DEL-161", STR_TO_DATE("12,02,2020", "%d,%m,%Y"), "9-BTX-001", 300, STR_TO_DATE("25,02,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("04,02,2020", "%d,%m,%Y"), "9-BTX-002", 400, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "8-CHA-666", STR_TO_DATE("09,02,2020", "%d,%m,%Y"), "9-BTX-002", 450, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "2-KEV-123", STR_TO_DATE("01,02,2020", "%d,%m,%Y"), "9-BTX-002", 500, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "6-DEL-161", STR_TO_DATE("01,02,2020", "%d,%m,%Y"), "9-BTX-002", 230, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("03,02,2020", "%d,%m,%Y"), "9-BTX-002", 350, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "8-CHA-666", STR_TO_DATE("04,02,2020", "%d,%m,%Y"), "9-BTX-002", 250, STR_TO_DATE("05,02,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZACB-XBOXO-Z3CE24", "3-FAB-123", STR_TO_DATE("04,03,2020", "%d,%m,%Y"), "9-BTX-001", 255, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("CLSQ-DMAPO-D0D6M3", "2-KEV-123", STR_TO_DATE("11,03,2020", "%d,%m,%Y"), "9-BTX-001", 375, STR_TO_DATE("21,03,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XNVB-DPZOD-D1J0E5", "7-LAR-069", STR_TO_DATE("11,03,2020", "%d,%m,%Y"), "9-BTX-001", 250, STR_TO_DATE("05,04,2020", "%d,%m,%Y"), "Berlin");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DEDE-JPOLE-D7J0M4", "6-DEL-161", STR_TO_DATE("12,03,2020", "%d,%m,%Y"), "9-BTX-001", 300, STR_TO_DATE("25,03,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("04,03,2020", "%d,%m,%Y"), "9-BTX-002", 400, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "8-CHA-666", STR_TO_DATE("09,03,2020", "%d,%m,%Y"), "9-BTX-002", 450, STR_TO_DATE("05,04,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "2-KEV-123", STR_TO_DATE("01,03,2020", "%d,%m,%Y"), "9-BTX-002", 500, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "6-DEL-161", STR_TO_DATE("01,03,2020", "%d,%m,%Y"), "9-BTX-002", 230, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("03,03,2020", "%d,%m,%Y"), "9-BTX-002", 350, STR_TO_DATE("05,03,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "8-CHA-666", STR_TO_DATE("04,04,2020", "%d,%m,%Y"), "9-BTX-002", 250, STR_TO_DATE("05,04,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZACB-XBOXO-Z3CE24", "3-FAB-123", STR_TO_DATE("04,05,2020", "%d,%m,%Y"), "9-BTX-001", 255, STR_TO_DATE("05,06,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("CLSQ-DMAPO-D0D6M3", "2-KEV-123", STR_TO_DATE("11,06,2020", "%d,%m,%Y"), "9-BTX-001", 375, STR_TO_DATE("21,06,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XNVB-DPZOD-D1J0E5", "7-LAR-069", STR_TO_DATE("11,06,2020", "%d,%m,%Y"), "9-BTX-001", 250, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DEDE-JPOLE-D7J0M4", "6-DEL-161", STR_TO_DATE("12,06,2020", "%d,%m,%Y"), "9-BTX-001", 300, STR_TO_DATE("25,06,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("04,06,2020", "%d,%m,%Y"), "9-BTX-002", 400, STR_TO_DATE("05,06,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "8-CHA-666", STR_TO_DATE("09,07,2020", "%d,%m,%Y"), "9-BTX-002", 450, STR_TO_DATE("05,08,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "2-KEV-123", STR_TO_DATE("01,07,2020", "%d,%m,%Y"), "9-BTX-002", 500, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "6-DEL-161", STR_TO_DATE("01,07,2020", "%d,%m,%Y"), "9-BTX-002", 230, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("03,07,2020", "%d,%m,%Y"), "9-BTX-002", 350, STR_TO_DATE("05,07,2020", "%d,%m,%Y"), "Strasbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "8-CHA-666", STR_TO_DATE("04,07,2020", "%d,%m,%Y"), "9-BTX-002", 250, STR_TO_DATE("05,08,2020", "%d,%m,%Y"), "Duisbourg");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZACB-XBOXO-Z3CE24", "3-FAB-123", STR_TO_DATE("04,08,2020", "%d,%m,%Y"), "9-BTX-001", 255, STR_TO_DATE("05,08,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("CLSQ-DMAPO-D0D6M3", "2-KEV-123", STR_TO_DATE("11,08,2020", "%d,%m,%Y"), "9-BTX-001", 375, STR_TO_DATE("21,08,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XNVB-DPZOD-D1J0E5", "7-LAR-069", STR_TO_DATE("11,08,2020", "%d,%m,%Y"), "9-BTX-001", 250, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DEDE-JPOLE-D7J0M4", "6-DEL-161", STR_TO_DATE("12,08,2020", "%d,%m,%Y"), "9-BTX-001", 300, STR_TO_DATE("25,08,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("04,09,2020", "%d,%m,%Y"), "9-BTX-002", 400, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "8-CHA-666", STR_TO_DATE("09,09,2020", "%d,%m,%Y"), "9-BTX-002", 450, STR_TO_DATE("05,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "2-KEV-123", STR_TO_DATE("01,09,2020", "%d,%m,%Y"), "9-BTX-002", 500, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "6-DEL-161", STR_TO_DATE("01,09,2020", "%d,%m,%Y"), "9-BTX-002", 230, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("03,09,2020", "%d,%m,%Y"), "9-BTX-002", 350, STR_TO_DATE("05,09,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "8-CHA-666", STR_TO_DATE("04,09,2020", "%d,%m,%Y"), "9-BTX-002", 250, STR_TO_DATE("05,10,2020", "%d,%m,%Y"), "Madrid");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ZACB-XBOXO-Z3CE24", "3-FAB-123", STR_TO_DATE("04,10,2020", "%d,%m,%Y"), "9-BTX-001", 255, STR_TO_DATE("05,10,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("CLSQ-DMAPO-D0D6M3", "2-KEV-123", STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "9-BTX-001", 375, STR_TO_DATE("21,10,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("XNVB-DPZOD-D1J0E5", "7-LAR-069", STR_TO_DATE("11,10,2020", "%d,%m,%Y"), "9-BTX-001", 250, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("DEDE-JPOLE-D7J0M4", "6-DEL-161", STR_TO_DATE("12,10,2020", "%d,%m,%Y"), "9-BTX-001", 300, STR_TO_DATE("25,10,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("ABCD-X12XO-12CE24", "2-KEV-123", STR_TO_DATE("04,10,2020", "%d,%m,%Y"), "9-BTX-002", 400, STR_TO_DATE("05,10,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("EFGH-GM3LF-A456G4", "8-CHA-666", STR_TO_DATE("09,10,2020", "%d,%m,%Y"), "9-BTX-002", 450, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("IJKL-DM3PO-12D6M3", "2-KEV-123", STR_TO_DATE("01,11,2020", "%d,%m,%Y"), "9-BTX-002", 500, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("MNOP-XB8XO-Z35624", "6-DEL-161", STR_TO_DATE("01,11,2020", "%d,%m,%Y"), "9-BTX-002", 230, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("QRST-GME6F-A489G4", "2-KEV-123", STR_TO_DATE("03,11,2020", "%d,%m,%Y"), "9-BTX-002", 350, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination)
VALUES ("UVWX-DM2PO-D069M3", "8-CHA-666", STR_TO_DATE("04,11,2020", "%d,%m,%Y"), "9-BTX-002", 250, STR_TO_DATE("05,11,2020", "%d,%m,%Y"), "Paris");

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination, etape)
VALUES ("UVWX-DM2PO-D069M3", "9-BTX-002", null, null, 250, null, "Paris", true);

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination, etape)
VALUES ("XNVB-DPZOD-D1J0E5", "9-BTX-002", null, null, 250, null, "Paris", true);

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination, etape)
VALUES ("DEDE-JPOLE-D7J0M4", "9-BTX-002", null, null, 250, null, "Paris", true);

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination, etape)
VALUES ("ABCD-X12XO-12CE24", "9-BTX-002", null, null, 250, null, "Paris", true);

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination, etape)
VALUES ("EFGH-GM3LF-A456G4", "9-BTX-002", null, null, 250, null, "Paris", true);

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination, etape)
VALUES ("IJKL-DM3PO-12D6M3", "9-BTX-002", null, null, 250, null, "Paris", true);

INSERT into Mouvements (idContainer, transporteurEntrant, dateArrivee, transporteurSortant, poidsTotal, dateDepart, destination, etape)
VALUES ("MNOP-XB8XO-Z35624", "9-BTX-002", null, null, 250, null, "Paris", true);