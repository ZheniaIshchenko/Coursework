package com.example.coursework;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.coursework.Database.clients;

public class EditionClientController implements Initializable {


    @FXML
    protected TextField carBrandTextField;

    @FXML
    protected TextField carNumberTextField;

    @FXML
    protected TextField nameTextField;

    @FXML
    protected CheckBox isRegularClientCheckBox;

    @FXML
    protected CheckBox isOnParkingCheckBox;

    @FXML
    protected CheckBox isPaymentCheckBox;




    @FXML
    protected Label isRegularClient;

    @FXML
    protected Label isOnParking;

    @FXML
    protected Label isPayment;



    @FXML
    protected ComboBox<String> numbersBox;

    @FXML
    protected ComboBox<String> comboBoxEditionClient;



    @FXML
    protected Button editingButton;
    @FXML
    public Button deleteButton;


    private MainWindowController parent;

    Database database = new Database();

    protected Client choosed = null;
    int choosedClientId;

    AccessController childrenDeleteClientController;

    public void setParent(MainWindowController parent){
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tooltip regularClient = new Tooltip("Постійний клієнт");
        Tooltip onParking = new Tooltip("Чи знаходиться траспрот на парковці");
        Tooltip payment = new Tooltip("Оплата");

        isRegularClient.setTooltip(regularClient);
        isOnParking.setTooltip(onParking);
        isPayment.setTooltip(payment);

        try {
            comboBoxEditionClientVoid();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void  comboBoxEditionClientVoid() throws SQLException {
        comboBoxEditionClient.getItems().clear();
        String[] clientsToString = new String[clients.getSize()];
        int countClients = 0;
        for (int i = 1; i < 30; i++) {
            if (clients.getById(i) != null) {
                clientsToString[countClients] = clients.getById(i).toString();
                countClients++;
            }
        }
        comboBoxEditionClient.getItems().addAll(clientsToString);
    }

    public void comboBoxEditionClientOnAction() {
        editingButton.setDisable(comboBoxEditionClient.getValue() == null);
        deleteButton.setDisable(false);
        numbersBox.setDisable(false);
        nameTextField.setDisable(false);
        isRegularClientCheckBox.setDisable(false);
        carBrandTextField.setDisable(false);
        carNumberTextField.setDisable(false);
        isOnParkingCheckBox.setDisable(false);
        isPaymentCheckBox.setDisable(false);

        char[] comboBoxEditionClientChar;
            comboBoxEditionClientChar = comboBoxEditionClient.getValue().toCharArray();
        int clientNumber = Integer.parseInt((comboBoxEditionClientChar[1] + "" + comboBoxEditionClientChar[2]).strip());

        int countNumber = 0;
        String[] numbersTo30 = new String[30];
        for (int i = 1; i <= numbersTo30.length; i++){
            try {
                if (i == clients.getById(i).getId()){
                    if (i == clientNumber) {
                        numbersTo30[countNumber] = String.valueOf(i);
                        countNumber++;
                    }
                }
            } catch (NullPointerException e){
                numbersTo30[countNumber] = String.valueOf(i);
                countNumber++;
            }

        }

        String[] numbersTo30srt = new String[countNumber];
        System.arraycopy(numbersTo30, 0, numbersTo30srt,0, countNumber);
        numbersBox.getItems().clear();
        numbersBox.getItems().addAll(numbersTo30srt);

        for (int i = 1; i < 30; i++) {
            if (i == clientNumber) {
                choosed = clients.getById(i);
                choosedClientId = i;
            }
        }

        numbersBox.setValue(String.valueOf(choosed.getId()));
        nameTextField.setText(choosed.getNameOfClient());
        isRegularClientCheckBox.setSelected(choosed.isRegularCostumer());
        carBrandTextField.setText(choosed.getCarBrand());
        carNumberTextField.setText(choosed.getCarNumber());
        isOnParkingCheckBox.setSelected(choosed.isOnParking());
        isPaymentCheckBox.setSelected(choosed.isPayment());
    }

    public void setEditing() throws SQLException {
        int numberBoxValue = Integer.parseInt(numbersBox.getValue());
        Client client = new Client(numberBoxValue, isRegularClientCheckBox.isSelected(), isOnParkingCheckBox.isSelected(),isPaymentCheckBox.isSelected(),
                nameTextField.getText(), carBrandTextField.getText(), carNumberTextField.getText());
        database.changeClientDB(choosedClientId, client);
        database.updateClients();

        parent.refreshListDatabase();
        Stage stage = (Stage) editingButton.getScene().getWindow();
        stage.close();
        parent.editButton.setDisable(false);
    }

    public void accessWindow() throws IOException {
        deleteButton.setDisable(true);
        editingButton.setDisable(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("access-window.fxml"));

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Додати нового клієнта");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("parking.png"))));
        stage.setX(deleteButton.getLayoutX());
        stage.setY(deleteButton.getLayoutY());
        stage.setResizable(false);
        stage.show();

        childrenDeleteClientController = loader.getController();
        childrenDeleteClientController.setParent(this);
        stage.setOnCloseRequest(windowEvent -> {
            deleteButton.setDisable(false);
            editingButton.setDisable(false);
        });
    }

    public void deleteClient() throws SQLException {
        database.deleteClientDB(choosedClientId);
        parent.refreshListDatabase();
        Stage stage = (Stage) editingButton.getScene().getWindow();
        stage.close();
        parent.editButton.setDisable(false);
    }

    public void deleteButtonOnAction() throws IOException {
        Notifications notificationError = Notifications.create();
        notificationError.title("Помилка вводу");
        notificationError.text("Є пусті поля або ви ввели спецсимвол/цифру");
        notificationError.hideAfter(Duration.seconds(20));
        accessWindow();
        childrenDeleteClientController.label.setText("Ви дійсно хочете відалити клієнта:");
        childrenDeleteClientController.Client.setText(clients.getById(choosedClientId).toString());
        childrenDeleteClientController.access = true;

        parent.refreshListDatabase();
        Stage stage = (Stage) editingButton.getScene().getWindow();
        stage.close();
        parent.editButton.setDisable(false);
    }

    public void editingButtonOnAction() throws IOException{
        Notifications notificationError = Notifications.create();
        notificationError.title("Помилка вводу");
        notificationError.text("Є пусті поля або ви ввели спецсимвол/цифру");
        notificationError.hideAfter(Duration.seconds(20));
        if (nameTextField.getText().matches("[\\p{L}| ]+") && !(numbersBox.getValue() == null || nameTextField.getText().isBlank() || carBrandTextField.getText().isBlank() || carNumberTextField.getText().isBlank())) {
            accessWindow();
            childrenDeleteClientController.label.setText("Ві дійсно хочете відредагувати клієнта:");
            int numberBoxValue = Integer.parseInt(numbersBox.getValue());
            Client editingClient = new Client(numberBoxValue,isRegularClientCheckBox.isSelected(),isOnParkingCheckBox.isSelected(),
                    isPaymentCheckBox.isSelected(), nameTextField.getText(),
                    carBrandTextField.getText(), carNumberTextField.getText());
            childrenDeleteClientController.Client.setText(editingClient.toString());

            parent.refreshListDatabase();
            Stage stage = (Stage) editingButton.getScene().getWindow();
            stage.close();
            parent.editButton.setDisable(false);
        } else {
            notificationError.showError();
        }
    }
}
