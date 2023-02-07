package com.bugtracker.issues;

import com.bugtracker.auth.Person;
import com.bugtracker.enums.Status;
import com.bugtracker.project.Project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor

public class Issue {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Status state = Status.TODO;

    @ManyToOne()
    @JoinColumn(name = "assignee_id")
    Person assignee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;
}