package com.polytech.SocialBucket;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller extends Application {

    private TextField nameField;
    private PasswordField passwordField;
    private Label statusLabel;
    private UserFacade userFacade = UserFacade.getInstance();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        // UI components
        Label nameLabel = new Label("Name");
        nameField = new TextField();

        Label passwordLabel = new Label("Password");
        passwordField = new PasswordField();

        /*Image appImage = new Image("path/to/your/app/image.png"); // Remplacez "path/to/your/app/image.png" par le chemin de votre image
        ImageView imageView = new ImageView(appImage);
        imageView.setFitHeight(100); // Ajustez la hauteur selon vos besoins
        imageView.setPreserveRatio(true);*/

        Label socialBucketLabel = new Label("Social Bucket");
        socialBucketLabel.setStyle("-fx-font-size: 18px;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #0073e6; -fx-text-fill: white;");
        loginButton.setOnAction(e -> handleLogin());

        statusLabel = new Label();

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        //gridPane.add(imageView, 0, 0, 2, 1);
        gridPane.add(socialBucketLabel, 3, 1);
        gridPane.add(nameLabel, 2, 2);
        gridPane.add(nameField, 3, 2);
        gridPane.add(passwordLabel, 2, 3);
        gridPane.add(passwordField, 3, 3);
        gridPane.add(loginButton, 3, 4);
        gridPane.add(statusLabel, 3, 5);

        // Scene
        Scene scene = new Scene(gridPane, 400, 300); // Ajustez la taille selon vos besoins
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void handleLogin() {
        String username = nameField.getText();
        String password = passwordField.getText();

        User user = userFacade.login(username, password);
        if(user != null) {
            statusLabel.setText("Hello " + user.getUsername() + "!");
        } else {
            statusLabel.setText("Login failed, try again!");
        }
    }
}
