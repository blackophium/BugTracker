package com.bugtracker.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.bugtracker.person.Person;
import com.bugtracker.person.PersonRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);

        System.out.println("Znaleziony użytkownik: " + person);

        if (person == null) {
            throw new UsernameNotFoundException(username);
        }

        return buildUserDetails(person);
    }

    private UserDetails buildUserDetails(Optional<Person> person) {
        List<GrantedAuthority> authorities = getUserAuthorities(person);
        return new User(person.get().getUsername(), person.get().getPassword(), authorities);
    }

    private List<GrantedAuthority> getUserAuthorities(Optional<Person> person) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Authority authority : person.get().getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.authority.toString()));
        }
        return new ArrayList<>(grantedAuthorities);
    }
}