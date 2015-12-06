package fr.arpinum.graine.boundedcontext;

import com.google.inject.*;
import org.jongo.*;
import org.reflections.*;
import org.reflections.util.*;
import org.slf4j.*;

import java.util.*;

public class Patcher {

    public Patcher(Injector injector, String name) {
        this.injector = injector;
        this.name = name;
    }

    public void run(String packageToScan) {
        Reflections reflections = new Reflections(ClasspathHelper.forPackage(packageToScan));
        final Set<Class<?>> patches = reflections.getTypesAnnotatedWith(PatchNumber.class);
        patches.stream()
                .filter(c -> c.getPackage().getName().startsWith(packageToScan))
                .map(c -> {
                    final PatchNumber annotation = c.getAnnotation(PatchNumber.class);
                    return new PatchDescription(annotation.value(), (Class<? extends Patch>) c);
                }).sorted((o1, o2) -> Integer.compare(o1.number, o2.number))
                .forEach(this::runIfNeeded);
    }

    private void runIfNeeded(PatchDescription patchDescription) {
        final Jongo jongo = injector.getInstance(Jongo.class);
        if (jongo.getCollection("patch_" + name).count("{_id:#}", patchDescription.number) == 0) {
            LOGGER.info("running {} ", patchDescription.clazz.getName());
            try {
                runPatch(patchDescription, jongo);
            } catch (Exception e) {
                LOGGER.error("Fail patch", e);
                return;
            }
        }
    }

    private void runPatch(PatchDescription patchDescription, Jongo jongo) {
        final Patch patch = injector.getInstance(patchDescription.clazz);
        patch.run();
        jongo.getCollection("patch_" + name).insert("{_id:#}", patchDescription.number);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Patcher.class);
    private final Injector injector;
    private final String name;

    private class PatchDescription {

        public PatchDescription(int number, Class<? extends Patch> clazz) {
            this.number = number;
            this.clazz = clazz;
        }

        int number;
        Class<? extends Patch> clazz;
    }
}
