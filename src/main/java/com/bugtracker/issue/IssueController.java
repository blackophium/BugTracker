package com.bugtracker.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.bugtracker.enums.Priority;
import com.bugtracker.enums.Status;
import com.bugtracker.enums.Type;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonRepository;
import com.bugtracker.project.ProjectRepository;
import com.bugtracker.person.PersonService;
import com.bugtracker.mail.*;
import com.bugtracker.utils.MarkdownUtils;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;
    private final PersonService personService;
    private final MailService mailService;
    private final IssueService issueService;
    private final MarkdownUtils markdownUtils;

    @Autowired
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, ProjectRepository projectRepository,
                           PersonService personService, MailService mailService, IssueService issueService, MarkdownUtils markdownUtils) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.projectRepository = projectRepository;
        this.personService = personService;
        this.mailService = mailService;
        this.issueService = issueService;
        this.markdownUtils = markdownUtils;
    }

    @GetMapping
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
    public String save(Issue issue, BindingResult result, Principal principal, Model model) {
        if (result.hasErrors()){
            return "issue/add-issue";
        }
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(issue::setCreator);
        issue.setHtml(markdownUtils.markdownToHTML(issue.getDescription()));
        issueRepository.save(issue);
        return "redirect:/issues";
    }

    @GetMapping("edit/{id}")
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
    public String updateIssue(@PathVariable("id") Long id, BindingResult result) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        if(result.hasErrors()) {
            issue.setId(id);
            return "issue/add-issue";
        }
        issueRepository.save(issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}")
    public String showIssueDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        model.addAttribute("issues", issueRepository.findAll());
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("issue", issue);
        model.addAttribute("creator", issue.getCreator());

        return "issue/details-issue";
    }

    @GetMapping("/delete/{id}")
    public String deleteIssue(@PathVariable("id") Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue id : " + id));
        issueRepository.delete(issue);
        String email = issue.getCreator().getEmail();
        if (!email.isEmpty()){
            String subject = "Zamknięto Twoje zadanie";
            mailService.send(new Mail(email, subject, issueService.initMailContent(issue)));
        }
        return "redirect:/issues";
    }

}