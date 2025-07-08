DROP DATABASE IF EXISTS postgres; --https://www.w3schools.com/sql/sql_drop_db.asp

\connect words;

CREATE TABLE all_words (
    word VARCHAR(200) NOT NULL PRIMARY KEY
    );