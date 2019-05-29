package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class UserController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<Administrator> administrators = DataProviderSingleton.getInstance().getAdministrators();

    @GetMapping
    @ResponseBody
    public ArrayList<User> getUsers(@RequestBody Department department) {
        String departmentId = department.getId();
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(departmentId))
                for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                    users.add(departments.get(i).getUsers().get(j));
                }
        }
        return users;
    }

    @PostMapping
    @ResponseBody
    public void addUser(@RequestBody UserDepartmentWrapper udw) {
        String departmentId = udw.user.getId();
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(departmentId)) {
                departments.get(i).addUser(udw.user);
            }
        }
    }

    @PutMapping
    @ResponseBody
    public void editUser(@RequestBody User user) {
        String id = user.getId();

        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                if (departments.get(i).getUsers().get(j).getId().equals(id)) {
                    if (!user.getUsername().equals(null)) {
                        departments.get(i).getUsers().get(j).setUsername(user.getUsername());
                    }
                    if (!user.getEmail().equals(null)) {
                        departments.get(i).getUsers().get(j).setEmail(user.getEmail());
                    }
                    if (!user.getFirstname().equals(null)) {
                        departments.get(i).getUsers().get(j).setFirstname(user.getFirstname());
                    }
                    if (!user.getLastname().equals(null)) {
                        departments.get(i).getUsers().get(j).setLastname(user.getLastname());
                    }
                    if (!user.getPassword().equals(null)) {
                        departments.get(i).getUsers().get(j).setPassword(user.getPassword());
                    }
                    if (!user.getDateOfBirth().equals(null)) {
                        departments.get(i).getUsers().get(j).setDateOfBirth(user.getDateOfBirth());
                    }
                }
            }
        }
    }

    @DeleteMapping
    @ResponseBody
    public void deleteUser(@RequestBody User user) {
        String id = user.getId();
        boolean isAdmin = false;

        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                if (departments.get(i).getUsers().get(j).getId().equals(id)) {
                    isAdmin = departments.get(i).getUsers().get(j).hasAdminRights();
                    departments.get(i).getUsers().remove(j);
                }
            }
        }
        if (isAdmin) {
            for (int i = 0; i < administrators.size(); i++) {
                if (administrators.get(i).getId().equals(id)) {
                    administrators.remove(i);
                }
            }
        }
    }

    class UserDepartmentWrapper{
        public User user;
        public Department department;
    }
}