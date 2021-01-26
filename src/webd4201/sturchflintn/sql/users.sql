--Nick Sturch-Flint (100303769)
--January 26, 2021
--WEBD4201
--Creates a DB Table for all generic users

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS users;
CREATE TABLE users(
id LONG PRIMARY KEY,
password VARCHAR(40) NOT NULL,
first_name VARCHAR(128),
last_name VARCHAR(128),
email_address VARCHAR(255) UNIQUE,
last_access TIMESTAMP,
enrol_date TIMESTAMP,
enabled BOOLEAN,
type VARCHAR(2)
);


SELECT * FROM users;