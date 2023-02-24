package com.bugtracker.issue;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonService;
import com.bugtracker.project.Project;
import com.bugtracker.utils.MarkdownUtils;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class IssueService {

    private final PersonService personService;
    private final MarkdownUtils markdownUtils;

    @Autowired
    public IssueService(PersonService personService, MarkdownUtils markdownUtils) {
        this.personService = personService;
        this.markdownUtils = markdownUtils;
    }

    public String initMailContent(Issue issue){
        String dateCreated = issue.getDateCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String dateClosed = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String content = "Twoje zgłoszenie, utworzone w dniu: " + dateCreated + " zostało zamknięte w dniu: " + dateClosed;
        return content;
    }

    void addCreatorToIssue(Issue issue, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(issue::setCreator);
    }

    void markdownParser(Issue issue) {
        issue.setHtml(markdownUtils.markdownToHTML(issue.getDescription()));
    }
}