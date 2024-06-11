CREATE TYPE COLOR AS ENUM ('GREEN', 'ORANGE', 'WHITE');
CREATE TYPE COUNTRY AS ENUM ('GERMANY', 'SPAIN', 'CHINA', 'VATICAN', 'JAPAN');
CREATE TYPE MOVIE_GENRE AS ENUM ('WESTERN', 'DRAMA', 'TRAGEDY', 'FANTASY');
CREATE TYPE MPAA_RATING AS ENUM ('PG', 'R', 'NC_17');

CREATE TABLE IF NOT EXISTS USERS
(
    id                SERIAL PRIMARY KEY,
    username          TEXT    NOT NULL,
    password          TEXT    NOT NULL,
    salt              TEXT    NOT NULL,
    superuser         BOOLEAN NOT NULL DEFAULT FALSE,
    registration_date TIMESTAMP        DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS MOVIE
(
    id            SERIAL PRIMARY KEY,
    user_id       INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id),
    name          TEXT             NOT NULL,
    creation_date TIMESTAMP        NOT NULL DEFAULT now(),
    oscars_count  INTEGER          NOT NULL CHECK ( oscars_count > 0 ),
    genre         MOVIE_GENRE      NOT NULL,
    mpaa_rating   MPAA_RATING,
    x             FLOAT            NOT NULL,
    y             DOUBLE PRECISION NOT NULL CHECK ( y <= 274 )
);

CREATE TABLE IF NOT EXISTS PERSON
(
    movie_id    INTEGER UNIQUE,
    FOREIGN KEY (movie_id) REFERENCES MOVIE (id),
    name        TEXT NOT NULL,
    birthday    TIMESTAMP,
    hair_color  COLOR,
    nationality COUNTRY
);
