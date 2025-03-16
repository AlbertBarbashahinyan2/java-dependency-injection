package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Env;
import java.lang.reflect.Field;

public class EnvAnnotationObjectConfigurator implements ObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Env.class)) {
                field.setAccessible(true);
                if (!field.getAnnotation(Env.class).value().isEmpty()) {
                    field.set(obj, System.getenv(field.getAnnotation(Env.class).value()));
                }else
                    field.set(obj, System.getenv(field.getName()));
            }
        }
    }
}
