package fr.arpinum.seed.model;

public class NotAllowedException extends BusinessError {

    public NotAllowedException() {
        super("NOT_ALLOWED");
    }
}
