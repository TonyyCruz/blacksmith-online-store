package com.anthony.blacksmithOnlineStore.validations.item;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrRangeValidator.class)
@Documented
public @interface NullOrRange {
  String message() default "{nullorrange.default}";

  double min() default Double.NEGATIVE_INFINITY;
  double max() default Double.POSITIVE_INFINITY;

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
