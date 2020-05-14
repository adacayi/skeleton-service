package uk.co.sancode.skeleton_service.service;

public class AuthenticationException extends Exception {
    public AuthenticationException(final String message, final Exception e) {
        super(message, e);
    }
}
