package org.example.infrastructure.configreader;

import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Scope;
import org.example.infrastructure.enums.ScopeType;
import org.example.infrastructure.exception.ComponentNotFoundException;
import org.reflections.Reflections;

import java.util.*;

public class JavaObjectConfigReader implements ObjectConfigReader {

    private Reflections reflections;

    public JavaObjectConfigReader(String packageToScan) {

        this.reflections = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> cls) {
        if (!cls.isInterface()) {
            if (!cls.isAnnotationPresent(Component.class)) {
                throw new ComponentNotFoundException("Component " +
                        cls.getName() + " is not annotated with @Component");
            }
            return cls;
        }

        Set<Class<? extends T>> subTypesOf =
                reflections.getSubTypesOf(cls);

        subTypesOf.removeIf(c -> !c.isAnnotationPresent(Component.class));

        if (subTypesOf.isEmpty()) {
            throw new ComponentNotFoundException("The interface " + cls.getName() +
                    "does not have any @Component annotated implementations");
        }
        if (subTypesOf.size() != 1) {
            throw new RuntimeException("Interface should have only one implementation");
        }

        return subTypesOf.iterator().next();
    }

    @Override
    public <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls) {

        return reflections.getSubTypesOf(cls);
    }

    @Override
    public <T> boolean isSingleton(Class<T> cls) {
        return !cls.isAnnotationPresent(Scope.class)
                || cls.getAnnotation(Scope.class).value() == ScopeType.SINGLETON;
    }
}
