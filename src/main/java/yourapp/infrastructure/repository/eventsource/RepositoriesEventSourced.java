package yourapp.infrastructure.repository.eventsource;


import arpinum.ddd.evenement.EventStore;
import arpinum.infrastructure.persistance.eventsourcing.UnitOfWork;
import yourapp.model.Repositories;
import yourapp.model.wallet.WalletRepository;

import javax.inject.Inject;

public class RepositoriesEventSourced extends Repositories {

    @Inject
    public RepositoriesEventSourced(EventStore eventStore, UnitOfWork unitOfWork) {
        walletRepositoryEventSourced = new WalletRepositoryEventSourced(eventStore, unitOfWork);
    }

    @Override
    protected WalletRepository getWalletRepository() {
        return walletRepositoryEventSourced;
    }

    private final WalletRepositoryEventSourced walletRepositoryEventSourced;
}
