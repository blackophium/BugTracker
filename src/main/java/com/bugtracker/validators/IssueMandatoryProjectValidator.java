package com.bugtracker.validators;

import com.bugtracker.issue.Issue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IssueMandatoryProjectValidator implements ConstraintValidator<IssueMandatoryProject, Issue> {

    @Override
    public void initialize(IssueMandatoryProject constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Issue issue, ConstraintValidatorContext ctx) {
        if (issue.getProject() == null){
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("project")
                    .addConstraintViolation();
            return false;
        }
        else {
            return true;
        }
    }
}