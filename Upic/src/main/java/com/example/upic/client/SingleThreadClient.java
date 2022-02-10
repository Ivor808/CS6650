package com.example.upic.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class SingleThreadClient {

  public static void main(String[] args) {
    long startTime = System.nanoTime();
    // run time 157112607100
    ApiClient client = new ApiClient();
    client.setBasePath("http://localhost:8080");

    SkiersApi resortsApi = new SkiersApi(client);

    LiftRide liftRide = new LiftRide();
    liftRide.setTime(1);
    liftRide.setLiftID(1);
    liftRide.setWaitTime(1);

    for (int i = 0; i < 10000; i++) {
      try {
        resortsApi.writeNewLiftRide(liftRide,1,"1","1",1);
      } catch (ApiException e) {
        e.printStackTrace();
      }
    }

    long endTime   = System.nanoTime();
    long totalTime = endTime - startTime;
    System.out.println(totalTime);
  }

}
