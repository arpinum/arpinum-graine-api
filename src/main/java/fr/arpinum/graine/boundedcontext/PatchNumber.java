package fr.arpinum.graine.boundedcontext;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PatchNumber {
    int value();
}
