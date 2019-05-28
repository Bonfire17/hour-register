package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/administrator")
public class AdminController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<Administrator> administrators = DataProviderSingleton.getInstance().getAdministrators();
    
    @GetMapping
    @ResponseBody
    public ArrayList<Administrator> getAdministrators() {
        return administrators;
    }
    
    @GetMapping(path = "/users")
    @ResponseBody
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();

        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                users.add(departments.get(i).getUsers().get(j));
            }
        }
        return users;
    }

    @GetMapping(path = "/working")
    @ResponseBody
    public ArrayList<User> getWorkingUsers() {
        ArrayList<User> workingUsers = new ArrayList<User>();

        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                if (departments.get(i).getUsers().get(j).getWorkdays().size() > 0) {
                    if (departments.get(i).getUsers().get(j).getWorkdays().get(departments.get(i).getUsers().get(j).getWorkdays().size() - 1).isWorking()) {
                        workingUsers.add(departments.get(i).getUsers().get(j));
                    }
                }
            }
        }
        return workingUsers;
    }

    @PostMapping
    @ResponseBody
    public void addAdmin(@RequestBody Administrator administrator) {
        administrators.add(administrator);
        String departmentId = administrator.getDepartmentId();
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(departmentId)) {
                departments.get(i).addUser(administrator);
            }
        }
    }
}
