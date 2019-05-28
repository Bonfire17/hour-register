package nl.bonfire17.hourregister.models;

import java.util.Date;

public class Administrator extends User {

    public Administrator(String username, String email, String firstname, String lastname, String password, Date dateOfBirth){
        super(username, email, firstname, lastname, password, dateOfBirth);
    }

    @Override
    public boolean hasAdminRights(){
        return true;
    }

}
