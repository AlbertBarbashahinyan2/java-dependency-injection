package org.example.infrastructure.postconstruct;

import lombok.SneakyThrows;
import org.example.infrastructure.annotation.PostConstruct;
import org.example.infrastructure.exception.InvalidPostConstructException;

import java.lang.reflect.Method;

public class PostConstructAnnotationHandler {
    @SneakyThrows
    public <T> void postConstructor(Class<T> cls, T obj) {
        Method[] methods =  cls.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                if (method.getParameterCount() != 0) {
                    throw new InvalidPostConstructException("The method " + cls.getName()
                            + "." + method.getName() +
                            " is marked with @PostConstruct but has arguments");
                }
                method.setAccessible(true);
                method.invoke(obj); // methods marked with @PostConstruct cannot have arguments
            }
        }
    }
}
