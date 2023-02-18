package com.bugtracker.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.bugtracker.enums.Priority;
import com.bugtracker.enums.Status;
import com.bugtracker.enums.Type;
import com.bugtracker.person.Person;
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

    @GetMapping("edit/{id}")
    @Secured("ROLE_MANAGE_USER")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        model.addAttribute("issue", issue);
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        return "issue/add-issue";
    }

    @PostMapping("update/{id}")
    public String updateIssue(@PathVariable("id") long id, BindingResult result, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        if(result.hasErrors()) {
            issue.setId(id);
            return "issue/add-issue";
        }
        issueRepository.save(issue);
        //model.addAttribute("issues", issueRepository.findAll());
        //model.addAttribute("issue", issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USERS_TAB")
    public String showIssueDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        model.addAttribute("issues", issueRepository.findAll());
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("issue", issue);
        return "issue/details-issue";
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_MANAGE_USER")
    public String deleteIssue(@PathVariable("id") Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue id : " + id));

        issueRepository.delete(issue);
        //model.addAttribute("issues", issueRepository.findAll());
        return "redirect:/issues";

    }
}