package yourapp.infrastructure.repository.eventsource;

import arpinum.ddd.event.EventStore;
import arpinum.infrastructure.persistance.eventsourcing.EventSourcedRepositoryWithUuid;
import yourapp.model.wallet.Wallet;
import yourapp.model.wallet.WalletRepository;


public class WalletRepositoryEventSourced extends EventSourcedRepositoryWithUuid<Wallet> implements WalletRepository {

    public WalletRepositoryEventSourced(EventStore eventStore) {
        super(eventStore);
    }
}
