TABLE IF EXISTS "reaction" CASCADE;
DROP TABLE IF EXISTS "comment" CASCADE;
DROP TABLE IF EXISTS "post" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "reactioncomment" CASCADE;

CREATE TABLE "user" (
    iduser SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    mail VARCHAR(100) NOT NULL,
    "password" VARCHAR(50) NOT NULL
);

CREATE TABLE "post" (
    idpost SERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    type VARCHAR(255), -- Utilisez BYTEA pour stocker les données binaires de l'image
    file BYTEA,  -- Utilisez BYTEA pour stocker les données binaires du fichier
    filename VARCHAR(255),
    iduser INT, -- Ajoutez la colonne iduser dans la table "post"
    FOREIGN KEY (iduser) REFERENCES "user"(iduser) ON DELETE CASCADE
);


CREATE TABLE "comment"(
    idcomment SERIAL PRIMARY KEY,
    idparentcomment INT,
    content VARCHAR(255) NOT NULL,
    iduser INT,
    idpost INT,

    FOREIGN KEY (iduser) REFERENCES "user"(iduser) ,
    FOREIGN KEY (idpost) REFERENCES "post"(idpost) ON DELETE CASCADE,
    FOREIGN KEY (idparentcomment) REFERENCES "comment"(idcomment) ON DELETE CASCADE
);


CREATE TABLE "reaction"(
    idreaction SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    iduser INT,
    idpost INT,

    FOREIGN KEY (iduser) REFERENCES "user"(iduser),
    FOREIGN KEY (idpost) REFERENCES "post"(idpost)ON DELETE CASCADE
);

CREATE TABLE "reactioncomment" (
    idreaction SERIAL PRIMARY KEY, 
    type VARCHAR(255) NOT NULL,
    iduser INT,
    idcomment INT,

    FOREIGN KEY (iduser) REFERENCES "user"(iduser),
    FOREIGN KEY (idcomment) REFERENCES "comment"(idcomment) ON DELETE CASCADE
);


INSERT INTO "user" (username, mail, "password") 
VALUES ('u1', 'utilisateur1@example.com', 'mdp1');
