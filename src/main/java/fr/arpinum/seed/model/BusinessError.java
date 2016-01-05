package fr.arpinum.seed.model;

@SuppressWarnings("UnusedDeclaration")
public class BusinessError extends RuntimeException {

    public BusinessError(String code) {
        super(code);
    }
}
