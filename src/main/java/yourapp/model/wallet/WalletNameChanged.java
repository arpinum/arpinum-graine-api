package yourapp.model.wallet;


import arpinum.ddd.event.Event;

import java.util.UUID;

public class WalletNameChanged extends Event<Wallet> {

    @SuppressWarnings("unused")
    protected WalletNameChanged() {
    }

    public WalletNameChanged(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
