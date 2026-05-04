package com.anthony.blacksmithOnlineStore.validations.item;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class NullOrRangeValidator implements ConstraintValidator<NullOrRange, Number> {
  private BigDecimal min;
  private BigDecimal max;
  private String defaultMessage;

  @Override
  public void initialize(NullOrRange annotation) {
    this.min = BigDecimal.valueOf(annotation.min());
    this.max = BigDecimal.valueOf(annotation.max());
    this.defaultMessage = annotation.message();
  }

  @Override
  public boolean isValid(Number value, ConstraintValidatorContext context) {
    if (value == null) return true;
    BigDecimal val = toBigDecimal(value);
    boolean validMin = val.compareTo(min) >= 0;
    boolean validMax = val.compareTo(max) <= 0;
    if (validMin && validMax) return true;
    boolean useCustomMessage = !"{nullorrange.default}".equals(defaultMessage);
    if (useCustomMessage) {
      return false;
    }
    context.disableDefaultConstraintViolation();
    String message = buildMessage(val, validMin, validMax);
    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    return false;
  }

  private String buildMessage(BigDecimal value, boolean minOk, boolean maxOk) {
    StringBuilder sb = new StringBuilder();
    if (!minOk) sb.append("Value must be at least (").append(min).append(")");
    if (!maxOk) {
      if (!sb.isEmpty()) {
        sb.append(" | ");
      }
      sb.append("Value must be less than (").append(max).append(")");
    }
    return sb.toString();
  }

  private BigDecimal toBigDecimal(Number value) {
    if (value instanceof BigDecimal bd) {
      return bd;
    }
    return BigDecimal.valueOf(value.doubleValue());
  }
}
