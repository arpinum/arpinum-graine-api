package fr.arpinum.seed.infrastructure.persistence.memory;

import com.google.common.collect.Sets;
import fr.arpinum.seed.model.Aggregate;
import fr.arpinum.seed.model.Repository;

import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class MemoryRepository<TId, TRoot extends Aggregate<TId>> implements Repository<TId, TRoot> {

    @Override
    public TRoot get(TId tId) {
        return entities.stream().filter(element -> element.getId().equals(tId)).findFirst().orElse(null);
    }

    @Override
    public void add(TRoot root) {
        entities.add(root);
    }

    @Override
    public void delete(TRoot root) {
        entities.remove(root);
    }

    public Set<TRoot> getAll() {
        return entities;
    }

    protected final Set<TRoot> entities = Sets.newHashSet();
}
