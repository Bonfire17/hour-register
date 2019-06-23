package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.Department;
import nl.bonfire17.hourregister.models.User;
import nl.bonfire17.hourregister.models.Workday;
import nl.bonfire17.hourregister.wrappers.WorkdayUserWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/*
    Both the admin and a user should be able to access this controller. Some methods however should not be accessible by the user
*/

@Controller
@RequestMapping("/workday")
public class WorkdayController {

    private ArrayList<User> users;
    private ArrayList<Workday> workdays;

    //Admin
    //Edit a existing workday
    @PostMapping(path = "/edit/{workdayId}")
    public RedirectView editWorkday(@RequestParam(name = "start-date", required = false) String startdate,
                                   @RequestParam(name = "start-time", required = false) String starttime,
                                   @RequestParam(name = "end-date", required = false) String enddate,
                                   @RequestParam(name = "end-time", required = false) String endtime,
                                   @RequestParam(name = "break-time", required = false) String breaktime,
                                   @RequestParam(name = "validated", required = false) String validated,
                                   @PathVariable("workdayId") String id, HttpSession session) {

        boolean inputValid = true;

        if (!checkAdmin(session)) {
            return new RedirectView("/admin/error");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Workday workday = DataProviderSingleton.getInstance().getWorkdayById(id);
        if(workday == null){
            inputValid = false;
        }

        //Validate user input
        if((startdate == null || starttime == null) || ((enddate == null || endtime == null || breaktime == null) && !workday.isWorking())){
            inputValid = false;
        }

        //Validate if time can be parsed
        LocalDateTime startParsed = null;
        LocalDateTime endParsed = null;
        LocalTime breakParsed = null;
        try {
            startParsed = LocalDateTime.parse(startdate + " " + starttime, dateTimeFormatter);
            if (!workday.isWorking()) {
                endParsed = LocalDateTime.parse(enddate + " " + endtime, dateTimeFormatter);
                breakParsed = LocalTime.parse(breaktime, timeFormatter);
            }
        }catch (DateTimeParseException e){
            inputValid = false;
        }

        if(inputValid){
            workday.setStartTime(startParsed);
            //The administrator should only change the endtime/enddate/validate when the user has clocked out.
            if (!workday.isWorking()) {
                workday.setEndTime(endParsed);
                workday.setBreakTime(breakParsed);
                if (validated != null) {
                    workday.setValidated(true);
                } else {
                    workday.setValidated(false);
                }
            }
            return new RedirectView("/administrator/workday");
        }else{
            return new RedirectView("/user/error?msg=input");
        }

    }

    //Admin
    //Delete a existing workday
    @PostMapping(path = "/delete/{workdayId}")
    public RedirectView deleteWorkday(@PathVariable("workdayId") String id, HttpSession session) {

        if (!checkAdmin(session)) {
            return new RedirectView("/error");//error
        }

        workdays = DataProviderSingleton.getInstance().getWorkdays();
        users = DataProviderSingleton.getInstance().getUsers();

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

    @GetMapping(path = "/error")
    public String loadErrorPage(Model model, @RequestParam("msg") String msg) {
        switch (msg) {
            case "input":
                model.addAttribute("message", "Uw ingevoerde gegevens kloppen niet!");
                break;
            default:
                model.addAttribute("message", "O ow, er is hier iets niet pluis gegaan D:");
                break;
        }
        return "/admin/error";
    }

    //Checks if current user is admin
    public boolean checkAdmin(HttpSession session) {
        if(session != null && session.getAttribute("userId") != null){
            User user = DataProviderSingleton.getInstance().getUserById(session.getAttribute("userId").toString());
            if(user != null && user.isAdmin()){
                return true;
            }
        }
        return false;
    }

    private boolean validateString(String string){
        if(string != null && !string.equals("")){
            return true;
        }
        return false;
    }
}
