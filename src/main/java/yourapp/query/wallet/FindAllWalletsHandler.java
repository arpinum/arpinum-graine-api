package yourapp.query.wallet;


import arpinum.query.QueryHandlerJongo;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import yourapp.query.wallet.model.WalletSummary;

public class FindAllWalletsHandler extends QueryHandlerJongo<FindAllWallets, Seq<WalletSummary>> {

    @Override
    protected Seq<WalletSummary> execute(FindAllWallets findAllWallets, Jongo jongo) {
        MongoCursor<WalletSummary> view_wallet = jongo.getCollection("view_wallet")
                .find().as(WalletSummary.class);
        return Stream.ofAll(view_wallet);
    }
}
