package com.anthony.blacksmithOnlineStore.validations.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {
  private int min;
  private int max;

  @Override
  public void initialize(Age constraintAnnotation) {
    this.min = constraintAnnotation.min();
    this.max = constraintAnnotation.max();
  }

  @Override
  public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
    if (birthDate == null) return false;
    int idade = Period.between(birthDate, LocalDate.now()).getYears();
    return idade >= min && idade <= max;
  }
}

