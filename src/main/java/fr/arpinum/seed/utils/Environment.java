package fr.arpinum.seed.utils;

import com.google.common.io.Resources;

import java.util.Optional;

public class Environment {

    public static java.net.URL logConfiguration() {
        return Resources.getResource("env/" + get() + "/" + "logback.xml");
    }

    public static String get() {
        return Optional.ofNullable(System.getenv("env")).orElse("dev");
    }

    private Environment() {
    }


}
