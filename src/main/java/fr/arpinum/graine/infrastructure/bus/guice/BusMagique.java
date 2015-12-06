package fr.arpinum.graine.infrastructure.bus.guice;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public final class BusMagique {

    public static <T> void scanPackageEtBind(String nomPackage, Class<T> type, Binder binder) {
        Reflections reflections = new Reflections(ClasspathHelper.forPackage("fr.arpinum.graine"), ClasspathHelper.forPackage(nomPackage));
        Set<Class<? extends T>> recherches = reflections.getSubTypesOf(type);
        Multibinder<T> rechercheMultibinder = Multibinder.newSetBinder(binder, type);
        recherches.forEach((typeTrouvé) -> {
            if (!Modifier.isAbstract(typeTrouvé.getModifiers()) && typeTrouvé.getCanonicalName().startsWith(nomPackage)) {
                LOGGER.debug("Implémentation trouvée pour {} : {}", type, typeTrouvé);
                rechercheMultibinder.addBinding().to(typeTrouvé);
            }
        });
    }

    private BusMagique() {
    }

    private static Logger LOGGER = LoggerFactory.getLogger(BusMagique.class);
}
