package com.bugtracker.issue;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonService;
import com.bugtracker.utils.MarkdownUtils;
import com.bugtracker.mail.Mail;
import com.bugtracker.mail.MailService;
import com.bugtracker.project.Project;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IssueService {

    private final PersonService personService;
    private final MarkdownUtils markdownUtils;
    private final MailService mailService;
    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(PersonService personService, MarkdownUtils markdownUtils, MailService mailService, IssueRepository issueRepository) {
        this.personService = personService;
        this.markdownUtils = markdownUtils;
        this.mailService = mailService;
        this.issueRepository = issueRepository;
    }

    public String initMailContent(Issue issue) {
        String dateCreated = issue.getDateCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String dateDelete = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String content = "Twoje zgłoszenie " + "'"+ issue.getName() + "'" + " (dot. projektu: " + issue.getProject().getName() + "), utworzone w dniu: " + dateCreated + " zostało usunięte w dniu: " + dateDelete;
        return content;
    }

    // pagination
    public Page<Issue> findAll(IssueFilter issueFilter, Pageable pageable){
        return issueRepository.findAll(issueFilter.buildQuery(), pageable);
    }

    void addCreatorToIssue(Issue issue, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(issue::setCreator);
    }

    void markdownParser(Issue issue) {
        issue.setHtml(markdownUtils.markdownToHTML(issue.getDescription()));
    }

    void sendEmailAboutDeleteIssue(Issue issue, String emailAddress) {
        if (!emailAddress.isEmpty()) {
            String subject = "Usunięto Twoje zadanie";
            mailService.send(new Mail(emailAddress, subject, initMailContent(issue)));
            log.info("We send an email about closed issue to: " + emailAddress);
            log.debug("Send an email about closed issue to: {}", emailAddress);
        } else {
            log.info("We didn't send an email about closed issue. We couldn't find the email address of the user with the given login: " + issue.getCreator().getUsername());
            log.debug("Didn't send an email about closed issue user with the given login: {}", issue.getCreator().getUsername());
        }
    }
}