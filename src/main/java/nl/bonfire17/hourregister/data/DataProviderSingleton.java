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

    public static String USER_ID;
    public static String DEPARTMENT_ID;

    private DataProviderSingleton() {
        departments = new ArrayList<Department>();
        users = new ArrayList<User>();
        workdays = new ArrayList<Workday>();
        administrators = new ArrayList<Administrator>();

        departments.add(new Department("Restaurant Bediening", "Alle restaurant medewerkers"));

        DEPARTMENT_ID = departments.get(0).getId();

        users.add(new User("Bonfire17", "bertus@gmail.com", "Bert", "Bonkers", "test123", new Date(898646400)));

        USER_ID = users.get(0).getId();
        users.add(new User("JJVoort", "jj@hotmail.com", "Jay", "Jay", "wachtwoord", new Date(955152000)));
        departments.get(0).addUser(users.get(0));
        departments.get(0).addUser(users.get(1));

        administrators.add(new Administrator("Admin420", "restaurantadmin@gmail.com", "Hans", "Jansen", "Admin85", new Date(955162000)));
        departments.get(0).addUser(administrators.get(0));

        LocalDateTime date = LocalDateTime.of(2019, 05, 28, 12, 0, 0);
        workdays.add(new Workday(date, date.plusHours(3), LocalTime.of(0, 15), true));
        departments.get(0).getUser(0).addWorkday(workdays.get(0));

        date = LocalDateTime.of(2019, 06, 28, 8, 0, 0);
        workdays.add(new Workday(date, date.plusHours(8), LocalTime.of(0, 45), true));
        departments.get(0).getUser(1).addWorkday(workdays.get(1));

        date = LocalDateTime.of(2019, 06, 29, 8, 0, 0);
        workdays.add(new Workday(date, date.plusHours(8), LocalTime.of(1, 0), false));
        departments.get(0).getUser(1).addWorkday(workdays.get(2));
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

    public ArrayList<User> getUserList () {
        return users;
    }

    public ArrayList<Workday> getWorkdays () {
        return workdays;
    }

    public void addWorkday(Workday workday) {
        workdays.add(workday);
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
