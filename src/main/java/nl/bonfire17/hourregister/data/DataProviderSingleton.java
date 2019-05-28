package nl.bonfire17.hourregister.data;

import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class DataProviderSingleton {

    private static DataProviderSingleton instance;

    private ArrayList<Department> departments;
    private ArrayList<Administrator> administrators;

    private DataProviderSingleton(){
        departments = new ArrayList<Department>();
        administrators = new ArrayList<Administrator>();

        Department dep;
        dep = new Department("Restaurant Bediening", "Alle restaurant medewerkers");

        //Add normal users
        dep.addUser(new User("Bonfire17", "bertus@gmail.com", "Bert", "Bonkers", "test123", new Date(898646400), dep.getId()));
        dep.addUser(new User("JJVoort", "jj@hotmail.com", "Jay", "Jay", "wachtwoord", new Date(955152000), dep.getId()));

        //Add administrators
        Administrator admin = new Administrator("Admin420", "restaurantadmin@gmail.com", "Hans", "Jansen", "Admin85", new Date(955162000), dep.getId());
        dep.addUser(admin);
        administrators.add(admin);

        LocalDateTime date = LocalDateTime.of(2019, 05, 28, 12, 0, 0);
        dep.getUser(0).getWorkdays().add(new Workday(date, date.plusHours(3), LocalTime.of(0, 15), dep.getUser(0).getId()));
        departments.add(dep);
    }

    private synchronized static void createInstance(){
        if(instance == null)instance = new DataProviderSingleton();
    }

    public static DataProviderSingleton getInstance(){
        if(instance == null)createInstance();
        return instance;
    }

    public ArrayList<Department> getDepartments(){
        return this.departments;
    }

    public ArrayList<Administrator> getAdministrators() {
        return this.administrators;
    }

    public void deleteDepartment(int index){
        this.departments.remove(index);
    }

    public void deleteDepartmentById(String id){
        for(int i = 0; i < this.departments.size(); i++){
            if(this.departments.get(i).id == id){
                this.departments.remove(i);
            }
        }
    }
}
