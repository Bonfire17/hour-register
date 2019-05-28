package nl.bonfire17.hourregister.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workday")
public class WorkdayController {

    @GetMapping
    @ResponseBody
    public String getWorkdays() {
        return "test";
    }

    @PostMapping
    @ResponseBody
    public void addWorkday() {

    }

    @PutMapping
    @ResponseBody
    public void editWorkday() {

    }

    @DeleteMapping
    @ResponseBody
    public void deleteWorkday() {

    }

}
