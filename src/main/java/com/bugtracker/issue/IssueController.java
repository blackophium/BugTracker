package com.bugtracker.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.bugtracker.enums.Priority;
import com.bugtracker.enums.Status;
import com.bugtracker.enums.Type;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonRepository;
import com.bugtracker.project.ProjectRepository;
import com.bugtracker.person.PersonService;
import com.bugtracker.mail.*;
import com.bugtracker.utils.MarkdownUtils;
import com.bugtracker.security.SecurityService;

import java.security.Principal;
import java.util.Optional;
import javax.validation.Valid;

@Slf4j
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
    private final SecurityService securityService;

    @Autowired
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, ProjectRepository projectRepository,
                           PersonService personService, MailService mailService, IssueService issueService, MarkdownUtils markdownUtils,
                           SecurityService securityService) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.projectRepository = projectRepository;
        this.personService = personService;
        this.mailService = mailService;
        this.issueService = issueService;
        this.markdownUtils = markdownUtils;
        this.securityService = securityService;
    }

    @GetMapping
    public String issues(@ModelAttribute IssueFilter issueFilter, Model model) {
        model.addAttribute("issues", issueRepository.findAll(issueFilter.buildQuery()));
        model.addAttribute("assignedPerson", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("filter", issueFilter);
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        log.debug("Getting issues list: {}", model);
        return "issue/issues";
    }

    @GetMapping("/create")
    public String showIssueForm(Model model) {
        model.addAttribute("persons", personRepository.findAllByEnabled(true));
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("issue", new Issue());

        log.debug("Getting issue create form: {}", model);
        return "issue/add-issue";
    }

    @PostMapping("/save")
    public String saveIssue(Issue issue, BindingResult result, Principal principal, Model model) {
        String usernameLoggedPerson = securityService.getLoggedUser();

        if (result.hasErrors()){
            model.addAttribute("persons", personRepository.findAllByEnabled(true));
            model.addAttribute("projects", projectRepository.findAll());
            log.error("There was a problem. The issue: " + issue + " was not saved.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            log.debug("Model create new issue: {}", model);
            return "issue/add-issue";
        }
        issueService.addCreatorToIssue(issue, principal);
        issueService.markdownParser(issue);
        issueRepository.save(issue);

        log.info("Created new issue: " + issue.getName() + " by: " + usernameLoggedPerson);
        log.debug("Create new issue: {}", issue);
        log.debug("Model create new issue: {}", model);
        return "redirect:/issues";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        model.addAttribute("issue", issue);
        model.addAttribute("persons", personRepository.findAllByEnabled(true));
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());

        log.debug("Getting issue update form: {}", model);
        log.debug("Issue to update: {}", issue);
        return "issue/add-issue";
    }

    @PostMapping("update/{id}")
    public String updateIssue(@PathVariable("id") Long id, BindingResult result) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        String usernameLoggedPerson = securityService.getLoggedUser();
        if(result.hasErrors()) {
            issue.setId(id);
            log.error("There was a problem. The issue: " + issue + " was not update.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            return "issue/add-issue";
        }
        issueRepository.save(issue);

        log.info("Updated issue: " + issue.getName() + " by: " + usernameLoggedPerson);
        log.debug("Updated issue: {}", issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}")
    public String showIssueDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find issue of id: " + id));
        model.addAttribute("person", issue.getAssignee());
        model.addAttribute("project", issue.getProject());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("issue", issue);
        model.addAttribute("creator", issue.getCreator());

        log.debug("Getting issue details: {}", model);
        return "issue/details-issue";
    }

    @GetMapping("/delete/{id}")
    public String deleteIssue(@PathVariable("id") Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue id : " + id));
        String emailAddress = issue.getCreator().getEmail();
        String usernameLoggedPerson = securityService.getLoggedUser();

        log.info("Deleted " + issue + " by " + usernameLoggedPerson);
        log.debug("Deleted issue: {}", issue);

        issueRepository.delete(issue);
        issueService.sendEmailAboutDeleteIssue(issue, emailAddress);
        return "redirect:/issues";
    }

}