package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();


    @GetMapping
    @ResponseBody
    public ArrayList<Department> getDepartments() {
        return departments;
    }

    @PostMapping
    @ResponseBody
    public void addDepartment() {

    }

    @PutMapping
    @ResponseBody
    public void editDepartment() {

    }

    @DeleteMapping
    @ResponseBody
    public void deleteDepartment() {

    }

}

