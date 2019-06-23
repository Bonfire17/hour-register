package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Administrator;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/*
    Only the admin should jave access to the controller. All admin panel getters are here!
 */

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

    //Load user by id
    @GetMapping(path = "/user/{id}")
    public String loadUser(Model model, @PathVariable("id") String id){
        User user = DataProviderSingleton.getInstance().getUserById(id);
        model.addAttribute("add", false);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("firstname", user.getFirstname());
        model.addAttribute("lastname", user.getLastname());
        model.addAttribute("dateOfBirth", user.getDateOfBirthUnix());
        model.addAttribute("administrator",  user instanceof Administrator);
        model.addAttribute("action", "/user/edit/" + user.id);
        model.addAttribute("departments", getDepartmentData());
        model.addAttribute("header", "Gebruiker Aanpassen");
        return "admin/user";
    }

    //Get add user form
    @GetMapping(path = "/user/add")
    public String loadAddUserForm(Model model){
        ArrayList<HashMap<String, String>> departmentData = getDepartmentData();
        model.addAttribute("add", true);
        model.addAttribute("action", "/user/add");
        model.addAttribute("departments", getDepartmentData());
        model.addAttribute("header", "Gebruiker Toevoegen");
        return "/admin/user";
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

    //Get department data
    private ArrayList<HashMap<String, String>> getDepartmentData(){
        ArrayList<HashMap<String, String>> departmentData = new ArrayList<>();
        for(Department department: departments){
            HashMap<String, String> map = new HashMap<>();
            map.put("id", department.id);
            map.put("name", department.getName());
            departmentData.add(map);
        }
        return departmentData;
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
