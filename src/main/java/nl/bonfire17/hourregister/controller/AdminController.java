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

    private static final String ADMIN = "ADMIN";
    private static final String ISWORKING = "ISWORKING";
    private static final String ALLUSERS = "ALLUSERS";
    private static final String UNVALIDATED = "UNVALIDATED";
    private static final String ALLWORKAYS = "ALLWORKDAYS";

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
        ArrayList<HashMap<String, String>> sendData = getWorkdayHashMapArray(users);
        model.addAttribute("workdays", sendData);
        model.addAttribute("header", "Werkdagen");
        return "admin/workday-overview";
    }

    //Load all workdays that are not validated
    @GetMapping(path = "/workday/unvalidated")
    public String loadUnvalidatedWorkdays(Model model){
        ArrayList<HashMap<String, String>> sendData = getWorkdayHashMapArray(users, AdminController.UNVALIDATED);
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
        Workday Methods
     */

    //Search for all workdays that is matching a criteria
    private ArrayList<HashMap<String, String>> getWorkdayHashMapArray(ArrayList<User> users, String search){
        ArrayList<HashMap<String, String>> sendData = new ArrayList<>();
        for(User user: users){
            ArrayList<Workday> workdays = user.getWorkdays();
            for(Workday workday: workdays){
                if(search.equals(AdminController.UNVALIDATED) && !workday.getValidated()){
                    sendData.add(loadWorkdayHashmap(user, workday));
                }else if(search.equals(AdminController.ALLWORKAYS)){
                    sendData.add(loadWorkdayHashmap(user, workday));
                }
            }
        }
        return sendData;
    }

    //Same method but with a default search parameter
    private ArrayList<HashMap<String, String>> getWorkdayHashMapArray(ArrayList<User> users){
        return getWorkdayHashMapArray(users, AdminController.ALLWORKAYS);
    }

    //Load user data into a HashMap
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

    /*
        User GetMapping
     */

    //Load all users and administrators
    @GetMapping(path = "/user")
    public String defaultUserPage(Model model){
        ArrayList<HashMap<String, String>> sendData = getUserHashMapArray(departments);
        model.addAttribute("users", sendData);
        model.addAttribute("header", "Gerbuikers");
        return "admin/user-overview";
    }

    //Load all administrators
    @GetMapping(path = "/user/admin")
    public String loadAdministrators(Model model){
        ArrayList<HashMap<String, String>> sendData = getUserHashMapArray(departments, AdminController.ADMIN);
        model.addAttribute("users", sendData);
        model.addAttribute("header", "Administratoren");
        return "admin/user-overview";
    }

    //Load all users and administrators that are currently working
    @GetMapping(path = "/user/working")
    public String loadUsersThatAreWorking(Model model){
        ArrayList<HashMap<String, String>> sendData = getUserHashMapArray(departments, AdminController.ISWORKING);
        model.addAttribute("users", sendData);
        model.addAttribute("header", "Actieve mederwerkers");
        return "admin/user-overview";
    }

    //Load all users from a department
    @GetMapping(path = "/user/department/{departmentId}")
    public String loadUsersByDepartment(Model model, @PathVariable("departmentId") String departmentId){
        Department department = DataProviderSingleton.getInstance().getDepartmentById(departmentId);
        ArrayList<HashMap<String, String>> sendData = getUserHashMapArray(department);
        model.addAttribute("users", sendData);
        model.addAttribute("header", "Department: " + department.getName());
        return "admin/user-overview";
    }


    /*
        User methods
     */

    //Search for all users that is matching a criteria
    private ArrayList<HashMap<String, String>> getUserHashMapArray(ArrayList<Department> departments, String search){
        ArrayList<HashMap<String, String>> sendData = new ArrayList<>();
        for(Department department: departments){
            ArrayList<User> users = department.getUsers();
            for(User user: users){
                if(search.equals(AdminController.ADMIN) && user instanceof Administrator){
                    sendData.add(loadUserHashmap(department, user));
                }else if(search.equals(AdminController.ISWORKING) && user.isWorking()){
                    sendData.add(loadUserHashmap(department, user));
                }else if(search.equals(AdminController.ALLUSERS)) {
                    sendData.add(loadUserHashmap(department, user));
                }
            }
        }
        return sendData;
    }

    //Same method but with a default search parameter
    private ArrayList<HashMap<String, String>> getUserHashMapArray(ArrayList<Department> departments){
        return getUserHashMapArray(departments, AdminController.ALLUSERS);
    }

    //Same method but with a single department parameter
    private ArrayList<HashMap<String, String>> getUserHashMapArray(Department department){
        ArrayList<Department> departments = new ArrayList<>();
        departments.add(department);
        return getUserHashMapArray(departments, AdminController.ALLUSERS);
    }

    //Load user data into a HashMap
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
