package com.anthony.blacksmithOnlineStore.controller.docs.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiBadRequestDoc
@ApiForbiddenDoc
@ApiNotFoundDoc
@ApiUnauthorizedDoc
public @interface DefaultApiResponses {
}
