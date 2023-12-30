DROP TABLE IF EXISTS "reaction" CASCADE;
DROP TABLE IF EXISTS "comment" CASCADE;
DROP TABLE IF EXISTS "post" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "reactioncomment" CASCADE;
DROP TABLE IF EXISTS "card" CASCADE;
DROP TABLE IF EXISTS "wallet" CASCADE;

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

CREATE TABLE card (
    iduser INT NOT NULL,
    idcard SERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    holder VARCHAR(255) NOT NULL,
    valid_thru DATE NOT NULL,
    cvc VARCHAR(3) NOT NULL,
    FOREIGN KEY (iduser) REFERENCES "user"(iduser)ON DELETE CASCADE
);

CREATE TABLE wallet (
    idwallet SERIAL PRIMARY KEY,
    balance FLOAT NOT NULL,
    iddefaultcard INT,
    secret_code INT NOT NULL,
	iduser INT NOT NULL,
    
	FOREIGN KEY (iduser) REFERENCES "user"(iduser) ON DELETE CASCADE,
    FOREIGN KEY (iddefaultcard) REFERENCES "card"(idcard) 
);
INSERT INTO "user" (username, mail, "password") 
VALUES ('u1', 'utilisateur1@example.com', 'mdp1');

INSERT INTO wallet (balance, iduser, secret_code)
VALUES (
    100.0,
    (SELECT iduser FROM "user" WHERE username = 'u1'),
    123
);