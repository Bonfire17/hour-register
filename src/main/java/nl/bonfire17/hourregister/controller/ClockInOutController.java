package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    public String defaultUserPage(HttpSession  session, Model model, @CookieValue(value = "lastbreaktime", defaultValue = "00:00") String cookieValue){
        if (session.getAttribute("userId") != null) {
            for (int i = 0; i < this.users.size(); i++) {
                if (session.getAttribute("userId").equals(this.users.get(i).id) && !this.users.get(i).isWorking()) {
                    return "user/clockin";
                } else if (session.getAttribute("userId").equals(this.users.get(i).id) && this.users.get(i).isWorking()) {
                    model.addAttribute("lastbreaktime", cookieValue);
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
    public RedirectView clockout (HttpSession session, HttpServletResponse response, @RequestParam(name = "break", required = false) String breaktime) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < this.users.size(); i++) {
            if (session.getAttribute("userId").equals(this.users.get(i).id) && this.users.get(i).isWorking()) {

                if (breaktime != null) {
                    this.users.get(i).clockOut(LocalTime.parse(breaktime, timeFormatter));
                    Cookie cookie = new Cookie("lastbreaktime", breaktime);
                    response.addCookie(cookie);
                } else {
                    this.users.get(i).clockOut(LocalTime.parse("00:00", timeFormatter));
                }
            }
        }
        return new RedirectView("/clocksystem");
    }
}
