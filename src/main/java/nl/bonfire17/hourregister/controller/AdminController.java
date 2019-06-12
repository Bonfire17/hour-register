package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import nl.bonfire17.hourregister.wrappers.AdminDepartmentWrapper;
import nl.bonfire17.hourregister.wrappers.WorkdayUserWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("/administrator")
public class AdminController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<Administrator> administrators = DataProviderSingleton.getInstance().getAdministrators();
    private ArrayList<User> users = DataProviderSingleton.getInstance().getUserList();
    private ArrayList<Workday> workdays = DataProviderSingleton.getInstance().getWorkdays();

    @GetMapping
    public String defaultPage(Model model){
        defaultWorkdayPage(model);
        return "admin";
    }

    @GetMapping(path = "/workday")
    public String defaultWorkdayPage(Model model){
        loadWorkdays(model);
        return "admin";
    }

    @GetMapping(path = "/workday/validated")
    public String loadWorkdays(Model model){
        //Get all unvalidated workdays

        ArrayList<HashMap<String, String>> sendData = new ArrayList<>();
        for(User user: users){
            ArrayList<Workday> workdays = user.getWorkdays();
            for(Workday workday: workdays){
                sendData.add(loadWorkdayHashmap(user, workday));
            }
        }

        model.addAttribute("workdays", sendData);
        return "admin";
    }

    @GetMapping(path = "/workday/unvalidated")
    public String loadUnvalidatedWorkdays(Model model){
        //Get all unvalidated workdays

        ArrayList<HashMap<String, String>> sendData = new ArrayList<>();
        for(User user: users){
            ArrayList<Workday> workdays = user.getWorkdays();
            for(Workday workday: workdays){
                if(!workday.getValidated()) {
                    sendData.add(loadWorkdayHashmap(user, workday));
                }
            }
        }

        model.addAttribute("workdays", sendData);
        return "admin";
    }

    @GetMapping(path = "/users")
    @ResponseBody
    public ArrayList<User> getAllUsers() {
        return users;
    }

    @GetMapping(path = "/working")
    @ResponseBody
    public ArrayList<User> getWorkingUsers() {
        ArrayList<User> workingUsers = new ArrayList<User>();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getWorkdays().size() > 0 && users.get(i).getWorkdays().get(users.get(i).getWorkdays().size() - 1).isWorking()) {
                workingUsers.add(users.get(i));
            }
        }
        return workingUsers;
    }

    @PostMapping
    @ResponseBody
    public void addAdmin(@RequestBody AdminDepartmentWrapper adw) {
        administrators.add(adw.administrator);
        String departmentId = adw.department;
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId().equals(departmentId)) {
                departments.get(i).addUser(adw.administrator);
                users.add(adw.administrator);
            }
        }
    }

    private HashMap<String, String> loadWorkdayHashmap(User user, Workday workday){
        HashMap<String, String> map = new HashMap<>();
        map.put("firstname", user.getFirstname());
        map.put("lastname", user.getLastname());
        map.put("date", workday.getDateFormated());
        map.put("starttime", workday.getStartTimeFormated());
        map.put("endtime", workday.getEndTimeFormated());
        map.put("breaktime", workday.getBreakTimeFormated());
        map.put("validated", workday.getValidated() ? "Ja": "Nee");
        return map;
    }
}
