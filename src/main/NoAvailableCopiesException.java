package main;

public class NoAvailableCopiesException extends RuntimeException{
    public NoAvailableCopiesException(String message) {
        super(message);
    }
}
