DROP TABLE IF EXISTS "post";
DROP TABLE IF EXISTS "user";

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
    FOREIGN KEY (iduser) REFERENCES "user"(iduser)
);


CREATE TABLE "comment"(
    idcomment SERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    iduser INT,
    idpost INT,

    FOREIGN KEY (iduser) REFERENCES "user"(iduser),
    FOREIGN KEY (idpost) REFERENCES "post"(idpost)
);


CREATE TABLE "reaction"(
    idreaction SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    iduser INT,
    idpost INT,
    idcomment INT,

    FOREIGN KEY (iduser) REFERENCES "user"(iduser),
    FOREIGN KEY (idpost) REFERENCES "post"(idpost),
    FOREIGN KEY (idcomment) REFERENCES "comment"(idcomment)
);

INSERT INTO "user" (username, mail, "password") 
VALUES ('u1', 'utilisateur1@example.com', 'mdp1');
