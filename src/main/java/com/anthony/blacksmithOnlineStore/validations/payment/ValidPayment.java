package com.anthony.blacksmithOnlineStore.validations.payment;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPaymentValidator.class)
public @interface ValidPayment {

  String message() default "Invalid payment";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
