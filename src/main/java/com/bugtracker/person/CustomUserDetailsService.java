package com.bugtracker.person;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.bugtracker.auth.Authority;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if(person.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> authorities = getUserAuthorities(person);
        return new User(person.get().getUsername(), person.get().getPassword(), authorities);
    }

    private List<GrantedAuthority> getUserAuthorities(Optional<Person> person) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Authority authority : person.get().authorities){
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority().name()));
        }
        return new ArrayList<>(grantedAuthorities);
    }
}