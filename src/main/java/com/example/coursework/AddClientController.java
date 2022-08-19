package com.example.coursework;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.coursework.Database.clients;
import static com.example.coursework.ParkDatabaseController.database;


public class AddClientController implements Initializable {

    @FXML
    protected Label RegularClient;

    @FXML
    protected Label OnParking;

    @FXML
    protected Label Payment;

    @FXML
    protected TextField nameTextField;

    @FXML
    protected TextField carBrandTextField;

    @FXML
    protected TextField carNumberTextField;

    @FXML
    protected CheckBox isRegularCostumer;

    @FXML
    protected CheckBox isOnParking;

    @FXML
    protected CheckBox isPayment;

    @FXML
    protected Button addClientButton;

    @FXML
    protected AnchorPane anchorPane;

    @FXML
    protected ComboBox<String> numberComboBox;
    private ParkDatabaseController parent;
    AccessController childrenDeleteClientController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tooltip regularClient = new Tooltip("Постійний клієнт");
        Tooltip onParking = new Tooltip("Чи знаходиться траспрот на парковці");
        Tooltip payment = new Tooltip("Оплата");

        RegularClient.setTooltip(regularClient);
        OnParking.setTooltip(onParking);
        Payment.setTooltip(payment);

        int countNumber = 0;
        String[] numbersTo30 = new String[30];
        for (int i = 1; i <= numbersTo30.length; i++){
            try {
                if (i != clients.getById(i).getId()){
                    numbersTo30[countNumber] = String.valueOf(i);
                    countNumber++;
                }
            } catch (NullPointerException e){
                numbersTo30[countNumber] = String.valueOf(i);
                countNumber++;
            }

        }

        String[] numbersTo30srt = new String[countNumber];
        System.arraycopy(numbersTo30, 0, numbersTo30srt,0, countNumber);
        numberComboBox.getItems().clear();
        numberComboBox.getItems().addAll(numbersTo30srt);

    }

    public void addClientButtonOnAction() throws IOException {
        Notifications notificationError = Notifications.create();
        notificationError.title("Помилка вводу");
        notificationError.text("Є пусті поля або ви ввели спецсимвол/цифру");
        notificationError.hideAfter(Duration.seconds(20));

        if (nameTextField.getText().matches("[\\p{L}| ]+") && !(numberComboBox.getValue() == null || nameTextField.getText().isBlank() || carBrandTextField.getText().isBlank() || carNumberTextField.getText().isBlank())) {
            int numberBoxValue = Integer.parseInt(numberComboBox.getValue());
            addClientButton.setDisable(true);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("access.fxml"));

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Додати нового клієнта");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("parking.png"))));
            stage.setX(addClientButton.getLayoutX());
            stage.setY(addClientButton.getLayoutY());
            stage.setResizable(false);
            stage.show();

            childrenDeleteClientController = loader.getController();
            childrenDeleteClientController.setParent(this);
            childrenDeleteClientController.label.setText("Ви дійсно хочете додати клієнта:");
            Client addClientExample = new Client(numberBoxValue,  isRegularCostumer.isSelected(),isOnParking.isSelected(), isPayment.isSelected(),nameTextField.getText(), carBrandTextField.getText(), carNumberTextField.getText());
            childrenDeleteClientController.Client.setText(addClientExample.toString());

            stage.setOnCloseRequest(windowEvent -> addClientButton.setDisable(false));
            parent.refreshListDatabase();

            Stage Addstage = (Stage) addClientButton.getScene().getWindow();
            Addstage.close();
            parent.addClientButton.setDisable(false);
        } else {
            notificationError.showError();
        }

    }

    public void addClient() throws SQLException {
        int numberBoxValue = Integer.parseInt(numberComboBox.getValue());
        Client client = new Client(numberBoxValue, isRegularCostumer.isSelected(), isOnParking.isSelected(),isPayment.isSelected(), nameTextField.getText(), carBrandTextField.getText(),    carNumberTextField.getText());
        database.addClientDB(client);
        database.updateClients();
        parent.refreshListDatabase();
        Stage stage = (Stage) addClientButton.getScene().getWindow();
        stage.close();
        parent.addClientButton.setDisable(false);
    }

    public void setParent(ParkDatabaseController parent) {
        this.parent = parent;
    }

}