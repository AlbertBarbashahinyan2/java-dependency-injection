package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Inject;
import org.example.infrastructure.annotation.Qualifier;
import org.example.infrastructure.exception.QualifierNotFoundException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

public class InjectAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Set<Class<?>> subTypesOf;
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Reflections reflections = new Reflections(obj.getClass().getPackage().getName());
                if(field.isAnnotationPresent(Qualifier.class)) {
                    subTypesOf = (Set<Class<?>>) reflections.getSubTypesOf(field.getType());
                    Class<?> qualifierClass = field.getAnnotation(Qualifier.class).value();
                    if(!subTypesOf.contains(qualifierClass)) {
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