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

    /*
    @GetMapping("showForm")
    public String showUserForm(Person person){
        return "add-user";
    }
    */
    @GetMapping("/list")
    @Secured("ROLE_USERS_TAB")
    public String users(Model model){
        model.addAttribute("users", this.personRepository.findAll());
        return "user/users";
    }
    /*
    @RequestMapping("/addUser")
    public String addUser(@Valid Person person, BindingResult result, Model model){

        if (result.hasErrors()){
            return "add-user";
        }
        model.addAttribute("authoritiesList", this.authorityRepository.findAll());
        System.out.println(person);
        personRepository.save(person);
        return "redirect:list";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("person", new Person());
        return "adduser";
    }
    */

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
        return new ModelAndView("redirect:/users/list");
    }
    /*
    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users", personRepository.findAll());
        return "layout";
    }
    */
    @GetMapping("delete/{id}")
    @Secured("ROLE_MANAGE_USER")
    public String deleteUser(@PathVariable("id") long id, Model model) {

        Person user = this.personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id : " + id));

        this.personRepository.delete(user);
        model.addAttribute("users", this.personRepository.findAll());
        return "redirect:/users/list";

    }

    @GetMapping("edit/{id}")
    @Secured("ROLE_MANAGE_USER")
    public String showUpdateForm(@PathVariable ("id") long id, Model model) {
        Person person = this.personRepository.findById(id)
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
        model.addAttribute("users", this.personRepository.findAll());
        return "redirect:/users/list";
    }
}