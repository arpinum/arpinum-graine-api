package yourapp.model;


import yourapp.model.wallet.WalletRepository;

import javax.inject.Inject;

public abstract class Repositories {

    public static WalletRepository wallets() {
        return INSTANCE.getWalletRepository();

    }

    protected abstract WalletRepository getWalletRepository();

    @Inject
    public static void initialize(Repositories instance) {
        INSTANCE = instance;
    }

    private static Repositories INSTANCE;
}
