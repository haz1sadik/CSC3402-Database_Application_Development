package LAB2;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;


public class UserRegistrationController extends Application {
    @FXML
    Button btnRegister, btnCancel;
    @FXML
    TextField tfName, tfEmail, tfPhone, tfAddress;
    @FXML
    PasswordField pwdPassword, pwdConfirmPassword;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(UserRegistrationController.class.getResource("UserRegistrationView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Registration");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void registerUser(){
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pwdPassword.getText());
        String confirmPassword = String.valueOf(pwdConfirmPassword.getText());

        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter all fields");
            alert.showAndWait();
            return;
        }
        if (!password.equals(confirmPassword)){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Confirm password does not match");
            alert.showAndWait();
            return;
        }
        addUserToDatabase(name, email, phone, address, password);
    }

    private void addUserToDatabase(String name, String email, String phone, String address, String password) {
        User user;
        final String DB_URL = "jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:FSKTM";
        final String USERNAME = "C223877";
        final String PASSWORD = "223877";

        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            String query = "INSERT INTO USERS(NAME, EMAIL, PHONE, ADDRESS, PASSWORD) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText("Successful Registration");

                alert.showAndWait();
                tfName.clear();
                tfEmail.clear();
                tfPhone.clear();
                tfAddress.clear();
                pwdPassword.clear();
                pwdConfirmPassword.clear();
            }
            connection.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
