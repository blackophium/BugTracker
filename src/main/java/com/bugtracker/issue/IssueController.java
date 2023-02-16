package com.bugtracker.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.bugtracker.project.ProjectRepository;
import com.bugtracker.person.PersonRepository;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, ProjectRepository projectRepository) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/list")
//    @Secured("ROLE_USERS_TAB")
    public String users(Model model){
        model.addAttribute("issues",issueRepository.findAll());
        return "issue/issues";
    }

    @GetMapping("/create")
    public String showIssueForm(Model model){
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("issue", new Issue());
        return "issue/add-issue";
    }

    @PostMapping("/save")
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