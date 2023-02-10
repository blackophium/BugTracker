package com.bugtracker;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugtracker.auth.Authority;
import com.bugtracker.auth.AuthorityRepository;
import com.bugtracker.enums.AuthorityName;
import com.bugtracker.auth.PersonService;


@Service
public class Bootstrap implements InitializingBean {

    private final AuthorityRepository authorityRepository;
    private final PersonService personService;

    @Autowired
    public Bootstrap(AuthorityRepository authorityRepository, PersonService personService) {
        this.authorityRepository = authorityRepository;
        this.personService = personService;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Rozpoczynamy przygotowywanie aplikacji...");
        prepareAuthorities();
        personService.prepareAdminUser();
    }

    private void prepareAuthorities() {
        for (AuthorityName authorityName : AuthorityName.values()) {
            Authority existingAuthority = authorityRepository.findByName(authorityName);
            if (existingAuthority == null) {
                Authority authority = new Authority(authorityName);

                authorityRepository.save(authority);
                System.out.println("Zapisano nowe uprawienie: " + authorityName.name());
            }
            System.out.println("Uprawnienie " +  authorityName.name() + " już istnieje.");
        }
    }
}
