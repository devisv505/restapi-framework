package restapi.core;

import restapi.core.config.Config;
import restapi.core.config.JavaConfig;

public class RestApiApplication {

  public static ApplicationContext run(Class mainClass) {
    Config javaConfig = new JavaConfig(mainClass.getPackageName());
    ApplicationContext context = new ApplicationContext(javaConfig);
    ObjectFactory objectFactory = new ObjectFactory(context);

    context.setObjectFactory(objectFactory);

    return context;
  }

}
