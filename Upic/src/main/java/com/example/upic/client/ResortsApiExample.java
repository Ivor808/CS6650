package com.example.upic.client;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class ResortsApiExample {

  public static void main(String[] args) {
    ApiClient client = new ApiClient();
    client.setBasePath("http://localhost:8080");
    SkiersApi apiInstance = new SkiersApi(client);
    try {
      LiftRide liftRide = new LiftRide();
      liftRide.setLiftID(1);
      liftRide.setTime(15);
      apiInstance.writeNewLiftRide(liftRide,1,"1","1",1);
      System.out.println("new season created");
    } catch (ApiException e) {
      System.err.println("Exception when calling ResortsApi#getResorts");
      e.printStackTrace();
    }
  }
}