package com.example.coursework;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.coursework.Database.clients;

public class ParkDatabaseController implements Initializable {

    public TextField searchTextField;
    @FXML
    protected GridPane listDatabase;

    @FXML
    protected Button addClientButton;

    Label name = new Label();
    Label regularCostumer = new Label();
    Label carBrand = new Label();
    Label carNumber = new Label();
    Label onParking = new Label();
    Label payment = new Label();

    @FXML
    protected Button editButton;

    @FXML
    protected Label isRegularClient;

    @FXML
    protected Label isOnParking;

    @FXML
    protected Label isPayment;

    @FXML
    protected String numberSearchComboBox = "По номеру";

    @FXML
    protected String nameSearchComboBox = "По імені";

    @FXML
    protected String carBrandSearchComboBox = "По бренду авто";

    @FXML
    protected String carNumberSearchComboBox = "По номеру авто";

    @FXML
    protected String isPaymentSearchComboBox = "Оплачено";

    @FXML
    protected String isOnParkingSearchComboBox = "Знаходяться на парковці";

    @FXML
    protected String isRegularClientSearchComboBox = "Постійні клієнти";

    @FXML
    protected ComboBox<String> searchComboBox = new ComboBox<>();

    static Database database = new Database();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            database.getClients();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clientsListDatabaseLoad();

        Tooltip regularClient = new Tooltip("Постійний клієнт");
        Tooltip onParking = new Tooltip("Чи знаходиться траспрот на парковці");
        Tooltip payment = new Tooltip("Оплата");

        isRegularClient.setTooltip(regularClient);
        isOnParking.setTooltip(onParking);
        isPayment.setTooltip(payment);

        searchComboBox.getItems().addAll(numberSearchComboBox, nameSearchComboBox, carBrandSearchComboBox, carNumberSearchComboBox, isOnParkingSearchComboBox, isRegularClientSearchComboBox, isPaymentSearchComboBox);
        searchComboBox.setValue("Пошук");

