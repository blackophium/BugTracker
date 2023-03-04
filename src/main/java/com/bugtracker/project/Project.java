package com.bugtracker.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bugtracker.issue.Issue;
import com.bugtracker.person.Person;
import com.bugtracker.validators.UniqueProjectName;
import com.bugtracker.validators.ProjectMandatoryTitle;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@UniqueProjectName
@ProjectMandatoryTitle
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "project")
    private Set<Issue> issues;
    private Boolean enabled = true;

    @Column(nullable = false)
    private final Date dateCreated = new Date();
    private String code;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @Column(columnDefinition = "text")
    private String html;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", issues=" + issues +
                ", enabled=" + enabled +
                ", dateCreated=" + dateCreated +
                ", description='" + description + '\'' +
                ", html='" + html + '\'' +
                '}';
    }

}