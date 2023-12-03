DROP TABLE IF EXISTS "user";


CREATE TABLE "user" (
    username VARCHAR(50) NOT NULL,
    mail VARCHAR(100) NOT NULL,
    "password" VARCHAR(50) NOT NULL
);

INSERT INTO "user" (username, mail, "password") 
VALUES ('u1', 'utilisateur1@example.com', 'mdp1')