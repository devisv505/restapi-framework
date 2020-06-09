package restapi.example;

import restapi.core.annotation.InjectByType;
import restapi.core.annotation.Singleton;

@Singleton
public class ConsoleAnnouncer implements Announcer {

  @InjectByType
  private Recommendator recommendator;

  @Override
  public void announce(String message) {
    recommendator.recommend();
    System.out.println(message);
  }
}
