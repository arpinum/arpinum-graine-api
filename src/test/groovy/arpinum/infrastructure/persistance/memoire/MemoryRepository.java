package arpinum.infrastructure.persistance.memoire;

import com.google.common.collect.Sets;
import arpinum.ddd.AggregateRoot;
import arpinum.ddd.Repository;

import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class MemoryRepository<TId, TRacine extends AggregateRoot<TId>> implements Repository<TId, TRacine> {

    @Override
    public TRacine get(TId tId) {
        return entities.stream().filter(element -> element.getId().equals(tId)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean exists(TId tId) {
        return entities.stream().filter(e -> e.getId().equals(tId)).count() > 0;
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
