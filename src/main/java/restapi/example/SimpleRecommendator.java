package restapi.example;

import restapi.core.annotation.InjectProperty;

public class SimpleRecommendator implements Recommendator {

  @InjectProperty
  private String alcohol;

  @Override
  public void recommend() {
    System.out.println("Drink " + alcohol);
  }

}
