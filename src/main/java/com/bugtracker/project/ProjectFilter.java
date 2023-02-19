package com.bugtracker.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import com.bugtracker.person.Person;

@Getter
@Setter
@NoArgsConstructor
public class ProjectFilter {

    Person creator;
    String name;
    String description;

    private Specification<Project> hasCreator() {
        return (projectRoot, query, builder) -> builder.equal(projectRoot.get("creator"), creator);
    }

    private Specification<Project> hasName(){
        return (projectRoot, query, builder) -> builder.like(builder.lower(projectRoot.get("name")), "%" + name.toLowerCase() + "%");
    }

    private Specification<Project> hasDescription(){
        return (projectRoot, query, builder) -> builder.like(builder.lower(projectRoot.get("description")), "%" + description.toLowerCase() + "%");
    }

    public Specification<Project> buildQuery() {
        Specification<Project> specification = Specification.where(null);

        if (creator != null) {
            specification = specification.and(hasCreator());
        }

        if (name != null) {
            specification = specification.and(hasName());
        }

        if (description != null) {
            specification = specification.and(hasDescription());
        }

        return specification;
    }

}