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
import com.bugtracker.enums.Priority;
import com.bugtracker.enums.Status;
import com.bugtracker.enums.Type;
import com.bugtracker.person.PersonRepository;
import com.bugtracker.project.ProjectRepository;

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

    @GetMapping
//    @Secured("ROLE_USERS_TAB")
    public String users(@ModelAttribute IssueFilter issueFilter, Model model) {
        model.addAttribute("issues", issueRepository.findAll(issueFilter.buildQuery()));
        model.addAttribute("assignedPerson", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("filter", issueFilter);
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());

        return "issue/issues";
    }

    @GetMapping("/create")
    public String showIssueForm(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("issue", new Issue());
        return "issue/add-issue";
    }

    @PostMapping("/save")
    ModelAndView save(@ModelAttribute Issue issue) {
        System.out.println(issue);
        issueRepository.save(issue);

        return new ModelAndView("redirect:/issues");
    }
//    @GetMapping("/issue")
//    ModelAndView create() {
//        ModelAndView modelAndView = new ModelAndView("issue/add-issue");
//        modelAndView.addObject("persons", personRepository.findAll());
//        modelAndView.addObject("issue", new Issue());
//        return modelAndView;
//    }
}