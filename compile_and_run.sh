#!/bin/bash

# Chemin relatif vers le répertoire lib de JavaFX
export PATH_TO_FX=./lib/javafx-sdk-21.0.1/lib

# Ajoutez le chemin au PATH
export PATH=$PATH:$PATH_TO_FX

# Répertoire de sortie pour les fichiers binaires
OUTPUT_DIR=./bin

# Créez le répertoire de sortie s'il n'existe pas
mkdir -p $OUTPUT_DIR

# Compilez votre application JavaFX dans le répertoire de sortie
javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -d $OUTPUT_DIR Controller.java

# Exécutez votre application JavaFX à partir du répertoire de sortie
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp $OUTPUT_DIR Controller
