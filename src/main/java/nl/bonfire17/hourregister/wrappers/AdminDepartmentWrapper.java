package nl.bonfire17.hourregister.wrappers;

import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;

public class AdminDepartmentWrapper{
    public Administrator administrator;
    public String department;

    public AdminDepartmentWrapper(Administrator administrator, String department) {
        this.administrator = administrator;
        this.department = department;
    }

    public AdminDepartmentWrapper() {
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
