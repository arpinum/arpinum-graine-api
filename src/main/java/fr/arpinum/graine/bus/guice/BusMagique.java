package fr.arpinum.graine.bus.guice;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public final class BusMagique {

    private BusMagique() {
    }

    public static <T> void scanPackageEtBind(String nomPackage, Class<T> type, Binder binder) {
        Reflections reflections = new Reflections(nomPackage);
        Set<Class<? extends T>> recherches = reflections.getSubTypesOf(type);
        Multibinder<T> rechercheMultibinder = Multibinder.newSetBinder(binder, type);
        recherches.forEach((typeTrouvé) -> {
            if (!Modifier.isAbstract(typeTrouvé.getModifiers())) {
                rechercheMultibinder.addBinding().to(typeTrouvé);
            }
        });
    }
}
