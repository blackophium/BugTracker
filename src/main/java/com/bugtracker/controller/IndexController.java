package com.bugtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.bugtracker.person.Person;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/contact")
    public String contact(Model model){
//        model.addAttribute("isAdmin", true);
        return "contact";
    }
}
