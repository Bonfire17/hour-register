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
    private ArrayList<User> users;
    private ArrayList<Workday> workdays;
    private ArrayList<Administrator> administrators;

    private DataProviderSingleton() {
        departments = new ArrayList<Department>();
        users = new ArrayList<User>();
        workdays = new ArrayList<Workday>();
        administrators = new ArrayList<Administrator>();

        departments.add(new Department("Restaurant Bediening", "Alle restaurant medewerkers"));

        users.add(new User("Bonfire17", "bertus@gmail.com", "Bert", "Bonkers", "test123", new Date(898646400)));
        users.add(new User("JJVoort", "jj@hotmail.com", "Jay", "Jay", "wachtwoord", new Date(955152000)));
        departments.get(0).addUser(users.get(0));
        departments.get(0).addUser(users.get(1));

        administrators.add(new Administrator("Admin420", "restaurantadmin@gmail.com", "Hans", "Jansen", "Admin85", new Date(955162000)));
        departments.get(0).addUser(administrators.get(0));

        LocalDateTime date = LocalDateTime.of(2019, 05, 28, 12, 0, 0);
        workdays.add(new Workday(date, date.plusHours(3), LocalTime.of(0, 15)));
        departments.get(0).getUser(0).addWorkday(workdays.get(0));
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
