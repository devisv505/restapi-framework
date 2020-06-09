package restapi.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class JavaConfig implements Config {

  private final Reflections reflections;
  private final Map<Class, Class> ifcToImpClass = new HashMap<>();

  public JavaConfig(String packageToScan) {
    this.reflections = new Reflections(packageToScan);
  }

  @Override
  public <T> Class<? extends T> getImplClass(Class<T> ifc) {
    return ifcToImpClass.computeIfAbsent(ifc, aClass -> {
      Set<Class<? extends T>> classes = reflections.getSubTypesOf(ifc);

      if (classes.size() != 1) {
        throw new RuntimeException(ifc + " has zero or more then one impl");
      }

      return classes.iterator().next();
    });
  }

  @Override
  public Reflections getScanner() {
    return reflections;
  }

}
