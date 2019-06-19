package nl.bonfire17.hourregister.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Administrator extends User {

    public Administrator(String username, String email, String firstname, String lastname, String password, LocalDate dateOfBirth){
        super(username, email, firstname, lastname, password, dateOfBirth);
    }

    public Administrator(String id, String username, String email, String firstname, String lastname, String password, LocalDate dateOfBirth, ArrayList<Workday> workdays, Workday currentWorkday){
        super(id, username, email, firstname, lastname, password, dateOfBirth, workdays, currentWorkday);
    }

    public User getUser(){
        return new User(id, username, email, firstname, lastname, password, dateOfBirth, workdays, currentWorkday);
    }

    @Override
    public boolean isAdmin(){
        return true;
    }

}
