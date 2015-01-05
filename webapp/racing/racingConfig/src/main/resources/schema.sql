-- CREATE USERS TABLE --
CREATE TABLE IF NOT EXISTS users (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  email varchar(255) DEFAULT NULL,
  full_name varchar(255) DEFAULT NULL,
  public tinyint DEFAULT 1
);

-- CREATE CIRCUITS TABLE --
CREATE TABLE IF NOT EXISTS circuits (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  name varchar(255) NOT NULL,
  designer int NOT NULL,
  direction varchar(255) DEFAULT 'UP', -- Replaced enum(MySQL) to VARCHAR ==> (no enum support in HSQLDB)
  rows int NOT NULL,
  columns int NOT NULL,
  FOREIGN KEY (designer) REFERENCES users (id)
);

-- CREATE TILES TABLE --
CREATE TABLE IF NOT EXISTS tiles (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  tile int NOT NULL,
  x int NOT NULL,
  y int NOT NULL,
  checkpoint int NOT NULL,
  circuit_id int NOT NULL,
  FOREIGN KEY (circuit_id) REFERENCES circuits (id)
);

-- CREATE GHOSTS TABLE --
CREATE TABLE IF NOT EXISTS ghosts (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  time int NOT NULL,
  user_id int NOT NULL,
  circuit_id int NOT NULL,
  poses longvarbinary NOT NULL, -- longvarbinary(HSQLDB) == longblob(MySQL)
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (circuit_id) REFERENCES circuits (id)
);

-- CREATE TOURNAMENTS TABLE --
CREATE TABLE IF NOT EXISTS tournaments (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  name varchar(255) NOT NULL,
  formule varchar(255) NOT NULL, -- Replaced enum(MySQL) to VARCHAR ==> (no enum support in HSQLDB)
  user_id int NOT NULL,
  max_players int NOT NULL,
  date datetime NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id)
);

-- CREATE RACES TABLE --
CREATE TABLE IF NOT EXISTS races (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  tournament_id int NOT NULL,
  circuit_id int NOT NULL,
  laps int NOT NULL,
  FOREIGN KEY (circuit_id) REFERENCES circuits (id),
  FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

-- CREATE PARTICIPANTS TABLE --
CREATE TABLE IF NOT EXISTS participants (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  tournament_id int NOT NULL,
  user_id int NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

-- CREATE RESULTS TABLE --
CREATE TABLE IF NOT EXISTS results (
  id int GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  time int NOT NULL,
  lap_number int NOT NULL,
  race_id int NOT NULL,
  user_id int NOT NULL,
  FOREIGN KEY (race_id) REFERENCES races (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);

ALTER TABLE participants ADD CONSTRAINT "unique_participants" UNIQUE (user_id, tournament_id);