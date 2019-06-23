package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.wrappers.UserDepartmentWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/*
    Both the admin and a user should be able to access this controller. Some methods however should not be accessible by the user
 */

@Controller
@RequestMapping("/user")
public class UserController {

    private ArrayList<Department> departments;
    private ArrayList<User> users;

    //Admin
    //Add a new user
    @PostMapping(path = "/add")
    public RedirectView addUser(@RequestParam(name = "username", required = false) String username,
                        @RequestParam(name = "email", required = false) String email,
                        @RequestParam(name = "firstname", required = false) String firstname,
                        @RequestParam(name = "lastname", required = false) String lastname,
                        @RequestParam(name = "date-of-birth", required = false) String dateOfBirth,
                        @RequestParam(name = "administrator", required = false) String administrator,
                        @RequestParam(name = "password", required = false) String password,
                        @RequestParam(name = "department", required = false) String departmentId, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("/user/error");//error
        }

        boolean userValidate = true;

        //Validate if all params are set
        if(!validateString(username) || !validateString(email) || !validateString(firstname) || !validateString(lastname) ||
                !validateString(dateOfBirth) || !validateString(password) || !validateString(departmentId)){
            userValidate = false;
        }

        //Validate if department exist
        if(DataProviderSingleton.getInstance().getDepartmentById(departmentId) == null){
            userValidate = false;
        }

        //Try to parse the date
        LocalDate parsedDateOfBirth = null;
        try{
            parsedDateOfBirth = LocalDate.parse(dateOfBirth);
        }catch (DateTimeParseException e){
            userValidate = false;
        }

        if(userValidate) {
            if (administrator != null) {
                DataProviderSingleton.getInstance().getDepartmentById(departmentId).addUser(new Administrator(username, email, firstname, lastname, password, parsedDateOfBirth));
            } else {
                DataProviderSingleton.getInstance().getDepartmentById(departmentId).addUser(new User(username, email, firstname, lastname, password, parsedDateOfBirth));
            }
        }else{
            return new RedirectView("/user/error?msg=input");
        }
        return new RedirectView("/administrator/user");
    }

    //Admin
    //Edit a existing user
    @PostMapping(path = "/edit/{id}")
    public RedirectView editUser(@PathVariable(name = "id", required = false) String id,
                                 @RequestParam(name = "username", required = false) String username,
                                 @RequestParam(name = "email", required = false) String email,
                                 @RequestParam(name = "firstname", required = false) String firstname,
                                 @RequestParam(name = "lastname", required = false) String lastname,
                                 @RequestParam(name = "date-of-birth", required = false) String dateOfBirth,
                                 @RequestParam(name = "administrator", required = false) String administrator,
                                 @RequestParam(name = "old-password", required = false) String oldPassword,
                                 @RequestParam(name = "new-password", required = false) String newPassword, HttpSession session) {

        boolean userValidate = true;

        if (!checkAdmin(session)) {
            return new RedirectView("/user/error");
        }

        //Validate id
        if(DataProviderSingleton.getInstance().getUserById(id) == null){
            userValidate = false;
        }

        //Validate if all params are set
        if(!validateString(username) || !validateString(email) || !validateString(firstname) || !validateString(lastname) || !validateString(dateOfBirth)) {
            userValidate = false;
        }

        //Try to parse the date
        LocalDate parsedDateOfBirth = null;
        try{
            parsedDateOfBirth = LocalDate.parse(dateOfBirth);
        }catch (DateTimeParseException e) {
            userValidate = false;
        }
        if(userValidate) {
            User user = DataProviderSingleton.getInstance().getUserById(id);
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setDateOfBirth(parsedDateOfBirth);

            //Check if the administrator box is checked AND if the user is not already a administrator
            if (administrator != null && !(user instanceof Administrator)) {
                DataProviderSingleton.getInstance().replaceUser(user.getAdministrator());
            } else if (administrator == null && user instanceof Administrator) {
                DataProviderSingleton.getInstance().replaceUser(((Administrator) user).getUser());
            }
            if (oldPassword.equals(user.getPassword()) && validateString(oldPassword) && validateString(newPassword)) {
                user.setPassword(newPassword);
            }else if(validateString(oldPassword) || validateString(newPassword)){
                return new RedirectView("/user/error?msg=password");
            }
            return new RedirectView("/administrator/user");
        }else{
            return new RedirectView("/user/error?msg=input");
        }
    }

    //Admin
    //Transfer a user from one department to another
    @PostMapping(path = "/transfer/{id}")
    public RedirectView transferUser(@PathVariable(name = "id") String id, @RequestParam(name = "department") String targetDepartment, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("/department/error");//error
        }
        Department oldDepartment, newDepartment;
        User transferUser;

        departments = DataProviderSingleton.getInstance().getDepartments();

        for(int i = 0; i < departments.size(); i++){
           Department department = departments.get(i);
           for(User user: department.getUsers()){
               if(user.id.equals(id)){
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

    //Admin
    //Delete a user
    @PostMapping(path = "/delete/{id}")
    public RedirectView deleteUser(@PathVariable(name = "id") String id, HttpSession session) {

        departments = DataProviderSingleton.getInstance().getDepartments();
        users = DataProviderSingleton.getInstance().getUsers();

        if (!checkAdmin(session)) {
            return new RedirectView("/department/error");//error
        }
        for(Department department: departments) {
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

    @GetMapping(path = "/error")
    public String loadErrorPage(Model model, @RequestParam("msg") String msg){
        switch (msg){
            case "input":
                model.addAttribute("message", "Uw ingevoerde gegevens kloppen niet!");
                break;
            case "password":
                model.addAttribute("message", "Het oude wachtwoord klopte niet!");
                break;
            default:
                model.addAttribute("message", "O ow, er is hier iets niet pluis gegaan D:");
                break;
        }
        return "/admin/error";
    }


    //Checks if current user is admin
    public boolean checkAdmin(HttpSession session) {
        if(session != null && session.getAttribute("userId") != null){
            User user = DataProviderSingleton.getInstance().getUserById(session.getAttribute("userId").toString());
            if(user != null && user.isAdmin()){
                return true;
            }
        }
        return false;
    }

    private boolean validateString(String string){
        if(string != null && !string.equals("")){
            return true;
        }
        return false;
    }


}