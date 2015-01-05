CREATE SCHEMA IF NOT EXISTS racing_2d;

USE racing_2d;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


DROP TABLE IF EXISTS `obstacles` ;

CREATE TABLE IF NOT EXISTS `obstacles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `place` ENUM('LEFT','RIGHT') NOT NULL,
  `tile_id` INT NOT NULL,
  `obstacle_type` ENUM('EGGPLANT','MELON','PADDO','STRAWBERRY') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_obstacles_tiles_tile_id_idx` (`tile_id` ASC),
  CONSTRAINT `fk_obstacles_tiles_tile_id` FOREIGN KEY (`tile_id`) REFERENCES `tiles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `events` ;
CREATE TABLE IF NOT EXISTS `events` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(255) NOT NULL,
  `timestamp` DATE NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_events_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `favorite_circuits` ;
CREATE TABLE IF NOT EXISTS `favorite_circuits` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `circuit_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_favorite_circuits_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_favorite_circuits_circuit_circuit_id` FOREIGN KEY (`circuit_id`) REFERENCES `circuits` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `results`;
CREATE TABLE `results` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `time` INT NOT NULL,
  `lap_number` INT DEFAULT NULL,
  `race_id` INT DEFAULT NULL,
  `user_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `race_id_idx` (`race_id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `race_id` FOREIGN KEY (`race_id`) REFERENCES `races` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `participants`;
CREATE TABLE `participants` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tournament_id` INT DEFAULT NULL,
  `user_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `u_id_idx` (`user_id`),
  KEY `t_id_idx` (`tournament_id`),
  CONSTRAINT `u_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `t_id` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `races`;
CREATE TABLE `races` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tournament_id` INT DEFAULT NULL,
  `circuit_id` INT DEFAULT NULL,
  `laps` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `circuit_id_idx` (`circuit_id`),
  KEY `tournament_id_idx` (`tournament_id`),
  CONSTRAINT `circuit_id` FOREIGN KEY (`circuit_id`) REFERENCES `circuits` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tournament_id` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

INSERT INTO races VALUES 
(1,1,1,3),(2,1,4,5),(3,1,9,6),
(4,2,2,5),(5,2,4,3),(6,2,8,2),
(7,3,6,2),(8,3,8,2),
(9,4,7,4),(10,4,8,6),(11,4,9,8),
(12,5,2,6),(13,5,9,3),
(14,6,6,2),(15,6,7,1),(16,6,1,3),
(17,7,5,3),(18,7,7,2),(19,7,2,3),(20,7,9,2),
(21,8,9,3),(22,8,3,2),(23,8,6,1),
(24,9,7,5),(25,9,4,3),(26,9,1,1),(27,9,8,1),(28,9,5,1);


DROP TABLE IF EXISTS `tournaments`;
CREATE TABLE `tournaments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `formule` enum('TOTAL','FASTEST','LONGEST') NOT NULL,
  `user_id` INT DEFAULT NULL,
  `max_players` INT NOT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `_idx` (`user_id`),
  CONSTRAINT `fk_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

INSERT INTO `tournaments` VALUES 
(1,'tournament1','TOTAL',4,4,'2014-04-26 10:30:00'),
(2,'tournament2','TOTAL',4,6,'2014-04-27 20:00:00'),
(3,'tournament3','FASTEST',3,8,'2014-04-30 02:30:00'),
(4,'tournament4','TOTAL',3,10,'2014-05-01 19:15:00'),
(5,'tournament5','TOTAL',3,3,'2014-05-02 16:30:00'),
(6,'tournament6','LONGEST',2,4,'2014-05-01 00:30:00'),
(7,'tournament7','FASTEST',2,12,'2014-05-04 21:45:00'),
(8,'tournament8','LONGEST',1,5,'2014-04-28 10:00:00'),
(9,'tournament9','LONGEST',1,7,'2014-04-02 06:30:00');


DROP TABLE IF EXISTS `circuits`;
CREATE TABLE `circuits` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `designer` INT NOT NULL,
  `direction` ENUM('RIGHT','LEFT','UP','DOWN') NOT NULL,
  `rows` INT NOT NULL,
  `columns` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_circuits_designer` (`designer`),
  CONSTRAINT `fk_circuits_designer` FOREIGN KEY (`designer`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

INSERT INTO `circuits` VALUES (1,'circuit1',4,'RIGHT',3,3),(2,'circuit2',4,'LEFT',4,4),(3,'circuit3',4,'RIGHT',5,5),(4,'circuit4',3,'LEFT',3,3),(5,'circuit5',3,'RIGHT',6,6),(6,'circuit6',2,'LEFT',4,5),(7,'circuit7',2,'RIGHT',5,7),(8,'circuit8',2,'RIGHT',5,7),(9,'circuit9',1,'LEFT',3,3);


DROP TABLE IF EXISTS `ghosts`;
CREATE TABLE `ghosts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `time` INT NOT NULL,
  `user_id` INT NOT NULL,
  `circuit_id` INT NOT NULL,
  `poses` LONGBLOB NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ghosts_user_id_idx` (`user_id`),
  KEY `fk_ghosts_circuit_id_idx` (`circuit_id`),
  CONSTRAINT `fk_ghosts_circuit_id` FOREIGN KEY (`circuit_id`) REFERENCES `circuits` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ghosts_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tiles`;
CREATE TABLE `tiles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tile` INT NOT NULL,
  `x` INT NOT NULL,
  `y` INT NOT NULL,
  `checkpoint` INT NOT NULL DEFAULT 0,
  `circuit_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tiles_circuit_id_idx` (`circuit_id`),
  CONSTRAINT `fk_tiles_circuit_id` FOREIGN KEY (`circuit_id`) REFERENCES `circuits` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;

INSERT INTO `tiles` VALUES (1,6,0,0,0,1),(2,2,1,0,0,1),(3,7,2,0,0,1),(4,4,0,1,0,1),(5,4,2,1,0,1),(6,5,0,2,0,1),(7,3,1,2,0,1),(8,8,2,2,0,1),
(9,6,0,0,0,2),(10,7,1,0,0,2),(11,6,2,0,0,2),(12,7,3,0,0,2),(13,4,0,1,0,2),(14,4,1,1,0,2),(15,4,2,1,0,2),(16,4,3,1,0,2),(17,4,0,2,0,2),(18,5,1,2,0,2),(19,8,2,2,0,2),(20,4,3,2,0,2),(21,5,0,3,0,2),(22,2,1,3,0,2),(23,3,2,3,0,2),(24,8,3,3,0,2),
(25,6,1,0,0,3),(26,3,2,0,0,3),(27,3,3,0,0,3),(28,7,4,0,0,3),(29,6,0,1,0,3),(30,8,1,1,0,3),(31,4,4,1,0,3),(32,4,0,2,0,3),(33,4,4,2,0,3),(34,4,0,3,0,3),(35,4,4,3,0,3),(36,5,0,4,0,3),(37,2,1,4,0,3),(38,3,2,4,0,3),(39,3,3,4,0,3),(40,8,4,4,0,3),
(41,6,0,0,0,4),(42,3,1,0,0,4),(43,7,2,0,0,4),(44,5,0,1,0,4),(45,2,1,1,0,4),(46,8,2,1,0,4),
(47,6,0,0,0,5),(48,7,1,0,0,5),(49,6,3,0,0,5),(50,3,4,0,0,5),(51,7,5,0,0,5),(52,4,0,1,0,5),(53,5,1,1,0,5),(54,2,2,1,0,5),(55,8,3,1,0,5),(56,4,5,1,0,5),(57,4,0,2,0,5),(58,6,4,2,0,5),(59,8,5,2,0,5),(60,4,0,3,0,5),(61,4,4,3,0,5),(62,4,0,4,0,5),(63,4,4,4,0,5),(64,5,0,5,0,5),(65,3,1,5,0,5),(66,3,2,5,0,5),(67,3,3,5,0,5),(68,8,4,5,0,5),
(69,6,0,1,0,6),(70,3,1,1,0,6),(71,3,2,1,0,6),(72,3,3,1,0,6),(73,7,4,1,0,6),(74,5,0,2,0,6),(75,2,1,2,0,6),(76,3,2,2,0,6),(77,3,3,2,0,6),(78,8,4,2,0,6),
(79,6,0,0,0,7),(80,7,1,0,0,7),(81,6,3,0,0,7),(82,3,4,0,0,7),(83,3,5,0,0,7),(84,7,6,0,0,7),(85,4,0,1,0,7),(86,4,1,1,0,7),(87,4,3,1,0,7),(88,4,6,1,0,7),(89,4,0,2,0,7),(90,5,1,2,0,7),(91,2,2,2,0,7),(92,8,3,2,0,7),(93,6,5,2,0,7),(94,8,6,2,0,7),(95,4,0,3,0,7),(96,4,5,3,0,7),(97,5,0,4,0,7),(98,3,1,4,0,7),(99,3,2,4,0,7),(100,3,3,4,0,7),(101,3,4,4,0,7),(102,8,5,4,0,7),
(103,6,0,0,0,8),(104,7,1,0,0,8),(105,1,2,0,0,8),(106,6,3,0,0,8),(107,2,4,0,0,8),(108,3,5,0,0,8),(109,7,6,0,0,8),(110,4,0,1,0,8),(111,4,1,1,0,8),(112,1,2,1,0,8),(113,4,3,1,0,8),(114,1,4,1,0,8),(115,1,5,1,0,8),(116,4,6,1,0,8),(117,4,0,2,0,8),(118,5,1,2,0,8),(119,3,2,2,0,8),(120,8,3,2,0,8),(121,1,4,2,0,8),(122,6,5,2,0,8),(123,8,6,2,0,8),(124,4,0,3,0,8),(125,1,1,3,0,8),(126,1,2,3,0,8),(127,1,3,3,0,8),(128,1,4,3,0,8),(129,4,5,3,0,8),(130,1,6,3,0,8),(131,5,0,4,0,8),(132,3,1,4,0,8),(133,3,2,4,0,8),(134,3,3,4,0,8),(135,3,4,4,0,8),(136,8,5,4,0,8),(137,1,6,4,0,8),
(138,6,0,1,0,9),(139,2,1,1,0,9),(140,7,2,1,0,9),(141,5,0,2,0,9),(142,3,1,2,0,9),(143,8,2,2,0,9);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255) NULL,
  `public` TINYINT(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

INSERT INTO `users` VALUES (1,'sander','e02093b6edd9afeb94aa90ee90638b681d3f39ce8f016184238d39b07a2e253eede94bfbf1c35e9061d78425a517a78b0b8a21ce9d0372314dfc23dc792f87dd','sander@racing2d.be','Sander', 1),(2,'jan','a8b766462c783c08757c510f8b78b84b38532fd4b2c2fa47ab22154204e622e81496d49893255c0a466f5c6cfe5de77be5c26e8306ce0df077a1438e7c51cd71','jan@racing2d.be','Jan', 1),(3,'thomas','cad618d77e1c7e64f9069e67fd2782cc040a33559e4cd948efb81cf94630e106eb754fce1a7720d1da29dd6b5df2603ad8c7c4df3dd89077ef7e0d7b09c50109','thomas@racing2d.be','Thomas', 1),(4,'t0tec','c89686fe0393694344a44354d741f45e334ddd5b219f78c20d4e9cc39ade1a588ffff9535359872914f58993f6e77c77e5bf2c07451172222d860ca01e7d4850','t0tec@racing2d.be','totec', 1);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

alter table participants add unique index(user_id, tournament_id);
