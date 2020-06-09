package restapi.core.configurer;

import java.lang.reflect.Field;
import restapi.core.ApplicationContext;
import restapi.core.annotation.InjectByType;
import restapi.core.ObjectFactory;

public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {

  @Override
  public void configure(Object t, ApplicationContext context) {
    for (Field field : t.getClass().getDeclaredFields()) {
      if (field.getAnnotation(InjectByType.class) != null) {
        field.setAccessible(true);
        try {
          field.set(t, context.getObject(field.getType()));
        } catch (IllegalAccessException e) {
          //TODO
        }
      }
    }
  }
}
