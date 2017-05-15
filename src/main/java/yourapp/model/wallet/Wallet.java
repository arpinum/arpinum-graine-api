package yourapp.model.wallet;


import arpinum.ddd.BaseAggregateWithUuid;
import arpinum.ddd.evenement.EventSourceHandler;

import java.util.UUID;

public class Wallet extends BaseAggregateWithUuid {

    public static Factory factory() {
        return new Factory();
    }

    public static class Factory {
        public Wallet create(String name) {
            Wallet wallet = new Wallet();
            return wallet;
        }
    }

    @EventSourceHandler
    public void replay(WalletCreated event) {
        setId((UUID) event.getTargetId());
    }
}
