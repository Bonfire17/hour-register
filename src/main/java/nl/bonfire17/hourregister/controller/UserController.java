package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class UserController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();

    @GetMapping
    @ResponseBody
    public String getUsers() {
        return "test";
    }

    @PostMapping
    @ResponseBody
    public void addUser() {

    }

    @PutMapping
    @ResponseBody
    public void editUser() {

    }

    @DeleteMapping
    @ResponseBody
    public void deleteUser() {
    }
}