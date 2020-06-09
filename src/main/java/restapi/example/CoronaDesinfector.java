package restapi.example;

import restapi.core.annotation.InjectByType;
import restapi.core.annotation.PostConstruct;

public class CoronaDesinfector {

  @InjectByType
  private Announcer announcer;

  @InjectByType
  private Policeman policeman;

  @PostConstruct
  public void init() {
    System.out.println("post construct");
  }

  public void start(Room room) {
    announcer.announce("start");
    policeman.makePeopleLiveRoom();
    desinfect(room);
    announcer.announce("finish");
  }

  private void desinfect(Room room) {
    System.out.println("processing...");
  };

}
