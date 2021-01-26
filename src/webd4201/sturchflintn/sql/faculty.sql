--Nick Sturch-Flint (100303769)
--January 26, 2021
--WEBD4201
--Creates a DB Table for faculty

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS faculty;
CREATE TABLE faculty (
id LONG FOREIGN KEY,
school_code VARCHAR(5),
school_description VARCHAR(255),
office VARCHAR(5),
phone_extension INT
);


SELECT * FROM faculty;