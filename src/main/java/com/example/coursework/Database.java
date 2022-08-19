package com.example.coursework;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.coursework.ParkDatabaseController.database;
import static java.sql.DriverManager.getConnection;
public class Database {
    private static Connection con;

    static {
        try {
            String URL = "jdbc:mysql://127.0.0.1/parking_database?serverTimezone=UTC";
            String USER = "root";
            String PASSWORD = "root";
            con = getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Clients clients = new Clients();

    public void addClientDB(int id, String name, Boolean is_regular_client, String car_brand, String car_number, Boolean is_on_parking, Boolean is_payment) throws SQLException {
        Statement statement;
        statement = con.createStatement();
        statement.executeUpdate("INSERT into client( id, name_client, car_brand, car_number, is_regular_customer, is_parking, is_payment) " +
                "VALUES(" + id +", " + "'"+ name +"'"+ ", " + "'"+car_brand +"'"+ ", " +"'"+ car_number +"'"+ ", " + is_regular_client + ", " + is_on_parking + ", " + is_payment +")");
    }

    public void addClientDB(Client client) throws SQLException {
        Statement statement;
        statement = con.createStatement();
        statement.executeUpdate("INSERT into client( id, name_client, car_brand, car_number, is_regular_customer, is_parking, is_payment) " +
                "VALUES(" + client.getId() +", " + "'"+ client.getNameOfClient() +"'"+ ", " + "'" + client.getCarBrand() +"'"+ ", " +"'"+ client.getCarNumber() +"'"+ ", " + client.isRegularCostumer() + ", " + client.isOnParking() + ", " + client.isPayment() +")");
    }

    public void changeClientDB(int oldId, Client client) throws SQLException {
        database.deleteClientDB(oldId);
        database.addClientDB(client.getId(), client.getNameOfClient(), client.isRegularCostumer(), client.getCarBrand(), client.getCarNumber(), client.isOnParking(), client.isPayment());
    }

    public void deleteClientDB(int id) throws SQLException {
        clients.remove(clients.indexOf(clients.getById(id)));
        Statement statement;
        statement = con.createStatement();
        statement.executeUpdate("DELETE FROM client WHERE id=" + id);
    }

    public void updateClients() throws SQLException {
        clients.clear();
        database.getClients();
    }

    public String getPasswordDB() throws SQLException{
        ResultSet resultSet;
        Statement statement;
        statement = con.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM password WHERE id=1");
        resultSet.next();
        return resultSet.getString("password");
    }

    public void getClients() throws SQLException {
        Statement statement;
        ResultSet resultSet, rowsCheck;
        boolean is_regular_client, is_on_parking, is_payment ;
        String name, car_brand, car_number;
        int id;

        statement = con.createStatement();

        rowsCheck = statement.executeQuery("SELECT COUNT(*) as count FROM `client`");
        rowsCheck.next();
        int rowsCount = rowsCheck.getInt("count");

        resultSet = statement.executeQuery("SELECT * FROM `client`");
        resultSet.next();

        for (int i = 1; i <= rowsCount; i++) {

            try{
                is_regular_client = resultSet.getBoolean("is_regular_customer");
                is_on_parking = resultSet.getBoolean("is_parking");
                is_payment = resultSet.getBoolean("is_payment");
                name = resultSet.getString("name_client");
                car_brand = resultSet.getString("car_brand");
                car_number = resultSet.getString("car_number");
                id = resultSet.getInt("id");

                Client client = new Client(id, is_regular_client, is_on_parking, is_payment, name, car_brand, car_number);
                clients.addClient(client);
                resultSet.next();
            }catch (NullPointerException ignored){

            }
        }
    }

}
