package com.bugtracker.person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bugtracker.validators.ValidPasswordForm;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@ValidPasswordForm
public class PasswordForm {

    Long id;

    @Size(min = 8, max = 35)
    String password;

    String repeatedPassword;


    public PasswordForm(Person person) {
        this.id = person.getId();
        this.password = person.getPassword();
    }
}