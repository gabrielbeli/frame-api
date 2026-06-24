package com.frame.api.common.response;

public record ApiFieldError(
        String field,
        String message
) {
}
