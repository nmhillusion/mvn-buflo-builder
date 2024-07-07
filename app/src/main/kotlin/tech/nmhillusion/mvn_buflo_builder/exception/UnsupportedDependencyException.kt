package tech.nmhillusion.mvn_buflo_builder.exception;

/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-07-07
 */
public class UnsupportedDependencyException extends Exception {

    public UnsupportedDependencyException() {
    }

    public UnsupportedDependencyException(String message) {
        super(message);
    }

    public UnsupportedDependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedDependencyException(Throwable cause) {
        super(cause);
    }

    public UnsupportedDependencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
