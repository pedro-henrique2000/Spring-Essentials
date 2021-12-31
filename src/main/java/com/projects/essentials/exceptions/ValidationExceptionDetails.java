package com.projects.essentials.exceptions;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ValidationExceptionDetails extends ExceptionDetails{
    private String fields;
    private String fieldMessage;
}
