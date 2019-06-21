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

    public Department(){
        id = UUID.randomUUID().toString();
        users = new ArrayList<User>();
    }

    public Department(String id) {
        this.id = id;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public User getUser(int index){
        return this.users.get(index);
    }

    public User getUserById(String id){
        for(int i = 0; i < this.users.size(); i++){
            if(id == this.users.get(i).id){
                return this.users.get(i);
            }
        }
        return null;
    }

    public void removeUserById (String id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.remove(i);
            }
        }
    }

    //Static Methods

    //Transfar user from one department to the other
    public static void transferUser(Department oldDepartment, Department newDepartment, User tranferUser) {
        //remove from old department
        for(int i = 0; i < oldDepartment.getUsers().size(); i++){
            User user = oldDepartment.getUsers().get(i);
            if(user.id.equals(tranferUser.id)){
                oldDepartment.getUsers().remove(i);
                break;
            }
        }
        //Add to new department
        newDepartment.getUsers().add(tranferUser);
    }

    //End Static Methods
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

    public String getId() {
        return id;
    }
    //End Getters & Setters
    //Processing Data

    public int getUserCount(){
        return this.users.size();
    }

    //End Processing Data
}
