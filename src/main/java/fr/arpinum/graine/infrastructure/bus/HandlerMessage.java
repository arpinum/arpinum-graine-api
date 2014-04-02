package fr.arpinum.graine.infrastructure.bus;


public interface HandlerMessage<TCommande extends Message<TReponse>, TReponse> {

    TReponse execute(TCommande commande);

    Class<TCommande> typeCommande();


}
