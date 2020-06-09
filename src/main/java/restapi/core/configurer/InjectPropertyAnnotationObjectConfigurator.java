package restapi.core.configurer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import restapi.core.ApplicationContext;
import restapi.core.annotation.InjectProperty;
import static org.reflections.util.Utils.isEmpty;

public class InjectPropertyAnnotationObjectConfigurator implements ObjectConfigurator {

  private final Map<String, String> propertiesMap = new HashMap<>();

  public InjectPropertyAnnotationObjectConfigurator() {
    try {
      String path = ClassLoader.getSystemClassLoader().getResource("application.properties").getPath();
      Stream<String> lines = new BufferedReader(new FileReader(path)).lines();

      propertiesMap.putAll(
          lines.map(line -> line.split("=")).collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]))
      );
    } catch (FileNotFoundException e) {
      // ignore
    }
  }

  @Override
  public void configure(Object t, ApplicationContext context) {
    Class<?> implClass = t.getClass();

    try {
      for (Field field : implClass.getDeclaredFields()) {
        InjectProperty annotation = field.getAnnotation(InjectProperty.class);

        if (annotation != null) {
          String value = annotation.value().isEmpty()
              ? propertiesMap.get(field.getName())
              : propertiesMap.get(annotation.value());

          if (isEmpty(value)) {
            value = annotation.defaultValue();
          }

          field.setAccessible(true);
          field.set(t, value);
        }
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
