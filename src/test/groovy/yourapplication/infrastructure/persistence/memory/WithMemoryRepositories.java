package yourapplication.infrastructure.persistence.memory;

import org.junit.rules.ExternalResource;
import yourapplication.model.RepositoriesLocator;

public class WithMemoryRepositories extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        RepositoriesLocator.initialise(new MemoryRepositoriesLocator());
    }

    @Override
    protected void after() {
        RepositoriesLocator.initialise(null);
    }
}
