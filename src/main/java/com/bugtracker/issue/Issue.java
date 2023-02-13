package com.bugtracker.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.bugtracker.comment.Comment;
import com.bugtracker.enums.Priority;
import com.bugtracker.enums.Status;
import com.bugtracker.enums.Type;
import com.bugtracker.person.Person;
import com.bugtracker.project.Project;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Issue {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private Priority priority;
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false, unique = true, length = 120)
    private String name;
    private String description;
    @Column(unique = true, length = 20)
    private String code;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @OneToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;
    @OneToOne //ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private Person assignee;
    @Column(nullable = false)
    private final LocalDate dateCreated = LocalDate.now();
    private LocalDate lastUpdate;
    @OneToMany(mappedBy = "issue")
    private List<Comment> comments;

}