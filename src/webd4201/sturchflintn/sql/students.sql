--Nick Sturch-Flint (100303769)
--January 26, 2021
--WEBD4201 
--Creates a DB Table for Students

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS students;
CREATE TABLE students (
id LONG FOREIGN KEY,
program_code VARCHAR(4),
program_description VARCHAR(255),
year INT
);


SELECT * FROM students;