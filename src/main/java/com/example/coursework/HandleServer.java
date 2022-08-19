package com.example.coursework;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static com.example.coursework.Database.clients;

public class HandleServer implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
            returnResponse(httpExchange);
    }

    private void returnResponse(HttpExchange httpExchange) throws IOException{
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder response = new StringBuilder();

        response.append("[");
        for (int i=0; i< clients.getSize(); i++){
            response.append("\n\t{\n\t");
            response.append("\"id").append("\":\"").append(clients.get(i).getId()).append("\",\n\t");
            response.append("\"name").append("\":\"").append(Arrays.toString(clients.get(i).getNameOfClient().getBytes())).append("\",\n\t");
            response.append("\"isRegularCustomer").append("\":\"").append(clients.get(i).isRegularCostumer()).append("\",\n\t");
            response.append("\"carBrand").append("\":\"").append(Arrays.toString(clients.get(i).getCarBrand().getBytes())).append("\",\n\t");
            response.append("\"carNumber").append("\":\"").append(Arrays.toString(clients.get(i).getCarNumber().getBytes())).append("\",\n\t");
            response.append("\"isOnParking").append("\":\"").append(clients.get(i).isOnParking()).append("\",\n\t");
            response.append("\"isPayment").append("\":\"").append(clients.get(i).isPayment()).append("\",\n\t");

            response.deleteCharAt(response.length()-3);
            response.append("},\t");
        }
        response.deleteCharAt(response.length()-2);
        response.append("\n]");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        httpExchange.sendResponseHeaders(0, response.length());
        outputStream.write(response.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}