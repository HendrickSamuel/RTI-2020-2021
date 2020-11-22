/*
CREATE DATABASE IF NOT EXISTS BD_Shopping;
USE BD_MOUVEMENTS;
*/

SET storage_engine=INNODB;

DROP TABLE IF EXISTS Caddie;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Article;

CREATE TABLE Client (
id INT AUTO_INCREMENT PRIMARY KEY,
username varchar(255),
userpassword varchar(255)
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

INSERT INTO Article VALUES ("art000001", "Entrée parc", "./images/entree_parc.jpg", null, 25);
INSERT INTO Article VALUES ("art000002", "Entrée réserve naturelle", "./images/entree_reserve.jpg", null, 30);
INSERT INTO Article VALUES ("art000003", "Guide de la nature", "./images/guide_nature.jpg", 100, 15);
INSERT INTO Article VALUES ("art000004", "Guide de la foret", "./images/guide_foret.jpg", 2, 20);
INSERT INTO Article VALUES ("art000005", "Peluche hérisson", "./images/herisson.jpg", 25, 25);
INSERT INTO Article VALUES ("art000006", "Peluche hiboux", "./images/hiboux.jpg", 20, 30);
INSERT INTO Article VALUES ("art000007", "Peluche cerf", "./images/cerf.jpg", 20, 50);
INSERT INTO Article VALUES ("art000008", "Peluche renard", "./images/renard.jpg", 10, 25);
INSERT INTO Article VALUES ("art000009", "Déguisement Tarzan", "./images/tarzan.jpg", 10, 35);
INSERT INTO Article VALUES ("art000010", "Déguisement Jane", "./images/jane.jpg", 10, 35);
INSERT INTO Article VALUES ("art000011", "Déguisement Mowgli", "./images/mowgli.jpg", 10, 35);
INSERT INTO Article VALUES ("art000012", "Bic rouge", "./images/bic_rouge.jpg", 200, 2);
INSERT INTO Article VALUES ("art000013", "Bic bleu", "./images/bic_bleu.jpg", 200, 2);
INSERT INTO Article VALUES ("art000014", "Bic vert", "./images/bic_vert.jpg", 200, 2);
INSERT INTO Article VALUES ("art000015", "Perforatrice en bois", "./images/perforatrice.jpg", 50, 10);
INSERT INTO Article VALUES ("art000016", "Tapis de souris", "./images/tapis_souris.jpg", 70, 12);
INSERT INTO Article VALUES ("art000017", "Graines de tournesol", "./images/tournesol.jpg", 500, 5);
INSERT INTO Article VALUES ("art000018", "Graines de fraise", "./images/fraises.jpg", 500, 5);
INSERT INTO Article VALUES ("art000019", "Poubelle bio-dégradable", "./images/poubelle_bio.jpg", 30, 13);
INSERT INTO Article VALUES ("art000020", "Jumelles", "./images/jumelles.jpg", 50, 150);
