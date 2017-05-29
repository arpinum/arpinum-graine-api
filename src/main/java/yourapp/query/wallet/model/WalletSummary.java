package yourapp.query.wallet.model;


import org.jongo.marshall.jackson.oid.MongoId;

import java.util.UUID;

public class WalletSummary {

    @MongoId
    public UUID id;

    public String name;
}
