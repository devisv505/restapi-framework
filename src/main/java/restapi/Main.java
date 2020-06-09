package restapi;

import restapi.core.ApplicationContext;
import restapi.core.RestapiApplication;
import restapi.example.CoronaDesinfector;
import restapi.example.Room;

public class Main {

  public static void main(String[] args) {
    ApplicationContext context = RestapiApplication.run(Main.class);
    CoronaDesinfector coronaDesinfector = context.getObject(CoronaDesinfector.class);
    coronaDesinfector.start(new Room());
  }

}
