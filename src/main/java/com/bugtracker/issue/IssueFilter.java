package com.bugtracker.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import com.bugtracker.enums.Priority;
import com.bugtracker.enums.Status;
import com.bugtracker.enums.Type;
import com.bugtracker.person.Person;
import com.bugtracker.project.Project;

@Getter
@Setter
@NoArgsConstructor
public class IssueFilter {

    Project project;
    Type type;
    Status status;
    Priority priority;
    Person assignee;
    String name;

    private Specification<Issue> hasProject() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("project"), project);
    }

    private Specification<Issue> hasType() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("type"), type);
    }

    private Specification<Issue> hasStatus() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("status"), status);
    }

    private Specification<Issue> hasPriority() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("priority"), priority);
    }

    private Specification<Issue> hasAssignee() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("assignee"), assignee);
    }

    private Specification<Issue> hasName(){
        return (issueRoot, query, builder) -> builder.like(builder.lower(issueRoot.get("name")), "%" + name.toLowerCase() + "%");
    }


    public Specification<Issue> buildQuery(){
        Specification<Issue> specification = Specification.where(null);

        if (project != null) {
            specification = specification.and(hasProject());
        }

        if (type != null) {
            specification = specification.and(hasType());
        }

        if (status != null) {
            specification = specification.and(hasStatus());
        }

        if (priority != null) {
            specification = specification.and(hasPriority());
        }

        if (assignee != null) {
            specification = specification.and(hasAssignee());
        }

        if (name != null) {
            specification = specification.and(hasName());
        }

        return specification;
    }

}
