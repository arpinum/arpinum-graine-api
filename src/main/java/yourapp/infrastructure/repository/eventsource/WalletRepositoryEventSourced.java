package yourapp.infrastructure.repository.eventsource;

import arpinum.ddd.evenement.EventStore;
import arpinum.infrastructure.persistance.eventsourcing.EventSourcedRepositoryWithUuid;
import arpinum.infrastructure.persistance.eventsourcing.UnitOfWork;
import yourapp.model.wallet.Wallet;
import yourapp.model.wallet.WalletRepository;


public class WalletRepositoryEventSourced extends EventSourcedRepositoryWithUuid<Wallet> implements WalletRepository {

    public WalletRepositoryEventSourced(EventStore eventStore, UnitOfWork unitOfWork) {
        super(unitOfWork, eventStore);
    }
}
