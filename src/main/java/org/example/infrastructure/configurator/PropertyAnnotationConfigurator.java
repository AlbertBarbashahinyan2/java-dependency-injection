package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Property;

import java.lang.reflect.Field;

public class PropertyAnnotationConfigurator implements ObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Property.class)) {
                field.setAccessible(true);
                if (!field.getAnnotation(Property.class).value().isEmpty()) {
                    field.set(obj, field.getAnnotation(Property.class).value());
                }else
                    field.set(obj, field.getName());
            }
        }
    }
}
