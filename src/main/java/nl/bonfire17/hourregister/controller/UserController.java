package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.wrappers.UserDepartmentWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class UserController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<Administrator> administrators = DataProviderSingleton.getInstance().getAdministrators();
    private ArrayList<User> users = DataProviderSingleton.getInstance().getUserList();

    @GetMapping
    @ResponseBody
    public ArrayList<User> getUsers(@RequestBody Department department) {
        String departmentId = department.getId();
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(departmentId)) {
                return departments.get(i).getUsers();
            }
        }
        return users;
    }

    @PostMapping
    @ResponseBody
    public void addUser(@RequestBody UserDepartmentWrapper udw) {
        String departmentId = udw.department;
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(departmentId)) {
                departments.get(i).getUsers().add(udw.user);
            }
        }
        users.add(udw.user);
    }

    @PutMapping
    @ResponseBody
    public void editUser(@RequestBody User user) {
        String id = user.getId();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                if (!user.getUsername().equals(null)) {
                    users.get(i).setUsername(user.getUsername());
                }
                if (!user.getEmail().equals(null)) {
                    users.get(i).setEmail(user.getEmail());
                }
                if (!user.getFirstname().equals(null)) {
                    users.get(i).setFirstname(user.getFirstname());
                }
                if (!user.getLastname().equals(null)) {
                    users.get(i).setLastname(user.getLastname());
                }
                if (!user.getPassword().equals(null)) {
                    users.get(i).setPassword(user.getPassword());
                }
                if (!user.getDateOfBirth().equals(null)) {
                    users.get(i).setDateOfBirth(user.getDateOfBirth());
                }
            }
        }
    }

    @DeleteMapping
    @ResponseBody
    public void deleteUser(@RequestBody User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.id)) {
                users.remove(i);
            }
        }

        for (int i = 0; i < administrators.size(); i++) {
            if (administrators.get(i).id.equals(user.id)) {
                administrators.remove(i);
            }
        }
    }
}