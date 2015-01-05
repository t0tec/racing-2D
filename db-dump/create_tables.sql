CREATE DATABASE IF NOT EXISTS racing_2d;

USE racing_2d;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema racing-2D
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users` ;

CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255) NOT NULL,
  `public` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `circuits`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `circuits` ;

CREATE TABLE IF NOT EXISTS `circuits` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `designer` INT NOT NULL,
  `direction` ENUM('UP','DOWN','RIGHT','LEFT') NOT NULL,
  `rows` INT NOT NULL,
  `columns` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_circuits_designer` (`designer` ASC),
  CONSTRAINT `fk_circuits_designer`
    FOREIGN KEY (`designer`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ghosts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ghosts` ;

CREATE TABLE IF NOT EXISTS `ghosts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `time` INT NOT NULL,
  `user_id` INT NOT NULL,
  `circuit_id` INT NOT NULL,
  `poses` LONGBLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ghosts_user_id_idx` (`user_id` ASC),
  INDEX `fk_ghosts_circuit_id_idx` (`circuit_id` ASC),
  CONSTRAINT `fk_ghosts_circuit_id`
    FOREIGN KEY (`circuit_id`)
    REFERENCES `circuits` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ghosts_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tournaments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tournaments` ;

CREATE TABLE IF NOT EXISTS `tournaments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `formule` ENUM('TOTAL','FASTEST','LONGEST') NOT NULL,
  `user_id` INT NOT NULL,
  `max_players` INT NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `_idx` (`user_id` ASC),
  CONSTRAINT `fk_users_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `participants`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `participants` ;

CREATE TABLE IF NOT EXISTS `participants` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tournament_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `u_id_idx` (`user_id` ASC),
  INDEX `t_id_idx` (`tournament_id` ASC),
  CONSTRAINT `u_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `t_id`
    FOREIGN KEY (`tournament_id`)
    REFERENCES `tournaments` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `races`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `races` ;

CREATE TABLE IF NOT EXISTS `races` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tournament_id` INT NOT NULL,
  `circuit_id` INT NOT NULL,
  `laps` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `circuit_id_idx` (`circuit_id` ASC),
  INDEX `tournament_id_idx` (`tournament_id` ASC),
  CONSTRAINT `circuit_id`
    FOREIGN KEY (`circuit_id`)
    REFERENCES `circuits` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tournament_id`
    FOREIGN KEY (`tournament_id`)
    REFERENCES `tournaments` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `results`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `results` ;

CREATE TABLE IF NOT EXISTS `results` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `time` INT NOT NULL,
  `lap_number` INT NOT NULL,
  `race_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `race_id_idx` (`race_id` ASC),
  INDEX `user_id_idx` (`user_id` ASC),
  CONSTRAINT `race_id`
    FOREIGN KEY (`race_id`)
    REFERENCES `races` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tiles` ;

CREATE TABLE IF NOT EXISTS `tiles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tile` INT NOT NULL,
  `x` INT NOT NULL,
  `y` INT NOT NULL,
  `checkpoint` INT NOT NULL DEFAULT 0,
  `circuit_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tiles_1_idx` (`circuit_id` ASC),
  CONSTRAINT `fk_tiles_circuit_id`
    FOREIGN KEY (`circuit_id`)
    REFERENCES `circuits` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `favorite_circuits`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `favorite_circuits` ;

CREATE TABLE IF NOT EXISTS `favorite_circuits` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `circuit_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_favorite_circuits_users_user_id_idx` (`user_id` ASC),
  INDEX `fk_favorite_circuits_circuit_circuit_id_idx` (`circuit_id` ASC),
  CONSTRAINT `fk_favorite_circuits_users_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_favorite_circuits_circuit_circuit_id`
    FOREIGN KEY (`circuit_id`)
    REFERENCES `circuits` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `events`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `events` ;

CREATE TABLE IF NOT EXISTS `events` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(255) NOT NULL,
  `timestamp` DATE NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_events_users_user_id_idx` (`user_id` ASC),
  CONSTRAINT `fk_events_users_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obstacles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `obstacles` ;

CREATE TABLE IF NOT EXISTS `obstacles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `place` ENUM('LEFT','RIGHT') NOT NULL,
  `tile_id` INT NOT NULL,
  `obstacle_type` ENUM('EGGPLANT','MELON','PADDO','STRAWBERRY') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_obstacles_tiles_tile_id_idx` (`tile_id` ASC),
  CONSTRAINT `fk_obstacles_tiles_tile_id`
    FOREIGN KEY (`tile_id`)
    REFERENCES `tiles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

alter table participants add unique index(user_id,tournament_id);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
