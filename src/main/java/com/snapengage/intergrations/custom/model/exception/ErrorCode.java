package com.snapengage.intergrations.custom.model.exception;

public enum ErrorCode {
    VALIDATION_ERROR, /* User input data is not valid (but user can fix it an retry)*/
    UNKNOWN_ERROR, /* This will be returned when an exception that is not known and handled. */
}
