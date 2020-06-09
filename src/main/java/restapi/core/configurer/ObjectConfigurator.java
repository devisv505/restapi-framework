package restapi.core.configurer;

import restapi.core.ApplicationContext;

public interface ObjectConfigurator {

  void configure(Object t, ApplicationContext context);

}
