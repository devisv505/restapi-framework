package restapi.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

public class DeprecatedHandlerProxyConfigurator implements ProxyConfigurator {

  @Override
  public Object wrapInProxyIfNeeded(Object t, Class implClass) {
    if (implClass.isAnnotationPresent(Deprecated.class)) {
      if (implClass.getInterfaces().length == 0) {
        return Enhancer.create(implClass, (InvocationHandler) (proxy, method, args) -> getInvocationHandler(t, method, args));
      }

      return Proxy.newProxyInstance(
          implClass.getClassLoader(), implClass.getInterfaces(), (proxy, method, args) -> getInvocationHandler(t, method, args)
      );
    } else if (Arrays.stream(implClass.getDeclaredMethods())
        .anyMatch(field -> field.isAnnotationPresent(Deprecated.class))) {

      return Enhancer.create(implClass, (InvocationHandler) (proxy, method, args) -> getInvocationHandlerMethod(t, method, args));
    } else {
      return t;
    }
  }

  private Object getInvocationHandler(Object t, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
    System.out.println("WARNING: class " + t.getClass().getName() + " is deprecated.");
    return method.invoke(t, args);
  }

  private Object getInvocationHandlerMethod(Object t, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
    if (method.isAnnotationPresent(Deprecated.class)) {
      System.out.println("WARNING: Method " + t.getClass().getName() + "#" + method.getName() + " is deprecated.");
    }

    return method.invoke(t, args);
  }

}
