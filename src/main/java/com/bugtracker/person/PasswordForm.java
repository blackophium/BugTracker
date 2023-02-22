package com.bugtracker.person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bugtracker.validators.ValidPasswordForm;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
//@ValidPasswordForm
public class PasswordForm {

    Long id;

    @NotBlank
    String password;

    @NotBlank
    String repeatedPassword;


    public PasswordForm(Person person) {
        this.id = person.getId();
        this.password = person.getPassword();
    }
}