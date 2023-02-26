package com.bugtracker.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.bugtracker.enums.Status;
import com.bugtracker.project.Project;
import com.bugtracker.person.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {
    List<Issue> findAllByProject(Project project);
    Integer countAllByStatusIs(Status status);
    List<Issue> findAllByStatusAndAssignee(Status status, Optional<Person> assignee);
}