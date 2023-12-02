#!/bin/bash

# Chemin relatif vers le répertoire lib de JavaFX
export PATH_TO_FX=./lib/javafx-sdk-21.0.1/lib

# Chemin relatif vers le JAR du pilote PostgreSQL
export PATH_TO_POSTGRESQL=./lib/postgresql-42.7.0.jar

# Ajoutez les chemins au PATH
export PATH=$PATH:$PATH_TO_FX:$PATH_TO_POSTGRESQL

# Répertoire de sortie pour les fichiers binaires
OUTPUT_DIR=./bin

# Créez le répertoire de sortie s'il n'existe pas
mkdir -p $OUTPUT_DIR

# Compilez votre application JavaFX avec le module PostgreSQL dans le répertoire de sortie
javac --module-path $PATH_TO_FX:$PATH_TO_POSTGRESQL --add-modules javafx.controls,javafx.fxml -d $OUTPUT_DIR Controller.java

# Exécutez votre application JavaFX à partir du répertoire de sortie avec le module PostgreSQL
java --module-path $PATH_TO_FX:$PATH_TO_POSTGRESQL --add-modules javafx.controls,javafx.fxml -cp $OUTPUT_DIR Controller
