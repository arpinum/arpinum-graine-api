package yourapp.infrastructure.repository.eventsource;


import arpinum.ddd.evenement.EventStore;
import yourapp.model.Repositories;
import yourapp.model.wallet.WalletRepository;

import javax.inject.Inject;

public class RepositoriesEventSourced extends Repositories {

    @Inject
    public RepositoriesEventSourced(EventStore eventStore) {
        walletRepositoryEventSourced = new WalletRepositoryEventSourced(eventStore);
    }

    @Override
    protected WalletRepository getWalletRepository() {
        return walletRepositoryEventSourced;
    }

    private final WalletRepositoryEventSourced walletRepositoryEventSourced;
}
