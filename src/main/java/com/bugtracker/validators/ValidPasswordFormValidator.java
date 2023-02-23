package com.bugtracker.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bugtracker.person.PasswordForm;

public class ValidPasswordFormValidator implements ConstraintValidator<ValidPasswordForm, PasswordForm> {

    @Override
    public void initialize(ValidPasswordForm constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PasswordForm passwordForm, ConstraintValidatorContext ctx) {
        if (passwordForm.getPassword() == null || passwordForm.getPassword().equals("")) {
            if (passwordForm.getId() == null) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("password")
                        .addConstraintViolation();

                return false;
            }

            return true;
        }

        boolean passwordsAreValid = passwordForm.getPassword().equals(passwordForm.getRepeatedPassword());

        if (passwordsAreValid) {
            return true;
        } else {

            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("repeatedPassword")
                    .addConstraintViolation();

            return false;
        }
    }
}