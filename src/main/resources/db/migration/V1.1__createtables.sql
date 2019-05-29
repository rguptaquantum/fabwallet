CREATE SCHEMA IF NOT EXISTS fabwallet;

USE fabwallet;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) UNIQUE NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
  );

CREATE TABLE IF NOT EXISTS  `wallet`
(
id INT NOT NULL AUTO_INCREMENT,
user_id INT REFERENCES user(id),
balance DECIMAL(15,2) DEFAULT 0 NOT NULL,
last_updated TIMESTAMP DEFAULT now(),
PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `transaction`
(
id INT NOT NULL AUTO_INCREMENT,
global_id VARCHAR(45) UNIQUE NOT NULL,
type_id VARCHAR(45) NOT NULL,
amount DECIMAL(15,2) NOT NULL,
wallet_id INT REFERENCES wallet(id),
description TEXT,
last_updated TIMESTAMP DEFAULT now(),
last_updated_by VARCHAR(45) DEFAULT 'admin',
PRIMARY KEY (`id`)
);
