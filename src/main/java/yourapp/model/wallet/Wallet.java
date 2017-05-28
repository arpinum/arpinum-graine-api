package yourapp.model.wallet;


import arpinum.ddd.BaseAggregateWithUuid;
import arpinum.ddd.event.EventSourceHandler;
import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.UUID;

public class Wallet extends BaseAggregateWithUuid {

    public static Factory factory() {
        return new Factory();
    }

    public static class Factory {
        public Tuple2<Wallet, WalletCreated> create(String name) {
            Wallet wallet = new Wallet();
            return Tuple.of(wallet, new WalletCreated(wallet.getId(), name));
        }
    }

    @EventSourceHandler
    public void replay(WalletCreated event) {
        setId((UUID) event.getTargetId());
    }
}
