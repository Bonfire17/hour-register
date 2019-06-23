package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import nl.bonfire17.hourregister.wrappers.WorkdayUserWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/*
    Both the admin and a user should be able to access this controller. Some methods however should not be accessible by the user
*/

@Controller
@RequestMapping("/workday")
public class WorkdayController {

    private ArrayList<User> users = DataProviderSingleton.getInstance().getUsers();
    private ArrayList<Workday> workdays = DataProviderSingleton.getInstance().getWorkdays();

    //Admin
    //Edit a existing workday
    @PostMapping(path = "/edit/{workdayId}")
    public RedirectView editWorkday(@RequestParam(name = "start-date") String startdate,
                                   @RequestParam(name = "start-time") String starttime,
                                   @RequestParam(name = "end-date", required = false) String enddate,
                                   @RequestParam(name = "end-time", required = false) String endtime,
                                   @RequestParam(name = "break-time", required = false) String breaktime,
                                   @RequestParam(name = "validated", required = false) String validated,
                                   @PathVariable("workdayId") String id, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("");//error
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < workdays.size(); i++) {
            if (workdays.get(i).getId().equals(id)) {
                Workday workday = workdays.get(i);
                workday.setStartTime(LocalDateTime.parse(startdate + " " + starttime, dateTimeFormatter));

                //The administrator should only change the endtime/enddate/validate when the user has clocked out.
                if(!workday.isWorking()) {
                    workday.setEndTime(LocalDateTime.parse(enddate + " " + endtime, dateTimeFormatter));
                    workday.setBreakTime(LocalTime.parse(breaktime, timeFormatter));
                    if (validated != null) {
                        workday.setValidated(true);
                    } else {
                        workday.setValidated(false);
                    }
                }
            }
        }
        return new RedirectView("/administrator/workday");
    }

    //Admin
    //Delete a existing workday
    @PostMapping(path = "/delete/{workdayId}")
    public RedirectView deleteWorkday(@PathVariable("workdayId") String id, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("");//error
        }
        System.out.println(id);
        for (int i = 0; i < workdays.size(); i++) {
            if (workdays.get(i).getId().equals(id)) {
                workdays.remove(i);
            }
        }
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < users.get(i).getWorkdays().size(); j++) {
                if (users.get(i).getWorkdays().get(j).getId().equals(id)) {
                    users.get(i).getWorkdays().remove(j);
                }
            }
        }
        return new RedirectView("/administrator/workday");
    }

    //Checks if current user is admin
    public boolean checkAdmin(HttpSession session) {
        for (int i = 0; i < this.users.size(); i++) {
            if (session.getAttribute("userId").equals(this.users.get(i).id) && this.users.get(i).isAdmin()) {
                return true;
            }
        }
        return false;
    }
}
