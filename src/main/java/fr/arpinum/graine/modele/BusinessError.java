package fr.arpinum.graine.modele;

@SuppressWarnings("UnusedDeclaration")
public class BusinessError extends RuntimeException {

    public BusinessError(String code) {
        super(code);
    }
}
