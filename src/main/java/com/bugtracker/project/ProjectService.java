package com.bugtracker.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonService;
import com.bugtracker.utils.MarkdownUtils;
import com.bugtracker.project.ProjectRepository;

import java.security.Principal;
import java.util.Optional;

@Service
public class ProjectService {

    private final PersonService personService;
    private final MarkdownUtils markdownUtils;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(PersonService personService, MarkdownUtils markdownUtils, ProjectRepository projectRepository) {
        this.personService = personService;
        this.markdownUtils = markdownUtils;
        this.projectRepository = projectRepository;
    }


    void addCreatorToProject(Project project, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(project::setCreator);
    }

    void markdownParser(Project project) {
        project.setHtml(markdownUtils.markdownToHTML(project.getDescription()));
    }

    public Page<Project> findAll(ProjectFilter projectFilter, Pageable pageable){
   //     try {
     //       Thread.sleep(2000);
       // } catch (InterruptedException e) {
         //   e.printStackTrace();
       // }
        return projectRepository.findAll(projectFilter.buildQuery(), pageable);
    }
}
