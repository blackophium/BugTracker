package com.bugtracker.validators;

import com.bugtracker.issue.Issue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IssueMandatoryTitleValidator implements ConstraintValidator<IssueMandatoryTitle, Issue> {
    @Override
    public void initialize(IssueMandatoryTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Issue issue, ConstraintValidatorContext ctx) {
        if (issue.getName().isEmpty()) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("name")
                    .addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }
}