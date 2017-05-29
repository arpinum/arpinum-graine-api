package arpinum.infrastructure.bus;


public class CaptorNotFound extends Throwable {

    public CaptorNotFound(Class<?> aClass) {
        super(String.format("CAPTOR_NOT_FOUND - %s", aClass));
    }
}
