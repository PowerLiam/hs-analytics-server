package com.liamnbtech.server.client.blizzard;

public class BlizzardApiClientException extends Exception {
    /**
     * Creates a new BlizzardApiClientException.
     *
     * @param msg The exception message
     */
    public BlizzardApiClientException(String msg) {
        super(msg);
    }

    /**
     * Creates a new BlizzardApiClientException.
     *
     * @param msg The exception message
     * @param cause The Throwable which caused this exception
     */
    public BlizzardApiClientException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
