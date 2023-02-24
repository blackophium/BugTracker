package com.bugtracker.validators;

import com.bugtracker.issue.Issue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IssueMandatoryAssigneeValidator implements ConstraintValidator<IssueMandatoryAssignee, Issue> {

    @Override
    public void initialize(IssueMandatoryAssignee constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Issue issue, ConstraintValidatorContext ctx) {
        if (issue.getAssignee() == null){
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("assignee")
                    .addConstraintViolation();
            return false;
        }
        else{
            return true;
        }
    }
}
