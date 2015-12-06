package fr.arpinum.graine.infrastructure.persistance.memoire;

import com.google.common.collect.Sets;
import fr.arpinum.graine.modele.Aggregate;
import fr.arpinum.graine.modele.Repository;

import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class MemoryRepository<TId, TRacine extends Aggregate<TId>> implements Repository<TId, TRacine> {

    @Override
    public TRacine get(TId tId) {
        return entities.stream().filter(element -> element.getId().equals(tId)).findFirst().orElse(null);
    }

    @Override
    public void add(TRacine racine) {
        entities.add(racine);
    }

    @Override
    public void delete(TRacine racine) {
        entities.remove(racine);
    }

    public Set<TRacine> getAll() {
        return entities;
    }

    protected final Set<TRacine> entities = Sets.newHashSet();
}
