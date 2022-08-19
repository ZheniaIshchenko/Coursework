package com.example.coursework;

import java.util.Arrays;
import java.util.Objects;

public class Client {
    private int id;
    private boolean isRegularCostumer;
    private boolean isOnParking;
    private boolean isPayment;
    private String nameOfClient;
    private String carBrand;
    private String carNumber;

    public Client(int id, boolean isRegularCostumer, boolean isOnParking, boolean isPayment, String nameOfClient, String carBrand, String carNumber) {
        this.setId(id);
        this.setIsRegularCostumer(isRegularCostumer);
        this.setIsOnParking(isOnParking);
        this.setIsPayment(isPayment);
        this.setNameOfClient(nameOfClient);
        this.setCarBrand(carBrand);
        this.setCarNumber(carNumber);
    }

    public void setId(int id) {
        if (id < 31 && id > 0) {
            this.id = id;
        }
    }

    public void setIsRegularCostumer(boolean regularCostumer) {
        this.isRegularCostumer = regularCostumer;
    }

    public void setIsOnParking(boolean inTheParking) {
        this.isOnParking = inTheParking;
    }

    public void setIsPayment(boolean payment) {
        this.isPayment = payment;
    }

    public String setNameOfClient(String nameOfClient) {
        this.nameOfClient = wordSlicer(nameOfClient);
        return nameOfClient;
    }

    private String wordSlicer(String nameOfClient) {
        String[] wordsArray = nameOfClient.split(" ");
        StringBuilder nameClient = new StringBuilder(" ");
        for (String word : wordsArray) {
            try {
                if (!word.equals("")){
                    nameClient.append(firstCharUpperCase(word).strip()).append(" ");
                }

            } catch (ArrayIndexOutOfBoundsException ignored){}
        }
        return String.valueOf(nameClient).strip();
    }

    private String firstCharUpperCase(String word) {
        word = word.strip();
        char[] charArray = word.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return String.valueOf(charArray);
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = wordSlicer(carBrand).strip();
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = wordSlicer(carNumber).strip();
    }

    public int getId() {
        return id;
    }

    public String getNameOfClient() {
        return nameOfClient;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String isRegularCostumerString() {
        return  isRegularCostumer? "Так" : "Ні";
    }

    public String isOnParkingString() {
        return isOnParking ? "Так" : "Ні";
    }

    public String isPaymentString() {
        return isPayment? "Так" : "Ні";
    }

    @Override
    public String toString() {
        return  "№" + id +
                " | Ім'я Клиєнта: " + nameOfClient +
                " | Постійний клиєнт: " + isRegularCostumerString() +
                " | Бренд авто: " + carBrand  +
                " | Номер авто: " + carNumber +
                " | На парковці: " + isOnParkingString() +
                " | Оплачено: " + isPaymentString();
    }

    public boolean isRegularCostumer() {
        return this.isRegularCostumer;
    }

    public boolean isOnParking() {
        return isOnParking;
    }

    public boolean isPayment() {
        return isPayment;
    }


}
