package com.example.coursework;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AccessController extends EditionClientController  implements Initializable {

    public Button yesButton;
    public Button noButton;
    public Label label;
    public PasswordField passwordField;
    public boolean access = false;
    private EditionClientController parent;
    public void setParent(EditionClientController parent){
        this.parent = parent;
    }
    private AddClientController parentAdd;
    public void setParent(AddClientController parent){
        this.parentAdd = parent;
    }
    
    @FXML
    Label Client = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public String getHash(String string){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = md.digest(string.getBytes());

        return new BigInteger(1, digest).toString(16);
    }

    public void yesButtonOnAction() throws SQLException {
        Notifications notificationError = Notifications.create();
        notificationError.title("Неправильний пароль").text("Введіть правильний пароль").hideAfter(Duration.seconds(20));

        Notifications notificationAddClient = Notifications.create();
        notificationAddClient.title("Операція успішна").text("Клієнт доданий").hideAfter(Duration.seconds(20));

        Notifications notificationDeleteClient = notificationAddClient.text("Клієнт відалений");
        Notifications notificationEditingClient = notificationAddClient.text("Клієнт відредагований");

        if (getHash(passwordField.getText()).equals(database.getPasswordDB())) {
            try {
                if(access){
                    parent.deleteClient();
                } else {
                    parent.setEditing();
                }
            } catch (NullPointerException e) {
                parentAdd.addClient();
            }
            Stage stage = (Stage) yesButton.getScene().getWindow();
            stage.close();
            try {
                parent.editingButton.setDisable(false);
                parent.deleteButton.setDisable(false);
                if(access){
                    notificationDeleteClient.show();
                } else {
                    notificationEditingClient.show();
                }

            } catch (NullPointerException e) {
                parentAdd.addClientButton.setDisable(false);
                notificationAddClient.show();
            }
        } else {
            notificationError.showError();
        }
    }

    public void noButtonOnAction(){
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
        try {
            parent.editingButton.setDisable(false);
            parent.deleteButton.setDisable(false);
        }catch (NullPointerException e){
            parentAdd.addClientButton.setDisable(false);
        }
    }
}
