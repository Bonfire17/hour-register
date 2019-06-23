package nl.bonfire17.hourregister.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

public class User {

    public String id;

    protected String username, email, firstname, lastname, password;

    protected LocalDate dateOfBirth;
    protected ArrayList<Workday> workdays;
    protected Workday currentWorkday;

    public User(String username, String email, String firstname, String lastname, String password, LocalDate dateOfBirth){
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;

        id = UUID.randomUUID().toString();
        workdays = new ArrayList<Workday>();
    }

    public User(String id, String username, String email, String firstname, String lastname, String password, LocalDate dateOfBirth, ArrayList<Workday> workdays, Workday currentWorkday){
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.id = id;
        this.workdays = workdays;
        this.currentWorkday = currentWorkday;
        workdays = new ArrayList<Workday>();
    }

    public User(){
        id = UUID.randomUUID().toString();
        workdays = new ArrayList<Workday>();
    }

    //Return admin with user data
    public Administrator getAdministrator(){
        return new Administrator(id, username, email, firstname, lastname, password, dateOfBirth, workdays, currentWorkday);
    }

    //Clock user in, return if successful
    public boolean clockIn(){
        if(currentWorkday == null) {
            workdays.add(new Workday(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
            currentWorkday = workdays.get(workdays.size() - 1);
            return true;
        }
        return false;
    }

    //Clock user out, return if successful
    public boolean clockOut(LocalTime breakTime){
        if(currentWorkday != null) {
            currentWorkday.clockOut(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), breakTime);
            currentWorkday = null;
            return true;
        }
        return false;
    }

    //Chech if user is still working
    public boolean isWorking(){
        for(Workday workday: workdays){
            if(workday.isWorking()){
                return true;
            }
        }
        return false;
    }

    public void addWorkday(Workday workday){
        workdays.add(workday);
    }

    //Check if the user has adminRights, needs to override for administrator
    public boolean isAdmin(){
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ArrayList<Workday> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(ArrayList<Workday> workdays) {
        this.workdays = workdays;
    }

    public String getId() {
        return id;
    }

    //End Getters & Setters
    //Processing Data

    public String getDateOfBirthFormated(){
        return dateOfBirth.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String getDateOfBirthUnix(){
        return dateOfBirth.toString();
    }

    //End Processing Data
}
