package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.wrappers.TransferWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<User> users = DataProviderSingleton.getInstance().getUsers();

    @GetMapping
    @ResponseBody
    public ArrayList<Department> getDepartments() {
        return departments;
    }

    @PostMapping
    @ResponseBody
    public void addDepartment(@RequestBody Department department) {
        departments.add(department);
    }

    @PutMapping
    @ResponseBody
    public void editDepartment(@RequestBody Department department) {
        String id = department.getId();

        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(id)) {
                if (!department.getName().equals(null)) {
                    departments.get(i).setName(department.getName());
                }
                if (!department.getInfo().equals(null)) {
                    departments.get(i).setInfo(department.getInfo());
                }
            }
        }
    }

    @DeleteMapping
    @ResponseBody
    public void deleteDepartment(@RequestBody  Department department) {
        String id = department.getId();

        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(id)) {
                departments.remove(i);
            }
        }
    }
}

