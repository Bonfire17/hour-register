package nl.bonfire17.hourregister.wrappers;

import nl.bonfire17.hourregister.models.User;

public class UserDepartmentWrapper{
    public User user;
    public String department;

    public UserDepartmentWrapper(User user, String department){
        this.user = user;
        this.department = department;
    }

    public UserDepartmentWrapper() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
