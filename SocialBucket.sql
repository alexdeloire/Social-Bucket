DROP TABLE IF EXISTS user;


CREATE TABLE "user" (
    pseudo VARCHAR(50) NOT NULL,
    mail VARCHAR(100) NOT NULL,
    mdp VARCHAR(50) NOT NULL
);

INSERT INTO "user" (pseudo, mail, mdp) 
VALUES 
('u1', 'utilisateur1@example.com', 'mdp1')