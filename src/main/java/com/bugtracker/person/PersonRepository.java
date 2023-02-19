package com.bugtracker.person;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByUsername(String username);
    Optional<Person> findByUsernameAndEnabled(String username, Boolean enabled);

    @Query("select p from Person p where p.dateCreated >= :date order by p.dateCreated desc")
    Iterable<Person> findEnabledUsersCreatedAfter(@Param("date") Date date);
}
