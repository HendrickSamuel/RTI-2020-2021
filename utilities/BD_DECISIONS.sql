/*
CREATE DATABASE IF NOT EXISTS BD_DECISIONS;
USE BD_MOUVEMENTS;
*/
SET storage_engine=INNODB;

DROP TABLE IF EXISTS Decisions;
DROP TABLE IF EXISTS TypeRequetes;

CREATE TABLE TypeRequetes (
typeRequete varchar(255) PRIMARY KEY
);

CREATE TABLE Decisions (
id INT AUTO_INCREMENT PRIMARY KEY,
typeRequete varchar(255) NOT NULL,
p_value FLOAT NOT NULL,
decision CHAR(4) CHECK (decision IN ('OK', 'NOK')),
dateDecision DATE NOT NULL,
FOREIGN KEY (typeRequete) REFERENCES TypeRequetes(typeRequete)
);


INSERT into TypeRequetes (typeRequete) VALUES ("Test conformite");
INSERT into TypeRequetes (typeRequete) VALUES ("Test homogeneite");
INSERT into TypeRequetes (typeRequete) VALUES ("Test ANOVA");