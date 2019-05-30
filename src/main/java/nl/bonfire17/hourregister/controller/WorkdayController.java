package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import nl.bonfire17.hourregister.wrappers.WorkdayUserWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/workday")
public class WorkdayController {

    private ArrayList<User> users = DataProviderSingleton.getInstance().getUserList();
    private ArrayList<Workday> workdays = DataProviderSingleton.getInstance().getWorkdays();

    @GetMapping
    @ResponseBody
    public ArrayList<Workday> getWorkdays(@RequestBody User user) {
        String userId = user.getId();
        ArrayList<Workday> workdays = new ArrayList<Workday>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                return users.get(i).getWorkdays();
            }
        }
        return workdays;
    }

    @PostMapping
    @ResponseBody
    public void addWorkday(@RequestBody WorkdayUserWrapper wuw) {
        String userId = wuw.user;
        LocalTime breakTime = LocalTime.of(0,0,0);

        if (wuw.workday != null) {
            breakTime = wuw.workday;
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                if (!users.get(i).clockIn(userId)) {
                    users.get(i).clockOut(breakTime);
                }
            }
        }
    }

    @PutMapping
    @ResponseBody
    public void editWorkday(@RequestBody Workday workday) {
        String id = workday.getId();

        for (int i = 0; i < workdays.size(); i++) {
            if (workdays.get(i).getId().equals(id)) {
                if (!workday.getStartTime().equals(null)) {
                    workdays.get(i).setStartTime(workday.getStartTime());
                }
                if (!workday.getEndTime().equals(null)) {
                    workdays.get(i).setEndTime(workday.getEndTime());
                }
                if (!workday.getBreakTime().equals(null)) {
                    workdays.get(i).setBreakTime(workday.getBreakTime());
                }

            }
        }
    }

    @DeleteMapping
    @ResponseBody
    public void deleteWorkday(@RequestBody WorkdayUserWrapper wuw) {
        String id = wuw.workdayId;
        String userId = wuw.user;

        for (int i = 0; i < workdays.size(); i++) {
            if (workdays.get(i).getId().equals(id)) {
                workdays.remove(i);
            }
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                for (int j = 0; j < users.get(i).getWorkdays().size(); j++) {
                    if (users.get(i).getWorkdays().get(j).getId().equals(id)) {
                        users.get(i).getWorkdays().remove(j);
                    }
                }
            }
        }
    }
}
