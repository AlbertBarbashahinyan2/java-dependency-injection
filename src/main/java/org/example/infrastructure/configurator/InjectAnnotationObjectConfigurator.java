package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Inject;
import org.example.infrastructure.annotation.Qualifier;
import org.example.infrastructure.exception.QualifierNotFoundException;
import java.lang.reflect.Field;

public class InjectAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                if(field.isAnnotationPresent(Qualifier.class)) {
                    Class<?> qualifierClass = field.getAnnotation(Qualifier.class).value();
                    if(!field.getType().isAssignableFrom(qualifierClass)) {
                        throw new QualifierNotFoundException("The Qualifier " + qualifierClass.getName() +
                                " is indicated but is not found among the implementations of " + field.getType().getName());
                    }
                    field.set(obj, context.getObject(qualifierClass));
                }else
                    field.set(obj, context.getObject(field.getType()));
            }
        }
    }
}