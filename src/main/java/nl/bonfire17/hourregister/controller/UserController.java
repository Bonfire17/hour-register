package nl.bonfire17.hourregister.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping
    @ResponseBody
    public String getUsers() {
        return "test";
    }

    @PostMapping
    @ResponseBody
    public void addUser() {

    }

    @PutMapping
    @ResponseBody
    public void editUser() {

    }

    @DeleteMapping
    @ResponseBody
    public void deleteUser() {

    }
}