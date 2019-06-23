package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.wrappers.UserDepartmentWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/*
    Both the admin and a user should be able to access this controller. Some methods however should not be accessible by the user
 */

@Controller
@RequestMapping("/user")
public class UserController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<User> users = DataProviderSingleton.getInstance().getUsers();

    //Admin
    //Add a new user
    @PostMapping(path = "/add")
    public RedirectView addUser(@RequestParam(name = "username") String username,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "firstname") String firstname,
                                @RequestParam(name = "lastname") String lastname,
                                @RequestParam(name = "date-of-birth") String dateOfBirth,
                                @RequestParam(name = "administrator", required = false) String administrator,
                                @RequestParam(name = "password") String password,
                                @RequestParam(name = "department") String departmentId, HttpSession session) {

        for (int j = 0; j < this.users.size(); j++) {
            if (session.getAttribute("userId").equals(this.users.get(j).id) && this.users.get(j).isAdmin()) {
                if (administrator != null) {
                    DataProviderSingleton.getInstance().getDepartmentById(departmentId).addUser(new Administrator(username, email, firstname, lastname, password, LocalDate.parse(dateOfBirth)));
                } else {
                    DataProviderSingleton.getInstance().getDepartmentById(departmentId).addUser(new User(username, email, firstname, lastname, password, LocalDate.parse(dateOfBirth)));
                }
                return new RedirectView("/administrator/user");
            }
        }
        return new RedirectView("");
    }

    //Admin
    //Edit a existing user
    @PostMapping(path = "/edit/{id}")
    public RedirectView editUser(@PathVariable(name = "id") String id,
                                 @RequestParam(name = "username") String username,
                                 @RequestParam(name = "email") String email,
                                 @RequestParam(name = "firstname") String firstname,
                                 @RequestParam(name = "lastname") String lastname,
                                 @RequestParam(name = "date-of-birth") String dateOfBirth,
                                 @RequestParam(name = "administrator", required = false) String administrator,
                                 @RequestParam(name = "old-password", required = false) String oldPassword,
                                 @RequestParam(name = "new-password", required = false) String newPassword,
                                 HttpSession session) {

        for (int j = 0; j < this.users.size(); j++) {
            if (session.getAttribute("userId").equals(this.users.get(j).id) && this.users.get(j).isAdmin()) {

                User user = DataProviderSingleton.getInstance().getUserById(id);
                user.setUsername(username);
                user.setEmail(email);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setDateOfBirth(LocalDate.parse(dateOfBirth));

                //Check if the administrator box is checked AND if the user is not already a administrator
                if (administrator != null && !(user instanceof Administrator)) {
                    DataProviderSingleton.getInstance().replaceUser(user.getAdministrator());
                } else if (administrator == null && user instanceof Administrator) {
                    DataProviderSingleton.getInstance().replaceUser(((Administrator) user).getUser());
                }
                System.out.println(user.isAdmin());
                if (oldPassword == user.getPassword()) {
                    user.setPassword(newPassword);
                }
                return new RedirectView("/administrator/user");
            }
        }
        return new RedirectView("");
    }

    //Admin
    //Transfer a user from one department to another
    @PostMapping(path = "/transfer/{id}")
    public RedirectView transferUser(@PathVariable(name = "id") String id, @RequestParam(name = "department") String targetDepartment, HttpSession session) {

        for (int j = 0; j < this.users.size(); j++) {
            if (session.getAttribute("userId").equals(this.users.get(j).id) && this.users.get(j).isAdmin()) {
                Department oldDepartment, newDepartment;
                User transferUser;

                for (int i = 0; i < departments.size(); i++) {
                    Department department = departments.get(i);
                    for (User user : department.getUsers()) {
                        if (user.id.equals(id)) {
                            transferUser = user;
                            oldDepartment = department;
                            newDepartment = DataProviderSingleton.getInstance().getDepartmentById(targetDepartment);
                            Department.transferUser(oldDepartment, newDepartment, transferUser);
                            return new RedirectView("/administrator/user");
                        }
                    }
                }
                return new RedirectView("/administrator/user");
            }
        }
        return new RedirectView("");
    }

    //Admin
    //Delete a user
    @PostMapping(path = "/delete/{id}")
    public RedirectView transferUser(@PathVariable(name = "id") String id, HttpSession session) {

        for (int j = 0; j < this.users.size(); j++) {
            if (session.getAttribute("userId").equals(this.users.get(j).id) && this.users.get(j).isAdmin()) {
                for (Department department : departments) {
                    for (int i = 0; i < department.getUsers().size(); i++) {
                        if (department.getUsers().get(i).id.equals(id)) {
                            department.getUsers().get(i).getWorkdays().clear();
                            department.getUsers().remove(i);
                        }
                    }
                }
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).id.equals(id)) {
                        users.get(i).getWorkdays().clear();
                        users.remove(i);
                    }
                }
                return new RedirectView("/administrator/user");
            }
        }
        return new RedirectView("");
    }
}