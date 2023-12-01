import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        // UI components
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());

        statusLabel = new Label();

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, statusLabel);
        layout.setSpacing(10);

        // Scene
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void handleLogin() {
        // Implement your login logic here
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Replace this with your actual login logic
        if ("User1".equals(username) && "password".equals(password)) {
            statusLabel.setText("Login successful");
        } else {
            statusLabel.setText("Login failed. Please try again.");
        }
    }
}
