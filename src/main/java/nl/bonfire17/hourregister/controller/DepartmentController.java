package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.wrappers.TransferWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    private ArrayList<Department> departments;
    private ArrayList<User> users;

    //Admin
    //Edit a existing department
    @PostMapping(path = "/edit/{departmentId}")
    public RedirectView editDepartment(@PathVariable(value = "departmentId", required = false) String id,
                                       @RequestParam(value = "department-name", required = false) String name,
                                       @RequestParam(value = "department-info", required = false, defaultValue = "") String info, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("/department/error");//error
        }
        boolean departmentValidated = true;

        Department department = DataProviderSingleton.getInstance().getDepartmentById(id);

        //Check if the department exists
        //Check if department-name is valid
        if (department == null || !validateString(name)) {
            departmentValidated = false;
        }

        if(departmentValidated){
            department.setName(name);
            department.setInfo(info);
            return new RedirectView("/administrator/department");
        }else{
            return new RedirectView("/department/error?msg=input");
        }
    }

    //Admin
    //Add a new Department
    @PostMapping(path = "/add")
    public RedirectView editDepartment(@RequestParam(value = "department-name", required = false) String name,
                                       @RequestParam(value = "department-info", required = false, defaultValue = "") String info, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("department/error");//error
        }
        boolean departmentValidated = true;

        //Check if the department exists
        //Check if department-name is valid
        if (!validateString(name)) {
            departmentValidated = false;
        }

        if(departmentValidated){
            departments = DataProviderSingleton.getInstance().getDepartments();
            departments.add(new Department(name, info));
            return new RedirectView("/administrator/department");
        }else{
            return new RedirectView("/department/error?msg=input");
        }
    }

    //Admin
    //Delete a existing department
    @PostMapping(path = "/delete/{id}")
    public RedirectView deleteDepartment(@PathVariable("id") String id, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("department/error");//error
        }
        Department department = DataProviderSingleton.getInstance().getDepartmentById(id);
        //Check if the department exists
        if(department != null){
            //Loop all users of a department
            for(User user: department.getUsers()){
                user.getWorkdays().clear();
            }
            department.getUsers().clear();
            departments = DataProviderSingleton.getInstance().getDepartments();
            departments.remove(department);
            return new RedirectView("/administrator/department");
        }
        return new RedirectView("/department/error");
    }

    //Display a error msg
    @GetMapping(path = "/error")
    public String loadErrorPage(Model model, @RequestParam("msg") String msg){
        switch (msg){
            case "input":
                model.addAttribute("message", "Uw ingevoerde gegevens kloppen niet!");
                break;
            default:
                model.addAttribute("message", "O ow, er is hier iets niet pluis gegaan D:");
                break;
        }
        return "/admin/error";
    }

    private boolean validateString(String string){
        if(string != null && !string.equals("")){
            return true;
        }
        return false;
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
}

