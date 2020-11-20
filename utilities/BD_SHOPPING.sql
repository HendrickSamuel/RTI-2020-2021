/*
CREATE DATABASE IF NOT EXISTS BD_Shopping;
USE BD_MOUVEMENTS;
*/

SET storage_engine=INNODB;

DROP TABLE IF EXISTS Res_Nat;
DROP TABLE IF EXISTS Caddie;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Article;

CREATE TABLE Client (
id INT AUTO_INCREMENT PRIMARY KEY,
username varchar(255),
userpassword varchar(255)
);

CREATE TABLE Res_Nat(
id date PRIMARY KEY,
quantite INT
);

CREATE TABLE Article(
id varchar(10) PRIMARY KEY,
libelle varchar(255),
image_path varchar(255),
quantite INT,
prix FLOAT
);

CREATE TABLE Caddie(
id_client INT,
id_article varchar(10),
quantite INT,
date_res timestamp,
acheter boolean DEFAULT FALSE,
FOREIGN KEY (id_client) REFERENCES Client(id),
FOREIGN KEY (id_article) REFERENCES Article(id),
PRIMARY KEY (id_client, id_article)
);

INSERT INTO Client VALUES (null, "Samuel", "sam");
INSERT INTO Client VALUES (null, "Kevin", "kev");

INSERT INTO Article VALUES ("art000001", "Entrée parc", null, null, 25);
INSERT INTO Article VALUES ("art000002", "Entrée réserve naturelle", null, null, 30);
INSERT INTO Article VALUES ("art000003", "Guide de la nature", null, 100, 15);
INSERT INTO Article VALUES ("art000004", "Guide de la foret", null, 2, 20);
INSERT INTO Article VALUES ("art000005", "Peluche hérisson", null, 25, 25);
INSERT INTO Article VALUES ("art000006", "Peluche hiboux", null, 20, 30);
INSERT INTO Article VALUES ("art000007", "Peluche cerf", null, 20, 50);
INSERT INTO Article VALUES ("art000008", "Peluche renard", null, 10, 25);
INSERT INTO Article VALUES ("art000009", "Déguisement Tarzan", null, 10, 35);
INSERT INTO Article VALUES ("art000010", "Déguisement Jane", null, 10, 35);
INSERT INTO Article VALUES ("art000011", "Déguisement Mowgli", null, 10, 35);
INSERT INTO Article VALUES ("art000012", "Bic rouge", null, 200, 2);
INSERT INTO Article VALUES ("art000013", "Bic bleu", null, 200, 2);
INSERT INTO Article VALUES ("art000014", "Bic vert", null, 200, 2);
INSERT INTO Article VALUES ("art000015", "Perforatrice en bois", null, 50, 10);
INSERT INTO Article VALUES ("art000016", "Tapis de souris", null, 70, 12);
INSERT INTO Article VALUES ("art000017", "Graines de tournesol", null, 500, 5);
INSERT INTO Article VALUES ("art000018", "Graines de fraise", null, 500, 5);
INSERT INTO Article VALUES ("art000019", "Poubelle bio-dégradable", null, 30, 13);
INSERT INTO Article VALUES ("art000020", "Jumelles", null, 50, 150);
