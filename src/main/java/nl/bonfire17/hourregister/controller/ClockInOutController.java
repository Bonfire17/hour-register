package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
@RequestMapping("/clocksystem")
public class ClockInOutController {

    private ArrayList<User> users = DataProviderSingleton.getInstance().getUsers();

    @GetMapping
    public String defaultUserPage(HttpSession  session){
        if (session.getAttribute("userId") != null) {
            for (int i = 0; i < this.users.size(); i++) {
                if (session.getAttribute("userId").equals(this.users.get(i).id) && !this.users.get(i).isWorking()) {
                    return "user/clockin";
                } else if (session.getAttribute("userId").equals(this.users.get(i).id) && this.users.get(i).isWorking()) {
                    return "user/clockout";
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping(path = "/clockin")
    public RedirectView clockin(HttpSession session) {

        for (int i = 0; i < this.users.size(); i++) {
            if (session.getAttribute("userId").equals(this.users.get(i).id) && !this.users.get(i).isWorking()) {
                this.users.get(i).clockIn();
            }
        }
        return new RedirectView("/clocksystem");
    }

    @PostMapping(path = "/clockout")
    public RedirectView clockout (HttpSession session, HttpServletResponse response, @RequestParam(name = "break-time", required = false) String breaktime) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < this.users.size(); i++) {
            if (session.getAttribute("userId").equals(this.users.get(i).id) && this.users.get(i).isWorking()) {

                if (breaktime != null) {
                    this.users.get(i).clockOut(LocalTime.parse(breaktime, timeFormatter));
                    Cookie cookie = new Cookie("break-time", breaktime);
                    response.addCookie(cookie);
                } else {
                    this.users.get(i).clockOut(LocalTime.parse("00:00", timeFormatter));
                }
            }
        }
        return new RedirectView("/clocksystem");
    }
}
