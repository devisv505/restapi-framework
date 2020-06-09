package restapi.core;

import restapi.core.config.Config;
import restapi.core.config.JavaConfig;
import restapi.core.server.JettyServer;

public class RestapiApplication {

  public static ApplicationContext run(Class mainClass) {
    Config javaConfig = new JavaConfig(mainClass.getPackageName());
    ApplicationContext context = new ApplicationContext(javaConfig);
    ObjectFactory objectFactory = new ObjectFactory(context);
    context.setObjectFactory(objectFactory);

    JettyServer server = context.getObject(JettyServer.class);
    server.start();

    return context;
  }

}
