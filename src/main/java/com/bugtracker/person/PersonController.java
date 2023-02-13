package com.bugtracker.person;

import com.bugtracker.auth.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class PersonController {

    private final PersonRepository personRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public PersonController(PersonRepository personRepository, AuthorityRepository authorityRepository) {
        this.personRepository = personRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("showForm")
    public String showUserForm(Person person){
        return "add-user";
    }

    @GetMapping("/list")
    public String users(Model model){
        model.addAttribute("users", this.personRepository.findAll());
        return "users";
    }

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

//    @GetMapping("/addUser")
//    public String addUser(Model model) {
//        model.addAttribute("person", new Person());
//        return "adduser";
//    }

    @GetMapping("/index")
    public String showUserList(Model model) {
//        model.addAttribute("users", personRepository.findAll());
        return "layout";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {

        Person user = this.personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id : " + id));

        this.personRepository.delete(user);
        model.addAttribute("users", this.personRepository.findAll());
        return "redirect:/users/list";

    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable ("id") long id, Model model) {
        Person person = this.personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student id : " + id));

        model.addAttribute("user", person);
        return "update-user";
    }

    @PostMapping("update/{id}")
    public String updateStudent(@PathVariable("id") long id, @Valid Person user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }
        personRepository.save(user);
        model.addAttribute("users", this.personRepository.findAll());
        return "redirect:/users/list";
    }
}