CREATE DATABASE IF NOT EXISTS user_db;
USE user_db;
CREATE TABLE IF NOT EXISTS users (ID INT, username VARCHAR(20));
INSERT INTO users VALUES (1, 'admin');

CREATE DATABASE IF NOT EXISTS pass_db;
USE pass_db;
CREATE TABLE IF NOT EXISTS app_pass (ID INT, password VARCHAR(256), salt VARCHAR(128));

CREATE DATABASE IF NOT EXISTS generated_db;
USE generated_db;

CREATE TABLE IF NOT EXISTS e_keys (ID varchar(128), e_key VARCHAR(256), salt VARCHAR(256));
CREATE TABLE IF NOT EXISTS e_keys (ID varchar(128), password VARCHAR(256));
CREATE TABLE IF NOT EXISTS website_users (ID varchar(128), website VARCHAR(50), username VARCHAR(50));