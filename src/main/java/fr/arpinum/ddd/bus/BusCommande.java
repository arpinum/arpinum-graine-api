package fr.arpinum.ddd.bus;

import com.google.common.util.concurrent.ListenableFuture;

public interface BusCommande {

    <TReponse> ListenableFuture<ResultatCommande<TReponse>> poste(Commande<TReponse> commande);

}
