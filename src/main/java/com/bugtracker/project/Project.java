package com.bugtracker.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bugtracker.issue.Issue;
import com.bugtracker.person.Person;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "project")
    private Set<Issue> issues;
    private Boolean enabled;
    @Column(nullable = false)
    private final Date dateCreated = new Date();
    private String code; // short name? relacja?
    private String description;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

}