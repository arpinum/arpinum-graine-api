package yourapp.model.wallet;

import arpinum.ddd.evenement.Event;

import java.util.UUID;


public class WalletCreated extends Event<Wallet> {

    @SuppressWarnings("unused")
    protected WalletCreated() {
    }

    public WalletCreated(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
