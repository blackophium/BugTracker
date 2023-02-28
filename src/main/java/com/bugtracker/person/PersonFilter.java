package com.bugtracker.person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import com.bugtracker.enums.Role;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class PersonFilter implements Serializable {

    String username;
    String firstName;
    String lastName;
    String email;
    Role role;

    private Specification<Person> hasUsername(){
        return (personRoot, query, builder) -> builder.like(builder.lower(personRoot.get("username")), "%" + username.toLowerCase() + "%");
    }

    private Specification<Person> hasFirstName(){
        return (personRoot, query, builder) -> builder.like(builder.lower(personRoot.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    private Specification<Person> hasLastName(){
        return (personRoot, query, builder) -> builder.like(builder.lower(personRoot.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    private Specification<Person> hasEmail(){
        return (personRoot, query, builder) -> builder.like(builder.lower(personRoot.get("email")), "%" + email.toLowerCase() + "%");
    }

    private Specification<Person> hasRole(){
        return (personRoot, query, builder) -> builder.equal(personRoot.get("role"), role);
    }

    public Specification<Person> buildQuery(){
        Specification<Person> specification = Specification.where(null);

        if (username != null) {
            specification = specification.and(hasUsername());
        }

        if (firstName != null) {
            specification = specification.and(hasFirstName());
        }


        if (lastName != null) {
            specification = specification.and(hasLastName());
        }

        if (email != null) {
            specification = specification.and(hasEmail());
        }

        if (role != null) {
            specification = specification.and(hasRole());
        }

        return specification;
    }

    public String toQueryString(Integer page){
        return "page=" + page +
                (username != null ? "&username=" + username : "") +
                (firstName != null ? "&firstName=" + firstName : "") +
                (lastName != null ? "&lastName=" + lastName : "") +
                (email != null ? "&email=" + email : "") +
                (role != null ? "&role=" + role : "");
    }
}
