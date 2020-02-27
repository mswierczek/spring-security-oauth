package com.baeldung.multitenancy;

public class MultitenancyException extends RuntimeException {

    private static final long serialVersionUID = -5984940903820981214L;

    public MultitenancyException(String message) {
        super(message);
    }

}
