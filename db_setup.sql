CREATE DATABASE IF NOT EXISTS user_db;
USE user_db;
CREATE TABLE IF NOT EXISTS users (ID INT, username VARCHAR(20));
INSERT INTO users VALUES (1, 'admin');

CREATE DATABASE IF NOT EXISTS pass_db;
USE pass_db;
CREATE TABLE IF NOT EXISTS app_pass (ID INT, password VARCHAR(256), salt VARCHAR(128));