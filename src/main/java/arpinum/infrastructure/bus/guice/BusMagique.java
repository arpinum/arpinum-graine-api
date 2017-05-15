package arpinum.infrastructure.bus.guice;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;

@SuppressWarnings("UnusedDeclaration")
public final class BusMagique {

    public static <T> void scanPackageAndBind(String packageName, Class<T> type, Binder binder) {
        Multibinder<T> multibinder = Multibinder.newSetBinder(binder, type);
        new FastClasspathScanner(packageName, type.getPackage().getName())
                .matchClassesImplementing(type, foundType -> {
                    if (!Modifier.isAbstract(foundType.getModifiers()) && foundType.getCanonicalName().startsWith(packageName)) {
                        LOGGER.debug("Implementation found for {} : {}", type, foundType);
                        multibinder.addBinding().to(foundType);
                    }
                }).scan();
    }


    private BusMagique() {
    }

    private static Logger LOGGER = LoggerFactory.getLogger(arpinum.infrastructure.bus.guice.BusMagique.class);
}
