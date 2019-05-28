package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.models.Department;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    private ArrayList<Department> departments = new ArrayList<>();
    private Department department = new Department("test", "testinfo");
    private Department department1 = new Department("test1", "testinfo1");


    @GetMapping
    @ResponseBody
    public List<Department> getDepartments() {
        List<Department> showDepartments = new ArrayList<>();
        showDepartments.add(this.department);
        showDepartments.add(this.department1);
        return showDepartments;
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

