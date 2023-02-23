package com.bugtracker.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.bugtracker.person.*;
import com.bugtracker.issue.Issue;
import com.bugtracker.issue.IssueRepository;
import com.bugtracker.utils.MarkdownUtils;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final IssueRepository issueRepository;
    private final MarkdownUtils markdownUtils;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, PersonService personService, PersonRepository personRepository,
                             IssueRepository issueRepository, MarkdownUtils markdownUtils) {
        this.projectRepository = projectRepository;
        this.personService = personService;
        this.personRepository = personRepository;
        this.issueRepository = issueRepository;
        this.markdownUtils = markdownUtils;
    }

    @GetMapping()
    public String projects(@ModelAttribute ProjectFilter projectFilter, Model model){
        model.addAttribute("projects", projectRepository.findAll(projectFilter.buildQuery()));
        model.addAttribute("creator", personRepository.findAll());
        model.addAttribute("filter", projectFilter);
        return "project/projects";
    }

    @GetMapping("/create")
    @Secured("ROLE_MANAGE_PROJECTS")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project/add-project";
    }

    @PostMapping("/save")
    public String save(@Valid Project project, BindingResult result, Principal principal, Model model){
        if (result.hasErrors()){
            return "project/add-project";
        }
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(project::setCreator);
        project.setHtml(markdownUtils.markdownToHTML(project.getDescription()));
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("edit/{id}")
    @Secured("ROLE_MANAGE_PROJECTS")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Project project =  projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        model.addAttribute("project", project);
        return "project/add-project";
    }

    @PostMapping("update/{id}")
    public String updateProject(@PathVariable("id") Long id, BindingResult result, @Valid Project project){
        // Project project = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        if (result.hasErrors()){
            project.setId(id);
            return "project/add-project";
        }
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String showProjectDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        model.addAttribute("creator", project.getCreator());
        model.addAttribute("project", project);
        return "project/details-project";
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_MANAGE_PROJECTS")
    public String deleteIssue(@PathVariable("id") Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        List<Issue> allIssuesByProject = issueRepository.findAllByProject(project);
        issueRepository.deleteAll(allIssuesByProject);
        projectRepository.delete(project);
        return "redirect:/projects";
    }
}