package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import nl.bonfire17.hourregister.wrappers.AdminDepartmentWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("/administrator")
public class AdminController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();
    private ArrayList<User> users = DataProviderSingleton.getInstance().getUsers();
    private ArrayList<Workday> workdays = DataProviderSingleton.getInstance().getWorkdays();

    /*
        Workday GetMapping
     */

    //Load default admin page
    @GetMapping
    public String defaultPage(Model model){
        defaultWorkdayPage(model);
        return "admin/workday-overview";
    }

    //Load all workdays per user
    @GetMapping(path = "/workday")
    public String defaultWorkdayPage(Model model){
        ArrayList<HashMap<String, String>> sendData = new ArrayList<>();
        for(User user: users){
            ArrayList<Workday> workdays = user.getWorkdays();
            for(Workday workday: workdays){
                sendData.add(loadWorkdayHashmap(user, workday));
            }
        }
        model.addAttribute("workdays", sendData);
        model.addAttribute("header", "Werkdagen");
        return "admin/workday-overview";
    }

    //Load all workdays that are not validated
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
        model.addAttribute("header", "Ongevalideerde Werkdagen");
        return "admin/workday-overview";
    }

    //Load specific workday by id
    @GetMapping(path = "/workday/{workdayId}")
    public String loadWorkday(Model model, @PathVariable("workdayId") String id){
        Workday workday = null;
        for(Workday workdayTemp: workdays){
            if(workdayTemp.id.equals(id)){
                workday = workdayTemp;
            }
        }
        model.addAttribute("startdate", workday.getStartDateUnix());
        model.addAttribute("starttime", workday.getStartTimeUnix());
        model.addAttribute("isWorking", workday.isWorking());
        if(!workday.isWorking()){
            model.addAttribute("enddate", workday.getEndDateUnix());
            model.addAttribute("endtime", workday.getEndTimeUnix());
            model.addAttribute("breaktime", workday.getBreakTimeUnix());
            model.addAttribute("validated", workday.getValidated());
        }
        model.addAttribute("id", workday.id);
        return "admin/workday";
    }



    /*
        User GetMapping
     */

    @GetMapping(path = "/user")
    public String defaultUserPage(Model model){
        ArrayList<HashMap<String, String>> sendData = new ArrayList<>();
        for(Department department: departments){
            ArrayList<User> users = department.getUsers();
            for(User user: users){
                sendData.add(loadUserHashmap(department, user));
            }
        }
        model.addAttribute("users", sendData);
        model.addAttribute("header", "Gerbuikers");
        return "admin/user-overview";
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

    /*
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
    */


    private HashMap<String, String> loadWorkdayHashmap(User user, Workday workday){
        HashMap<String, String> map = new HashMap<>();
        boolean isWorking = workday.isWorking();
        map.put("id", workday.id);
        map.put("firstname", user.getFirstname());
        map.put("lastname", user.getLastname());
        map.put("date", workday.getDateFormated());
        map.put("starttime", workday.getStartTimeFormated());
        map.put("endtime", isWorking ? "-" : workday.getEndTimeFormated());
        map.put("breaktime", isWorking ? "-" : workday.getBreakTimeFormated());
        map.put("validated", workday.getValidated() ? "Ja" : "Nee");
        return map;
    }

    private HashMap<String, String> loadUserHashmap(Department department, User user){
        HashMap<String, String> map = new HashMap<>();
        //Get last workday
        LocalDateTime high = null;
        for(int i = 0; i < user.getWorkdays().size(); i++){
            if(high == null || user.getWorkdays().get(i).getStartTime().isAfter(high)){
                high = user.getWorkdays().get(i).getStartTime();
            }
        }
        map.put("id", user.id);
        map.put("firstname", user.getFirstname());
        map.put("lastname", user.getLastname());
        map.put("dateOfBirth", user.getDateOfBirthFormated());
        map.put("department", department.getName());
        map.put("lastWorkday", high == null ? "-" : high.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        map.put("isWorking", user.isWorking() ? "Ja" : "Nee");
        map.put("isAdmin", user.isAdmin() ? "Ja" : "Nee");
        return map;
    }
}