        if (clients.getSize() == 0){
            editButton.setDisable(true);
        }
    }

    public void clientsListDatabaseLoad() {
        for (int row = 1; row < listDatabase.getRowCount(); row++) {
                try {
                if (row == clients.getById(row).getId()) {
                    name =  new Label(clients.getById(row).getNameOfClient());
                    regularCostumer = new Label(clients.getById(row).isRegularCostumerString());
                    carBrand = new Label(clients.getById(row).getCarBrand());
                    carNumber = new Label(clients.getById(row).getCarNumber());
                    onParking = new Label(clients.getById(row).isOnParkingString());
                    payment = new Label(clients.getById(row).isPaymentString());
                    listDatabase.add(name, 1, row);
                    listDatabase.add(regularCostumer, 2, row);
                    listDatabase.add(carBrand, 3, row);
                    listDatabase.add(carNumber, 4, row);
                    listDatabase.add(onParking, 5, row);
                    listDatabase.add(payment, 6, row);
                    GridPane.setHalignment(name, HPos.CENTER);
                    GridPane.setHalignment(regularCostumer, HPos.CENTER);
                    GridPane.setHalignment(carBrand, HPos.CENTER);
                    GridPane.setHalignment(carNumber, HPos.CENTER);
                    GridPane.setHalignment(onParking, HPos.CENTER);
                    GridPane.setHalignment(payment, HPos.CENTER);
                }
              } catch (NullPointerException e){
                    for (int i = 1; i < 7; i++) {
                        Label emptyLabel = new Label();
                        listDatabase.add(emptyLabel, i, row);
                        GridPane.setHalignment(emptyLabel, HPos.CENTER);
                    }
              }
        }
    }

    public void refreshListDatabase(){
        for (Node node : listDatabase.getChildren()) {
            if (node instanceof Label) {
               try {
                   int columnIndex = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
                   int rowIndex = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
                   if (clients.getById(rowIndex) != null) {
                       switch (columnIndex) {
                           case 0 -> ((Label) node).setText(String.valueOf(clients.getById(rowIndex).getId()));
                           case 1 -> ((Label) node).setText(clients.getById(rowIndex).getNameOfClient());
                           case 2 -> ((Label) node).setText(clients.getById(rowIndex).isRegularCostumerString());
                           case 3 -> ((Label) node).setText(clients.getById(rowIndex).getCarBrand());
                           case 4 -> ((Label) node).setText(clients.getById(rowIndex).getCarNumber());
                           case 5 -> ((Label) node).setText(clients.getById(rowIndex).isOnParkingString());
                           case 6 -> ((Label) node).setText(clients.getById(rowIndex).isPaymentString());
                       }
                   } else {
                       if (rowIndex > 0) {
                           switch (columnIndex) {
                               case 0 -> ((Label) node).setText(String.valueOf(rowIndex));
                               case 1, 2, 3, 4, 5, 6 -> ((Label) node).setText("");
                           }
                       }
                   }
               } catch (NullPointerException ignored){}
            }

        }
    }

    public void editingButton() throws IOException {
        editButton.setDisable(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("parking-editing.fxml"));

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Відредагувати клієнта");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("parking.png"))));
        stage.setX(editButton.getLayoutX());
        stage.setY(editButton.getLayoutY()-200);
        stage.setResizable(false);
        stage.show();

        EditionClientController childrenEditionClientController = loader.getController();
        childrenEditionClientController.setParent(this);
        stage.setOnCloseRequest(windowEvent -> editButton.setDisable(false));
    }

    public void addNewClient() throws IOException {
        addClientButton.setDisable(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("parking-add-client-view.fxml"));

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Додати нового клієнта");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("parking.png"))));
        stage.setX(addClientButton.getLayoutX());
        stage.setY(addClientButton.getLayoutY());
        stage.setResizable(false);
        stage.show();

        AddClientController childrenAddClientController = loader.getController();
        childrenAddClientController.setParent(this);
        stage.setOnCloseRequest(windowEvent -> addClientButton.setDisable(false));
    }

    public void searchComboBoxOnAction() {
        if (searchComboBox.getValue().equals(isRegularClientSearchComboBox) ||
            searchComboBox.getValue().equals(isOnParkingSearchComboBox) ||
            searchComboBox.getValue().equals(isPaymentSearchComboBox)){
            searchTextField.setText("");
            searchTextField.setDisable(true);
            searchTextFieldOnAction();
        } else {
            searchTextField.setDisable(false);
            searchTextFieldOnAction();
        }

    }

    public void searchTextFieldOnAction() {
        if (!searchTextField.getText().isBlank() ||
             searchComboBox.getValue().equals(isRegularClientSearchComboBox) ||
             searchComboBox.getValue().equals(isOnParkingSearchComboBox) ||
             searchComboBox.getValue().equals(isPaymentSearchComboBox)) {
            int countCharsTextField = searchTextField.getText().toCharArray().length;
            Clients clientsSearchBase = new Clients();
            char[] charArrayClient = new char[countCharsTextField];
            switch (String.valueOf(searchComboBox.getValue())) {
                case "По номеру":
                    for (int i = 0; i < clients.getSize(); i++) {
                        try {
                            char[] charArrayClientName = String.valueOf(clients.get(i).getId()).toCharArray();
                            System.arraycopy(charArrayClientName, 0, charArrayClient, 0, countCharsTextField);
                            if (String.valueOf(charArrayClient).toLowerCase(Locale.ROOT).equals(searchTextField.getText().toLowerCase(Locale.ROOT))) {
                                clientsSearchBase.addClient(clients.get(i));
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){}
                    }
                    break;
                case "По імені":
                    for (int i = 0; i < clients.getSize(); i++) {
                        try {
                            char[] charArrayClientName = clients.get(i).getNameOfClient().toCharArray();
                            System.arraycopy(charArrayClientName, 0, charArrayClient, 0, countCharsTextField);
                            if (String.valueOf(charArrayClient).toLowerCase(Locale.ROOT).equals(searchTextField.getText().toLowerCase(Locale.ROOT))) {
                                clientsSearchBase.addClient(clients.get(i));
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){}
                    }
                    break;
                case "По бренду авто":
                    for (int i = 0; i < clients.getSize(); i++) {
                        try {
                            char[] charArrayClientName = clients.get(i).getCarBrand().toCharArray();
                            System.arraycopy(charArrayClientName, 0, charArrayClient, 0, countCharsTextField);
                            if (String.valueOf(charArrayClient).toLowerCase(Locale.ROOT).equals(searchTextField.getText().toLowerCase(Locale.ROOT))) {
                                clientsSearchBase.addClient(clients.get(i));
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){}
                    }
                    break;
                case "По номеру авто":
                    for (int i = 0; i < clients.getSize(); i++) {
                        try {
                            char[] charArrayClientName = clients.get(i).getCarNumber().toCharArray();
                            System.arraycopy(charArrayClientName, 0, charArrayClient, 0, countCharsTextField);
                            if (String.valueOf(charArrayClient).toLowerCase(Locale.ROOT).equals(searchTextField.getText().toLowerCase(Locale.ROOT))) {
                                clientsSearchBase.addClient(clients.get(i));
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){}
                    }
                    break;
                case "Постійні клієнти":
                    for (int i = 0; i < clients.getSize(); i++) {
                        try {
                            if (clients.get(i).isRegularCostumer()) {
                                clientsSearchBase.addClient(clients.get(i));
                            }
                        } catch (NullPointerException ignored){}
                    }
                    break;
                case "Знаходяться на парковці":
                    for (int i = 0; i < clients.getSize(); i++) {
                        try {
                            if (clients.get(i).isOnParking()) {
                                clientsSearchBase.addClient(clients.get(i));
                            }
                        } catch (NullPointerException ignored){}
                    }
                    break;
                case "Оплачено":
                    for (int i = 0; i < clients.getSize(); i++) {
                        try {
                            if (clients.get(i).isPayment()) {
                                clientsSearchBase.addClient(clients.get(i));
                            }
                        } catch (NullPointerException ignored){}
                    }
                    break;
            }
            for (Node node : listDatabase.getChildren()) {
                if (node instanceof Label) {
                    try {
                        int columnIndex = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
                        int rowIndex = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;

                        if (rowIndex > 0 && rowIndex <= clientsSearchBase.getSize()) {
                            switch (columnIndex) {
                                case 0 -> ((Label) node).setText(String.valueOf(clientsSearchBase.get(rowIndex - 1).getId()));
                                case 1 -> ((Label) node).setText(clientsSearchBase.get(rowIndex - 1).getNameOfClient());
                                case 2 -> ((Label) node).setText(clientsSearchBase.get(rowIndex - 1).isRegularCostumerString());
                                case 3 -> ((Label) node).setText(clientsSearchBase.get(rowIndex - 1).getCarBrand());
                                case 4 -> ((Label) node).setText(clientsSearchBase.get(rowIndex - 1).getCarNumber());
                                case 5 -> ((Label) node).setText(clientsSearchBase.get(rowIndex - 1).isOnParkingString());
                                case 6 -> ((Label) node).setText(clientsSearchBase.get(rowIndex - 1).isPaymentString());
                            }
                        } else {
                            if (rowIndex > 0) {
                                switch (columnIndex) {
                                    case 0, 1, 2, 3, 4, 5, 6 -> ((Label) node).setText("");
                                }
                            }
                        }
                    } catch (NullPointerException ignored) {}
                }
            }
        } else {
            refreshListDatabase();
        }
    }

}