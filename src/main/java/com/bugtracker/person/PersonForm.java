package com.bugtracker.person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bugtracker.auth.Authority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PersonForm {

    Long id;

    @NotBlank
    String username;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    @Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    String email;

    Set<Authority> authorities;

    public PersonForm(Person person) {
        this.id = person.getId();
        this.username = person.getUsername();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.authorities= person.getAuthorities();
    }
}