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

CREATE USER IF NOT EXISTS 'reader_userDB'@'localhost' IDENTIFIED BY '9XE6g^#^5VB';
CREATE USER IF NOT EXISTS 'reader_passDB'@'localhost' IDENTIFIED BY 'Gy8KS7^7%a!';
CREATE USER IF NOT EXISTS 'writer_passDB'@'localhost' IDENTIFIED BY '56zj95!34u3$VB$t';

GRANT SELECT ON user_db.users TO 'reader_userDB'@'localhost';
GRANT SELECT ON pass_db.app_pass TO 'reader_passDB'@'localhost';
GRANT INSERT ON pass_db.app_pass TO 'writer_passDB'@'localhost';

CREATE USER IF NOT EXISTS 'reader_uspa'@'localhost' IDENTIFIED BY 'd7*jNc5dZz9z@';
CREATE USER IF NOT EXISTS 'reader_senes'@'localhost' IDENTIFIED BY '#js7qBD5#Y8xG';
CREATE USER IF NOT EXISTS 'writer_generatedDB'@'localhost' IDENTIFIED BY '67&7tP4f@PYcx';

GRANT SELECT ON generated_db.website_users TO 'reader_uspa'@'localhost';
GRANT SELECT ON generated_db.e_passwords TO 'reader_uspa'@'localhost';
GRANT SELECT ON generated_db.e_keys TO 'reader_senes'@'localhost';
GRANT INSERT ON generated_db.website_users TO 'writer_generatedDB'@'localhost';
GRANT INSERT ON generated_db.e_passwords TO 'writer_generatedDB'@'localhost';
GRANT INSERT ON generated_db.e_keys TO 'writer_generatedDB'@'localhost';