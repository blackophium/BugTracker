package com.bugtracker.issues;

import com.bugtracker.auth.PersonRepository;
import com.bugtracker.project.ProjectRepository;
import com.bugtracker.enums.Status;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/issue")
public class IssueController {

    final IssueRepository issueRepository;
    final ProjectRepository projectRepository;
    final PersonRepository personRepository;

    public IssueController(IssueRepository issueRepository, ProjectRepository projectRepository, PersonRepository personRepository) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.personRepository = personRepository;
    }

    @GetMapping
    ModelAndView index(@ModelAttribute IssueFilter issueFilter) {
        ModelAndView modelAndView = new ModelAndView("issue/index");

        modelAndView.addObject("issues", issueRepository.findAll(issueFilter.buildQuery()));
        modelAndView.addObject("projects", projectRepository.findAll());
        modelAndView.addObject("people", personRepository.findAll());
        modelAndView.addObject("states", Status.values());
        modelAndView.addObject("filter", issueFilter);

        return modelAndView;
    }
}