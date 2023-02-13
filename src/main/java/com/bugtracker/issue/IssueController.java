package com.bugtracker.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonRepository;

@Controller
public class IssueController {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;

    @Autowired
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("/issue/create")
    public String showIssueForm(Model model){
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("issue", new Issue());
        return "issue/add-issue";
    }

    @PostMapping("/issue/save")
    ModelAndView save(@ModelAttribute Issue issue) {
        System.out.println(issue);
        issueRepository.save(issue);

        return new ModelAndView("redirect:/list");
    }
//    @GetMapping("/issue")
//    ModelAndView create() {
//        ModelAndView modelAndView = new ModelAndView("issue/add-issue");
//        modelAndView.addObject("persons", personRepository.findAll());
//        modelAndView.addObject("issue", new Issue());
//        return modelAndView;
//    }
}