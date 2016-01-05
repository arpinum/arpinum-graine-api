package yourapplication.model;

import javax.inject.Inject;

public abstract class RepositoriesLocator {

    public static void initialise(RepositoriesLocator instance) {
        RepositoriesLocator.instance = instance;
    }

    @Inject
    private static RepositoriesLocator instance;
}
