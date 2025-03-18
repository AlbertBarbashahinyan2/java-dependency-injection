package org.example.infrastructure.proxywrapper;

import net.sf.cglib.proxy.Enhancer;
import org.example.infrastructure.annotation.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LogAnnotationProxyWrapper implements ProxyWrapper {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T wrap(T obj, Class<T> cls) {
        boolean hasLog = cls.isAnnotationPresent(Log.class);
        Set<String> logMethods = new HashSet<>();
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                logMethods.add(method.getName());
            }
        }
        if (!hasLog && logMethods.isEmpty()) {
            return obj;
        }

        if (cls.getInterfaces().length != 0) {
            return (T) Proxy.newProxyInstance(
                    cls.getClassLoader(),
                    cls.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            return logInvoke(proxy, method, args, hasLog, logMethods, cls, obj);
                        }
                    }
            );
        }

        return (T) Enhancer.create(
                cls,
                new net.sf.cglib.proxy.InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        return logInvoke(o, method, args, hasLog, logMethods, cls, obj);
                    }
                }
        );
    }

    private Object logInvoke(Object proxy, Method method, Object[] args, boolean hasLog,
                             Set<String> logMethods, Class<?> cls, Object obj) throws Throwable{
        if (hasLog || logMethods.contains(method.getName())) {
            System.out.printf(
                    "Calling method: %s.%s. Args: %s\n",
                    cls.getName(), method.getName(), Arrays.toString(args));
        }
        return method.invoke(obj, args);
    }
}
