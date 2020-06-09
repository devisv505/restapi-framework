package restapi.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeprecatedHandlerProxyConfigurator implements ProxyConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger("DeprecatedHandlerProxy");

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
    LOGGER.warn("WARNING: class " + t.getClass().getName() + " is deprecated.");
    return method.invoke(t, args);
  }

  private Object getInvocationHandlerMethod(Object t, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
    if (method.isAnnotationPresent(Deprecated.class)) {
      LOGGER.warn("WARNING: Method " + t.getClass().getName() + "#" + method.getName() + " is deprecated.");
    }

    return method.invoke(t, args);
  }

}
