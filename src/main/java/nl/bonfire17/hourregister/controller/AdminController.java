package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.wrappers.AdminDepartmentWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/administrator")
public class AdminController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<Administrator> administrators = DataProviderSingleton.getInstance().getAdministrators();
    private ArrayList<User> users = DataProviderSingleton.getInstance().getUserList();
    
    @GetMapping
    @ResponseBody
    public ArrayList<Administrator> getAdministrators() {
        return administrators;
    }
    
    @GetMapping(path = "/users")
    @ResponseBody
    public ArrayList<User> getAllUsers() {
        return users;
    }

    @GetMapping(path = "/working")
    @ResponseBody
    public ArrayList<User> getWorkingUsers() {
        ArrayList<User> workingUsers = new ArrayList<User>();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getWorkdays().size() > 0) {
                if (users.get(i).getWorkdays().get(users.get(i).getWorkdays().size() - 1).isWorking()) {
                    workingUsers.add(users.get(i));
                }
            }
        }
        return workingUsers;
    }

    @PostMapping
    @ResponseBody
    public void addAdmin(@RequestBody AdminDepartmentWrapper adw) {
        administrators.add(adw.administrator);
        String departmentId = adw.department;
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(departmentId)) {
                departments.get(i).addUser(adw.administrator);
                users.add(adw.administrator);
            }
        }
    }
}
