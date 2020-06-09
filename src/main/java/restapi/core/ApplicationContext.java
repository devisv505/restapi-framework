package restapi.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import restapi.core.annotation.RepositoryRestController;
import restapi.core.annotation.Singleton;
import restapi.core.config.Config;

public class ApplicationContext {

  private final Map<Class, Object> cache = new ConcurrentHashMap<>();
  private final Config config;
  private ObjectFactory objectFactory;

  public ApplicationContext(Config config) {
    this.config = config;
  }

  public <T> T getObject(Class<T> type) {
    if (cache.containsKey(type)) {
      return (T) cache.get(type);
    }

    Class<? extends T> implClass = type;

    if (implClass.isInterface() && !implClass.isAnnotationPresent(RepositoryRestController.class)) {
      implClass = config.getImplClass(type);
    }

    T t = objectFactory.createObject(implClass);

    if (implClass.isAnnotationPresent(Singleton.class)) {
      cache.put(type, t);
    }

    return t;
  }

  public Config getConfig() {
    return config;
  }

  public void setObjectFactory(ObjectFactory objectFactory) {
    this.objectFactory = objectFactory;
  }
}
