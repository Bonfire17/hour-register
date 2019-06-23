package nl.bonfire17.hourregister.data;

import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import nl.bonfire17.hourregister.wrappers.TransferWrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class DataProviderSingleton {

    private static DataProviderSingleton instance;

    private ArrayList<Department> departments;

    private DataProviderSingleton() {
        departments = new ArrayList<Department>();

        departments.add(new Department("Restaurant Bediening", "Alle restaurant medewerkers"));
        departments.add(new Department("ICT", "Alle ict medewerkers"));

        departments.get(0).getUsers().add(new User("User1", "userone@gmail.com", "Bert", "Jansen", "User1", LocalDate.of(1998, 6, 27)));
        departments.get(0).getUsers().add(new User("User2", "usertwo@gmail.com", "Jay", "Wit", "User2", LocalDate.of(2000, 5, 13)));
        departments.get(0).getUsers().add(new User("User3", "userthree@hotmail.com", "Jan", "Bertus", "User3", LocalDate.of(1999, 7, 16)));
        departments.get(0).getUsers().add(new Administrator("Admin1", "adminone@gmail.com", "Hans", "Jansen", "Admin1", LocalDate.of(1997, 2, 1)));
        departments.get(0).getUsers().add(new Administrator("Admin2", "admintwo@gmail.com", "Pieter", "Jan Coen", "Admin2", LocalDate.of(1995, 5, 8)));

        departments.get(0).getUser(2).clockIn();

        LocalDateTime date = LocalDateTime.of(2019, 05, 28, 12, 0, 0);
        departments.get(0).getUser(0).getWorkdays().add(new Workday(date, date.plusHours(3), LocalTime.of(0, 15), true));

        date = LocalDateTime.of(2019, 06, 28, 8, 0, 0);
        departments.get(0).getUser(1).getWorkdays().add(new Workday(date, date.plusHours(8), LocalTime.of(0, 45), true));

        date = LocalDateTime.of(2019, 06, 29, 8, 0, 0);
        departments.get(0).getUser(1).getWorkdays().add(new Workday(date, date.plusHours(8), LocalTime.of(1, 0), false));

        date = LocalDateTime.of(2019, 06, 7, 8, 0, 0);
        departments.get(0).getUser(1).getWorkdays().add(new Workday(date, date.plusHours(5), LocalTime.of(0, 45), false));
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

    public Department getDepartmentById(String id){
        for(Department department: departments){
            if(department.id.equals(id)){
                return department;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        for(Department department: departments){
            users.addAll(department.getUsers());
        }
        return users;
    }

    public User getUserById(String id){
        for(User user: getUsers()){
            if(user.id.equals(id)){
                return user;
            }
        }
        return null;
    }

    public void replaceUser(User newUser){
        for(Department department: departments) {
            for (int i = 0; i < department.getUsers().size(); i++) {
                User user = department.getUsers().get(i);
                if (user.id.equals(newUser.id)) {
                    department.getUsers().set(i, newUser);
                    break;
                }
            }
        }
    }

    public ArrayList<Workday> getWorkdays(){
        ArrayList<Workday> workdays = new ArrayList<>();
        for(User user: getUsers()){
            workdays.addAll(user.getWorkdays());
        }
        return workdays;
    }

    public Workday getWorkdayById(String id){
        for(int i = 0; i < departments.size(); i++){
            Department department = departments.get(i);
            for(int j = 0; j < department.getUsers().size(); j++){
                User user = department.getUsers().get(j);
                for(Workday workday: user.getWorkdays()){
                    if(workday.id.equals(id)){
                        return workday;
                    }
                }
            }
        }
        return null;
    }
}
