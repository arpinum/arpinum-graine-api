package arpinum.infrastructure.bus.guice;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public final class BusMagique {

    public static <T> void scanPackageAndBind(String packageName, Class<T> type, Binder binder) {
        Multibinder<T> multibinder = Multibinder.newSetBinder(binder, type);
        subtypesOf(packageName, type).forEach((foundTypes) -> {
            if (!Modifier.isAbstract(foundTypes.getModifiers()) && foundTypes.getCanonicalName().startsWith(packageName)) {
                LOGGER.debug("Implementation found for {} : {}", type, foundTypes);
                multibinder.addBinding().to(foundTypes);
            }
        });
    }

    public static <T> Set<Class<? extends T>> subtypesOf(String packageName, Class<T> type) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .filterInputsBy(new FilterBuilder().includePackage(packageName))
                        .setUrls(ClasspathHelper.forClass(BusMagique.class))
                        .setScanners(new SubTypesScanner()));
        return reflections.getSubTypesOf(type);
    }

    private BusMagique() {
    }

    private static Logger LOGGER = LoggerFactory.getLogger(BusMagique.class);
}
