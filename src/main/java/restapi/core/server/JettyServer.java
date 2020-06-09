package restapi.core.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import restapi.core.annotation.InjectProperty;
import restapi.core.annotation.PostConstruct;
import restapi.core.annotation.Singleton;

@Singleton
public class JettyServer {

  @InjectProperty(value = "server.port", defaultValue = "8080")
  private String port;

  private Server server;

  @PostConstruct
  public void init(){
    server = new Server();

    ServerConnector connector = new ServerConnector(server);
    connector.setPort(Integer.valueOf(port));

    server.setConnectors(new Connector[] { connector });
  }

  public void start() {
    try {
      server.start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  void stop() throws Exception {
    server.stop();
  }

}
