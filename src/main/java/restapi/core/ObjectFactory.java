package restapi.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import restapi.core.annotation.PostConstruct;
import restapi.core.annotation.RepositoryRestController;
import restapi.core.configurer.ObjectConfigurator;
import restapi.core.proxy.ProxyConfigurator;

public class ObjectFactory {

  private final ApplicationContext context;
  private List<ObjectConfigurator> configurators = new ArrayList<>();
  private List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

  public ObjectFactory(ApplicationContext context) {
    this.context = context;

    for (Class<? extends ObjectConfigurator> aClass : context.getConfig().getScanner().getSubTypesOf(ObjectConfigurator.class)) {
      configurators.add(createObject(aClass));
    }

    for (Class<? extends ProxyConfigurator> aClass : context.getConfig().getScanner().getSubTypesOf(ProxyConfigurator.class)) {
      proxyConfigurators.add(createObject(aClass));
    }
  }

  public <T> T createObject(Class<T> implClass) {
    T t = create(implClass);
    configure(t);
    invokeInitMethod(implClass, t);

    t = configureProxy(implClass, t);

    return t;
  }

  private <T> T configureProxy(Class<T> implClass, T t) {
    for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
      t = (T) proxyConfigurator.wrapInProxyIfNeeded(t, implClass);
    }
    return t;
  }

  private <T> void invokeInitMethod(Class<T> implClass, T t) {
    for (Method method : implClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(PostConstruct.class)) {
        try {
          method.invoke(t);
        }
        catch (IllegalAccessException e) {
          e.printStackTrace();
        }
        catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private <T> void configure(T t) {
    configurators.forEach(configurator -> configurator.configure(t, context));
  }

  private <T> T create(Class<? extends T> implClass) {
    try {
      return implClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException e) {
      // TODO: set normal message
      throw new RuntimeException("");
    }
    catch (IllegalAccessException e) {
      // TODO: set normal message
      throw new RuntimeException("");
    }
    catch (InvocationTargetException e) {
      // TODO: set normal message
      throw new RuntimeException("");
    }
    catch (NoSuchMethodException e) {
      // TODO: set normal message
      throw new RuntimeException("");
    }
  }
}
