package org.example.infrastructure.proxywrapper;

import net.sf.cglib.proxy.Enhancer;
import org.example.infrastructure.annotation.CacheKey;
import org.example.infrastructure.annotation.Cacheable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class CacheableAnnotationProxyWrapper implements ProxyWrapper {

    private Map<Object, Object> returnCache = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T wrap(T obj, Class<T> cls) {
        Map<String, Annotation[][]> annotatedMethods = new HashMap<>();
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Cacheable.class)) {
                annotatedMethods.put(method.getName(), method.getParameterAnnotations());
            }
        }
        if (annotatedMethods.isEmpty()) {
            return obj;
        }
        if (cls.getInterfaces().length != 0) {
            return (T) Proxy.newProxyInstance(
                    cls.getClassLoader(),
                    cls.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (annotatedMethods.containsKey(method.getName())) { // to stop at the method annotated with Cacheable, which for sure exists at this point
                                Annotation[][] annotations = annotatedMethods.get(method.getName()); // get the annotations of parameters of that method
                                for (int i = 0; i < annotations.length; i++) {
                                    for (int j = 0; j < annotations[i].length; j++) {
                                        if (annotations[i][j] instanceof CacheKey) { // we find the index (i) of the argument annotated with CacheKey
                                            if (returnCache.containsKey(args[i])) { // if the i-th argument is cached already
                                                System.out.printf("returning %s from cache: %s\n", args[i], returnCache.get(args[i])); // print for understanding reasons
                                                return returnCache.get(args[i]); // then return the value associated with that argument
                                            }
                                            if (method.invoke(obj, args) != null) // here the argument is not cached and if value is not null
                                                returnCache.put(args[i], method.invoke(obj, args)); // then we cache it with the value
                                        }
                                    }
                                }
                            }
                            return method.invoke(obj, args);
                        }
                    }
            );
        }

        return (T) Enhancer.create(
                cls,
                new net.sf.cglib.proxy.InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        if (annotatedMethods.containsKey(method.getName())) {
                            Annotation[][] annotations = annotatedMethods.get(method.getName());
                            for (int i = 0; i < annotations.length; i++) {
                                for (int j = 0; j < annotations[i].length; j++) {
                                    if (annotations[i][j] instanceof CacheKey) {
                                        if (returnCache.containsKey(args[i])) {
                                            System.out.printf("returning %s from cache: %s\n", args[i], returnCache.get(args[i]));
                                            return returnCache.get(args[i]);
                                        }
                                        if (method.invoke(obj, args) != null)
                                            returnCache.put(args[i], method.invoke(obj, args));
                                    }
                                }
                            }
                        }
                        return method.invoke(obj, args);
                    }
                }
        );
    }
}
