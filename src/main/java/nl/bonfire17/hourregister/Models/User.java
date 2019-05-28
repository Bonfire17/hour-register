package nl.bonfire17.hourregister.Models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class User {

    public String id;

    private String username, email, firstname, lastname, password;

    private Date dateOfBirth;
    private ArrayList<Workday> workdays;
    private Workday currentWorkday;

    public void User(String username, String email, String firstname, String lastname, String password, Date dateOfBirth){
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;

        id = UUID.randomUUID().toString();
        workdays = new ArrayList<Workday>();
    }

    //Clock user in, return if successful
    public boolean clockIn(){
        if(currentWorkday == null) {
            workdays.add(new Workday(LocalDateTime.now()));
            currentWorkday = workdays.get(workdays.size() - 1);
            return true;
        }
        return false;
    }

    //Clock user out, return if successful
    public boolean clockOut(LocalTime breakTime){
        if(currentWorkday != null) {
            currentWorkday.clockOut(LocalDateTime.now(), breakTime);
            currentWorkday = null;
            return true;
        }
        return false;
    }

    //Getters & Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ArrayList<Workday> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(ArrayList<Workday> workdays) {
        this.workdays = workdays;
    }

    //End Getters & Setters
}
