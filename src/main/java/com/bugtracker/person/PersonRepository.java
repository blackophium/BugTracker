package com.bugtracker.person;

import com.bugtracker.enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    Optional<Person> findByUsername(String username);
    Optional<Person> findByUsernameAndEnabled(String username, Boolean enabled);

    Iterable<Person> findAllByIdAndEnabled(Long id, Boolean enabled);
    Iterable<Person> findAllByEnabled(Boolean enabled);

    @Query("select p from Person p where p.dateCreated >= :date order by p.dateCreated desc")
    Iterable<Person> findEnabledUsersCreatedAfter(@Param("date") Date date);

    List<Person> findAllByRole(Role role);
}
