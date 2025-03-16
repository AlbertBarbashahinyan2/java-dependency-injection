package org.example.infrastructure.postconstruct;

import lombok.SneakyThrows;
import org.example.infrastructure.annotation.PostConstruct;

import java.lang.reflect.Method;

public class PostConstructAnnotationHandler {
    @SneakyThrows
    public <T> void postConstructor(Class<T> cls, T obj) {
        Method[] methods =  cls.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                method.invoke(obj); // methods marked with @PostConstruct cannot have arguments
            }
        }
    }
}
