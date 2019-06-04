package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.wrappers.TransferWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<User> users = DataProviderSingleton.getInstance().getUserList();

    @GetMapping
    public String firstPage(){
        return "department";
    }

    /*
    @GetMapping
    @ResponseBody
    public ArrayList<Department> getDepartments() {
        return departments;
    }
    */

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

    @PutMapping(path = "/transfer")
    @ResponseBody
    public void transferUser(TransferWrapper tfw) {
        String oldId = tfw.oldDepartment;
        String newId = tfw.newDepartment;
        String userId = tfw.userId;

        //remove from old department
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(oldId)) {
                departments.get(i).removeUserById(userId);
            }
        }
        //Add to new department
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(newId)) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getId().equals(userId)) {
                        departments.get(i).getUsers().add(users.get(j));
                    }
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

