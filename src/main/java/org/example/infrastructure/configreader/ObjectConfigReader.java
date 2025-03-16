package org.example.infrastructure.configreader;

import java.util.Collection;

public interface ObjectConfigReader {

    <T> Class<? extends T> getImplClass(Class<T> cls);

    <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls);

    <T> boolean isSingleton(Class<T> cls);

    <T> void storeComponents(Class<T> cls);
}
