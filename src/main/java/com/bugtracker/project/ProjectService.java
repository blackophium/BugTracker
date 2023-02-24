package com.bugtracker.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonService;
import com.bugtracker.utils.MarkdownUtils;

import java.security.Principal;
import java.util.Optional;

@Service
public class ProjectService {

    private final PersonService personService;
    private final MarkdownUtils markdownUtils;

    @Autowired
    public ProjectService(PersonService personService, MarkdownUtils markdownUtils) {
        this.personService = personService;
        this.markdownUtils = markdownUtils;
    }


    void addCreatorToProject(Project project, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(project::setCreator);
    }

    void markdownParser(Project project) {
        project.setHtml(markdownUtils.markdownToHTML(project.getDescription()));
    }
}
