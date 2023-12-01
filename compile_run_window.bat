@echo off

rem Chemin relatif vers le répertoire lib de JavaFX
set "PATH_TO_FX=.\lib\javafx-sdk-21.0.1\lib"

rem Chemin relatif vers le JAR du pilote PostgreSQL
set "PATH_TO_POSTGRESQL=.\lib\postgresql-42.7.0.jar"

rem Ajoutez les chemins au PATH
set "PATH=%PATH%;%PATH_TO_FX%;%PATH_TO_POSTGRESQL%"

rem Répertoire de sortie pour les fichiers binaires
set "OUTPUT_DIR=.\bin"

rem Créez le répertoire de sortie s'il n'existe pas
mkdir %OUTPUT_DIR%

rem Compilez votre application JavaFX avec le module PostgreSQL dans le répertoire de sortie
javac --module-path %PATH_TO_FX%;%PATH_TO_POSTGRESQL% --add-modules javafx.controls,javafx.fxml -d %OUTPUT_DIR% Controller.java

rem Exécutez votre application JavaFX à partir du répertoire de sortie avec le module PostgreSQL
java --module-path %PATH_TO_FX%;%PATH_TO_POSTGRESQL% --add-modules javafx.controls,javafx.fxml -cp %OUTPUT_DIR% Controller
