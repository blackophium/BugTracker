package com.bugtracker.auth;

import com.bugtracker.enums.AuthorityName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    @Query("select a from Person p join p.authorities a where p.username = :username order by a.authority")
    Iterable<Authority> findAllByPersonUsername(String username);

    Optional<Authority> findByAuthority(String authority);

    Authority findByAuthority(AuthorityName name);
}