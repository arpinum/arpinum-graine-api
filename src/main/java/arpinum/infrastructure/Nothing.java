package arpinum.infrastructure;


import io.reactivex.Single;

public final class Nothing {

    private Nothing() {
    }

    public static Single<Nothing> single() {
        return Single.just(INSTANCE);
    }

    public static final Nothing INSTANCE = new Nothing();
}
