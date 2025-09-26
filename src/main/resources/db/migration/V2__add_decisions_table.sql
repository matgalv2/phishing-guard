CREATE TABLE decisions (
   id           SERIAL PRIMARY KEY,
   sender       VARCHAR(255) NOT NULL,
   recipient    VARCHAR(255) NOT NULL,
   disposition  VARCHAR(50) NOT NULL,
   reason       VARCHAR(50) NOT NULL,
   urls         TEXT[] NOT NULL
);