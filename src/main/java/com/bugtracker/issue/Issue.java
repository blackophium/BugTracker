package com.bugtracker.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bugtracker.comment.Comment;
import com.bugtracker.enums.Priority;
import com.bugtracker.enums.Status;
import com.bugtracker.enums.Type;
import com.bugtracker.person.Person;
import com.bugtracker.project.Project;
import com.bugtracker.validators.IssueMandatoryProject;
import com.bugtracker.validators.IssueMandatoryAssignee;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@IssueMandatoryProject
@IssueMandatoryAssignee

public class Issue {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false, length = 120)
    private String name;
    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private Person assignee;

    @Column(nullable = false)
    private final LocalDate dateCreated = LocalDate.now();

    @OneToMany(mappedBy = "issue")
    private List<Comment> comments;

    @Column(columnDefinition = "text")
    private String html;

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", status=" + status +
                ", priority=" + priority +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }

}