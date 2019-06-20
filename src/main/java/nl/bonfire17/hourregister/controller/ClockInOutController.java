package nl.bonfire17.hourregister.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clocksystem")
public class ClockInOutController {


    @GetMapping
    public String defaultUserPage(Model model){
        model.addAttribute("header", "Gebruiker Paneel");
        return "user/clockin";
    }
}
