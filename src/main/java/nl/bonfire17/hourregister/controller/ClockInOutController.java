package nl.bonfire17.hourregister.controller;

import nl.bonfire17.hourregister.data.DataProviderSingleton;
import nl.bonfire17.hourregister.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    private ArrayList<User> users;

    //Checks if you are clocked in and sends you to the right page.
    @GetMapping
    public String defaultUserPage(HttpSession  session, Model model, @CookieValue(value = "lastbreaktime", defaultValue = "00:00") String cookieValue, @RequestParam("username") String value){
        users = DataProviderSingleton.getInstance().getUsers();
        if (session.getAttribute("userId") != null) {
            for (int i = 0; i < this.users.size(); i++) {
                if (session.getAttribute("userId").equals(this.users.get(i).id) && !this.users.get(i).isWorking()) {
                    model.addAttribute("isadmin", this.users.get(i).isAdmin());
                    model.addAttribute("username", value);
                    return "user/clockin";
                } else if (session.getAttribute("userId").equals(this.users.get(i).id) && this.users.get(i).isWorking()) {
                    model.addAttribute("isadmin", this.users.get(i).isAdmin());
                    model.addAttribute("lastbreaktime", cookieValue);
                    model.addAttribute("username", value);
                    return "user/clockout";
                }
            }
        }
        return "redirect:/";
    }

    //Clocks you in
    @PostMapping(path = "/clockin")
    public RedirectView clockin(HttpSession session) {

        String username = "N/A";
        User user = DataProviderSingleton.getInstance().getUserById(session.getAttribute("userId").toString());
        if (session.getAttribute("userId").equals(user.id) && !user.isWorking()) {
            user.clockIn();
            username = user.getUsername();
        }
        return new RedirectView("/clocksystem?username=" + username);
    }

    //Clocks you out
    @PostMapping(path = "/clockout")
    public RedirectView clockout (HttpSession session, HttpServletResponse response, @RequestParam(name = "break", required = false) String breaktime) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String username = "N/A";

        User user = DataProviderSingleton.getInstance().getUserById(session.getAttribute("userId").toString());
        if (user != null && user.isWorking()) {

            if (breaktime != null) {
                user.clockOut(LocalTime.parse(breaktime, timeFormatter));
                Cookie cookie = new Cookie("lastbreaktime", breaktime);
                response.addCookie(cookie);
            } else {
                user.clockOut(LocalTime.parse("00:00", timeFormatter));
            }
            username = user.getUsername();
        }
        return new RedirectView("/clocksystem?username=" + username);
    }
}
