package com.bugtracker.validators;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bugtracker.person.Person;
import com.bugtracker.person.PersonRepository;

import java.util.Optional;

public class UsernameUniquenessValidator implements ConstraintValidator<UniqueUsername, Person> {

    private final PersonRepository personRepository;

    @Autowired
    public UsernameUniquenessValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext ctx) {
        Optional<Person> foundPerson = personRepository.findByUsername(person.getUsername());

        if (foundPerson.isEmpty()) {
            return true;
        }

        boolean usernameIsUnique = person.getId() != null && foundPerson.get().getId().equals(person.getId());

        if (!usernameIsUnique) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        return usernameIsUnique;
    }
}
