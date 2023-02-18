package com.bugtracker.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bugtracker.auth.AuthorityRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class PersonController {

    private final PersonRepository personRepository;
    private final AuthorityRepository authorityRepository;
    private final PersonService personService;

    @Autowired
    public PersonController(PersonRepository personRepository, AuthorityRepository authorityRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.authorityRepository = authorityRepository;
        this.personService = personService;
    }

//    @GetMapping("/showForm")
//    public String showUserForm(Person person){
//        return "user/add-user";
//    }

    @GetMapping
    @Secured("ROLE_USERS_TAB")
    public String users(Model model){
        model.addAttribute("users", this.personRepository.findAll());
        return "user/users";
    }

//    @RequestMapping("/addUser") // czy tak moze byc? Czy tu lepiej post i skorzystac z osobnej metody GET 'showUserForm'?
//    public String addUser(@Valid Person person, BindingResult result, Model model){
//
//        if (result.hasErrors()){
//            return "user/add-user";
//        }
//        model.addAttribute("authorities",authorityRepository.findAll());
//        System.out.println(person);
//        personRepository.save(person);
//        return "redirect:list";
//    }

    @GetMapping("/create")
    @Secured("ROLE_MANAGE_USER")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("user/add-user");
        modelAndView.addObject("authorities", authorityRepository.findAll());
        modelAndView.addObject("person", new Person());
        return modelAndView;
    }

    @PostMapping("/save")
    ModelAndView save(@ModelAttribute @Valid Person person, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()){
            modelAndView.setViewName("user/add-user");
            modelAndView.addObject("person", person);
            return modelAndView;
        }
        personService.savePerson(person);
        return new ModelAndView("redirect:/users");
    }

//    @GetMapping("/addUser")
//    public String addUser(Model model) {
//        model.addAttribute("person", new Person());
//        return "adduser";
//    }


    @GetMapping("delete/{id}")
    @Secured("ROLE_MANAGE_USER")
    public String deleteUser(@PathVariable("id") Long id, Model model) {

        Person user = this.personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id : " + id));

        this.personRepository.delete(user);
        model.addAttribute("users", personRepository.findAll());
        return "redirect:/users";

    }

    @GetMapping("edit/{id}")
    @Secured("ROLE_MANAGE_USER")
    public String showUpdateForm(@PathVariable ("id") Long id, Model model) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student id : " + id));

        model.addAttribute("user", person);
        return "user/update-user";
    }

    @PostMapping("update/{id}")
    public String updateStudent(@PathVariable("id") long id, @Valid Person user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            user.setId(id);
            return "user/update-user";
        }
        personRepository.save(user);
        model.addAttribute("users", personRepository.findAll());
        return "redirect:/users";
    }
}