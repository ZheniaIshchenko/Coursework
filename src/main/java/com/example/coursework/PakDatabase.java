package com.example.coursework;

import com.sun.net.httpserver.HttpServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;



public class PakDatabase extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PakDatabase.class.getResource("parking-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        stage.setTitle("База даних про клиєнтів парковки");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("parking.png"))));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws IOException{
        simplestServerExample();
        launch();
    }

    public static void simplestServerExample() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8005), 0);
        server.createContext("/back", new HandleServer());
        server.start();
    }
}