package yourapp.query.wallet.projection;


import arpinum.ddd.event.EventCaptor;
import arpinum.infrastructure.date.Dates;
import org.jongo.Jongo;
import yourapp.model.wallet.WalletCreated;

import javax.inject.Inject;

public class OnWalletCreatedFillSummary implements EventCaptor<WalletCreated>{

    private Jongo jongo;

    @Inject
    public OnWalletCreatedFillSummary(Jongo jongo) {
        this.jongo = jongo;
    }

    @Override
    public void execute(WalletCreated event) {
        jongo.getCollection("view_wallet")
                .update("{_id:#}",event.getTargetId())
                .upsert()
                .with("{$set:{creationDate:#}}", Dates.fromTimestamp(event.getTimestamp()));
    }
}
