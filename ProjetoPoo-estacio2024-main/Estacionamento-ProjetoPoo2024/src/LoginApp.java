import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginApp extends Application {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/seuBancoDeDados";
    private static final String DB_USER = "seuUsuario";
    private static final String DB_PASSWORD = "suaSenha";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        // Create the GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        // Add components
        Label titleLabel = new Label("GamerSpaceCAR");
        titleLabel.setStyle("-fx-text-fill: #ECF0F1; -fx-font-size: 24px; -fx-font-weight: bold;");
        Label userLabel = new Label("Usuario:");
        userLabel.setStyle("-fx-text-fill: #ECF0F1;");
        grid.add(userLabel, 0, 1);
        TextField userTextField = new TextField();
        userTextField.setStyle("-fx-background-color: #34495E; -fx-text-fill: #ECF0F1; -fx-border-color: transparent;");
        grid.add(userTextField, 1, 1);
        Label pwLabel = new Label("Senha:");
        pwLabel.setStyle("-fx-text-fill: #ECF0F1;");
        grid.add(pwLabel, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setStyle("-fx-background-color: #34495E; -fx-text-fill: #ECF0F1; -fx-border-color: transparent;");
        grid.add(pwBox, 1, 2);
        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-padding: 10px;");
        grid.add(btnLogin, 1, 3);
        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-padding: 10px;");
        grid.add(btnRegister, 1, 4);
        final Label message = new Label();
        message.setStyle("-fx-text-fill: #ECF0F1;");
        grid.add(message, 1, 5);

        // Add action handler for the login button
        btnLogin.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            // Simple login logic
            if (validateLogin(username, password)) {
                message.setText("Login successful!");
                // Proceed to the main application
                showParkingApp(primaryStage);
            } else {
                message.setText("Invalid credentials");
            }
        });

        // Add action handler for the register button
        btnRegister.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            if (userExists(username)) {
                message.setText("Username already exists");
            } else {
                registerUser(username, password);
                message.setText("User registered successfully!");
            }
        });

        // Create a root layout to center the GridPane
        VBox root = new VBox(10, titleLabel, grid);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #34495E; -fx-padding: 40px;");

        // Create the Scene and show the stage
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error validating login: " + e.getMessage());
            return false;
        }
    }

    private boolean userExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    private void registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private void showParkingApp(Stage primaryStage) {
        EstacionamentoApp estacionamentoApp = new EstacionamentoApp();
        try {
            estacionamentoApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
