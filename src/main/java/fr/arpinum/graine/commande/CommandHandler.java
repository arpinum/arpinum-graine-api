package fr.arpinum.graine.commande;


import fr.arpinum.graine.infrastructure.bus.MessageCaptor;

public interface CommandHandler<TCommande extends Command<TReponse>, TReponse> extends MessageCaptor<TCommande, TReponse> {
}
