package fr.arpinum.seed.infrastructure.bus.guice;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public final class MagicalBus {

    public static <T> void scanPackageAndBind(String packageName, Class<T> type, Binder binder) {
        Reflections reflections = new Reflections(ClasspathHelper.forPackage("fr.arpinum.seed"), ClasspathHelper.forPackage(packageName));
        Set<Class<? extends T>> searchs = reflections.getSubTypesOf(type);
        Multibinder<T> searchMultibinder = Multibinder.newSetBinder(binder, type);
        searchs.forEach((typeFound) -> {
            if (!Modifier.isAbstract(typeFound.getModifiers()) && typeFound.getCanonicalName().startsWith(packageName)) {
                LOGGER.debug("Implementation found for {} : {}", type, typeFound);
                searchMultibinder.addBinding().to(typeFound);
            }
        });
    }

    private MagicalBus() {
    }

    private static Logger LOGGER = LoggerFactory.getLogger(MagicalBus.class);
}
