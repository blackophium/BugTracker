package com.bugtracker.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IssueMandatoryAssigneeValidator.class)

public @interface IssueMandatoryAssignee {

    String message() default "{assigneeField.mandatory.error}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}