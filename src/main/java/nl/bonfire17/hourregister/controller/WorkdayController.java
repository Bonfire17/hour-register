package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/workday")
public class WorkdayController {

    private ArrayList<Department> departments = DataProviderSingleton.getInstance().getDepartments();

    @GetMapping
    @ResponseBody
    public ArrayList<Workday> getWorkdays(@RequestBody User user) {
        String userId = user.getId();
        ArrayList<Workday> workdays = new ArrayList<Workday>();
        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                if (departments.get(i).getUsers().get(j).getId().equals(userId)) {
                    for (int k = 0; k < departments.get(i).getUsers().get(j).getWorkdays().size(); k++) {
                        workdays.add(departments.get(i).getUsers().get(j).getWorkdays().get(k));
                    }
                }
            }
        }
        return workdays;
    }

    @PostMapping
    @ResponseBody
    public void addWorkday(@RequestBody WorkdayUserWrapper wuw) {
        String userId = wuw.user.getId();
        LocalTime breakTime = LocalTime.of(0,0,0);

        if (wuw.workday.getBreakTime() != null) {
            breakTime = wuw.workday.getBreakTime();
        }

        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                if (departments.get(i).getUsers().get(j).getId().equals(userId)) {
                    if (departments.get(i).getUsers().get(j).clockIn(userId) == false) {
                        departments.get(i).getUsers().get(j).clockOut(breakTime);
                    }
                }
            }
        }
    }

    @PutMapping
    @ResponseBody
    public void editWorkday(@RequestBody Workday workday) {
        String id = workday.getId();

        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                for (int k = 0; k < departments.get(i).getUsers().get(j).getWorkdays().size(); k++) {
                    if (departments.get(i).getUsers().get(j).getWorkdays().get(k).getId().equals(id)) {
                        if (!workday.getStartTime().equals(null)) {
                            departments.get(i).getUsers().get(j).getWorkdays().get(k).setStartTime(workday.getStartTime());
                        }
                        if (!workday.getEndTime().equals(null)) {
                            departments.get(i).getUsers().get(j).getWorkdays().get(k).setEndTime(workday.getEndTime());
                        }
                        if (!workday.getBreakTime().equals(null)) {
                            departments.get(i).getUsers().get(j).getWorkdays().get(k).setBreakTime(workday.getBreakTime());
                        }
                    }
                }
            }
        }
    }

    @DeleteMapping
    @ResponseBody
    public void deleteWorkday(@RequestBody Workday workday) {
        String id = workday.getId();

        for (int i = 0; i < departments.size(); i++) {
            for (int j = 0; j < departments.get(i).getUsers().size(); j++) {
                for (int k = 0; k < departments.get(i).getUsers().get(j).getWorkdays().size(); k++) {
                    if (departments.get(i).getUsers().get(j).getWorkdays().get(k).getId().equals(id)) {
                        departments.get(i).getUsers().get(j).getWorkdays().remove(k);
                    }
                }
            }
        }
    }

    class WorkdayUserWrapper{
        public Workday workday;
        public User user;

        public WorkdayUserWrapper(Workday workday, User user) {
            this.workday = workday;
            this.user = user;
        }
    }
}
