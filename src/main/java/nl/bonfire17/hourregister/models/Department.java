package nl.bonfire17.hourregister.models;

import java.util.ArrayList;
import java.util.UUID;

public class Department {

    public String id;

    private String name, info;

    private ArrayList<User> users;

    public Department(String name, String info) {
        this.name = name;
        this.info = info;

        id = UUID.randomUUID().toString();
        users = new ArrayList<User>();
    }

    //Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    //End Getters & Setters
}
