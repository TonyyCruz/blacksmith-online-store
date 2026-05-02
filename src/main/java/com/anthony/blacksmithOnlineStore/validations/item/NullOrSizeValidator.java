package com.anthony.blacksmithOnlineStore.validations.item;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrSizeValidator implements ConstraintValidator<NullOrSize, String> {

  private int min;
  private int max;

  @Override
  public void initialize(NullOrSize constraintAnnotation) {
    this.min = constraintAnnotation.min();
    this.max = constraintAnnotation.max();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    int length = value.length();
    return length >= min && length <= max;
  }
}
