package fr.arpinum.graine.commande;


import fr.arpinum.graine.infrastructure.bus.Message;

public interface Command<TReponse> extends Message<TReponse> {
}
