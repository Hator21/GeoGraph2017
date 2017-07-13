package de.fh_bielefeld.geograph.API.Exception;

@SuppressWarnings("serial")
public class InvalidAPIRequestException extends Exception {

    public InvalidAPIRequestException(String message) {
        super(message);
    }

    public InvalidAPIRequestException(Throwable cause) {
        super(cause);
    }

    public InvalidAPIRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAPIRequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
