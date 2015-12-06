package fr.arpinum.graine.modele;

public class NotAllowedException extends BusinessError {

    public NotAllowedException() {
        super("NOT_ALLOWED");
    }
}
