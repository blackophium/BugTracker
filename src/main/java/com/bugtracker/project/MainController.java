package com.bugtracker.project;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {

        return "redirect:/issue";
    }

    @GetMapping("/create")
    @Secured("ROLE_CREATE_PROJECT")
    ModelAndView create(){
        ModelAndView modelAndView = new ModelAndView("project/create");
        modelAndView.addObject("project", new Project());

        return modelAndView;
    }

    @PostMapping("/save")
    @Secured("ROLE_CREATE_PROJECT")
    ModelAndView createNewProject(@ModelAttribute @Valid Project project, BindingResult bindingresult) {

        ModelAndView modelAndView = new ModelAndView();
        if (bindingresult.hasErrors()) {
            modelAndView.setViewName("projects/create");
            return modelAndView;
        }

//       projectRepository.save(project);
        modelAndView.setViewName("redirect:/projects");

        return modelAndView;
    }

}