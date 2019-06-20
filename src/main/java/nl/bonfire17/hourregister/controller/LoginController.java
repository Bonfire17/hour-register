package nl.bonfire17.hourregister.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    public String login(Model model){
        model.addAttribute("header", "Inloggen");
        return "index";
    }

}

