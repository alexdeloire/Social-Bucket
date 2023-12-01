@echo off

rem Chemin relatif vers le répertoire lib de JavaFX
set "PATH_TO_FX=.\lib\javafx-sdk-21.0.1\lib"

rem Ajoutez le chemin au PATH
set "PATH=%PATH%;%PATH_TO_FX%"

rem Répertoire de sortie pour les fichiers binaires
set "OUTPUT_DIR=.\bin"

rem Créez le répertoire de sortie s'il n'existe pas
mkdir %OUTPUT_DIR%

rem Compilez votre application JavaFX dans le répertoire de sortie
javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -d %OUTPUT_DIR% Controller.java

rem Exécutez votre application JavaFX à partir du répertoire de sortie
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp %OUTPUT_DIR% Controller
