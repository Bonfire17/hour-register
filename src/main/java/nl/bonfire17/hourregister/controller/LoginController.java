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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@Controller
@RequestMapping
public class LoginController {

    private ArrayList<User> users = DataProviderSingleton.getInstance().getUsers();

    //Used for login screen
    @GetMapping
    public String login(HttpSession session, Model model, @CookieValue(name = "isWrong", defaultValue = "false") boolean isWrong) {

        String username = "N/A";

        if (session.getAttribute("userId") != null){
            for (int i = 0; i < this.users.size(); i++) {
                if (session.getAttribute("userId").equals(this.users.get(i).id)) {

                    username = this.users.get(i).getUsername();
                    return "redirect:/clocksystem?username=" + username;
                }
            }
        }

        model.addAttribute("iswronglogin", isWrong);

        return "index";
    }

    //Checks if you entered the right username/password
    @PostMapping(path = "/logincheck")
    public RedirectView loginCheck(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "password") String password,
                                   HttpSession session, HttpServletResponse response) {

        for (int i = 0; i < this.users.size(); i++) {
            if (username.equals(this.users.get(i).getUsername()) && password.equals(this.users.get(i).getPassword())) {

                session.setAttribute("userId", this.users.get(i).id);
                session.setMaxInactiveInterval(300);

                return new RedirectView("/clocksystem?username=" + username);
            }
        }
        Cookie cookie = new Cookie("isWrong", "true");
        cookie.setMaxAge(5);
        response.addCookie(cookie);
        return new RedirectView("");
    }

    //Ends the session
    @PostMapping(path = "/logout")
    public RedirectView logout(HttpSession session) {

        session.removeAttribute("userId");

        return new RedirectView("");

    }



}

