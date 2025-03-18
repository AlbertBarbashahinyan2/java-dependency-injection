package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Property;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class PropertyAnnotationConfigurator implements ObjectConfigurator {

    private Properties properties = new Properties();

    private void propertiesReader () {
        String filePath = "src/main/resources/application.properties";
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
    }

    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        propertiesReader();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Property.class)) {
                String propertyName = field.getAnnotation(Property.class).value();
                field.setAccessible(true);
                if (!propertyName.isEmpty()) {
                    field.set(obj, properties.getProperty(propertyName));
                }else
                    field.set(obj, properties.getProperty(field.getName()));
            }
        }
    }
}