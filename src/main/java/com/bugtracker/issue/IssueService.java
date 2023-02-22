package com.bugtracker.issue;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class IssueService {

    public String initMailContent(Issue issue){
        String dateCreated = issue.getDateCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String dateClosed = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String content = "Twoje zgłoszenie, utworzone w dniu: " + dateCreated + " zostało zamknięte w dniu: " + dateClosed;
        return content;
    }
}
