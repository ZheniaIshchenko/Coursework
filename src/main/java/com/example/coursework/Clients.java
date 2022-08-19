package com.example.coursework;

import java.util.ArrayList;

public class Clients {
    private final ArrayList<Client> clients;

    public Clients (){
        clients = new ArrayList<>();
    }
    public void addClient(final Client client){
        clients.add(client);
    }

    public void setNulls(int countNulls){
        for (int i = 0; i<countNulls; i++){
            this.addClient(null);
        }
    }

    public int getSize(){
        return clients.size();
    }

    @Override
    public String toString() {
        System.out.println("Clients{");
        for (Client client : clients) {
            System.out.println(client);
        }
        return "}";
    }

    public Client getById(int id) {
        int clientIdOnParking;
        for (int i = 0; i < clients.size(); i++){
            if(clients.get(i).getId() == id) {
                clientIdOnParking = i;
                return clients.get(clientIdOnParking);
            }
        }
        return null;
    }

    public Client get(int index) {
        return clients.get(index);
    }

    public int indexOf(Client client){
        return clients.indexOf(client);
    }

    public void set(int index, Client client) {
        clients.set(index, client);
    }

    public boolean remove(int index){
        clients.remove(index);
        return false;
    }

    public void clear() {
        clients.clear();
    }
}

