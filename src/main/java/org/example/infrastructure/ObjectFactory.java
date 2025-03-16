package org.example.infrastructure;

import lombok.SneakyThrows;
import org.example.infrastructure.configreader.ObjectConfigReader;
import org.example.infrastructure.configurator.ObjectConfigurator;
import org.example.infrastructure.postconstruct.PostConstructAnnotationHandler;
import org.example.infrastructure.proxywrapper.ProxyWrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObjectFactory {

    private ApplicationContext applicationContext;
    private List<ObjectConfigurator> objectConfigurators = new ArrayList<>();
    private List<ProxyWrapper> proxyWrappers = new ArrayList<>();
    private PostConstructAnnotationHandler postConstructAnnotationHandler = new PostConstructAnnotationHandler();

    @SneakyThrows
    public ObjectFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.populateObjectConfigurators();
        this.populateProxyWrappers(applicationContext);
    }

    private void populateObjectConfigurators() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Collection<Class<? extends ObjectConfigurator>> implClasses =
                applicationContext.getObjectConfigReader().getImplClasses(ObjectConfigurator.class);
        for (Class<? extends ObjectConfigurator> implClass : implClasses) {
            ObjectConfigurator objectConfigurator = implClass.getDeclaredConstructor().newInstance();

            this.objectConfigurators.add(objectConfigurator);
        }
    }

    private void populateProxyWrappers(ApplicationContext applicationContext) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ObjectConfigReader objectConfigReader = applicationContext.getObjectConfigReader();
        for (Class<? extends ProxyWrapper> implClass : objectConfigReader.getImplClasses(ProxyWrapper.class)) {
            proxyWrappers.add(implClass.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> cls) {
        T obj = cls.getDeclaredConstructor().newInstance();

        for (ObjectConfigurator objectConfigurator : objectConfigurators) {
            objectConfigurator.configure(obj, applicationContext);
        }

        postConstructAnnotationHandler.postConstructor(cls, obj);

        for (ProxyWrapper proxyWrapper : proxyWrappers) {
            obj = proxyWrapper.wrap(obj, cls);
        }

        return obj;
    }
}
