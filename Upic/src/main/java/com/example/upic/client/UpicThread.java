package com.example.upic.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class UpicThread implements Runnable {

  private Integer startSkier;
  private Integer endSkier;
  private Integer liftId;
  private Integer startTime;
  private Integer endTime;
  private long numRequestsToSend;
  private ApiClient apiClient;
  Count count;
  CountDownLatch latch;


  public UpicThread(Integer startSkier, Integer endSkier, Integer startTime,
      Integer endTime,Integer liftId, long numRequestsToSend, ApiClient apiClient, Count count, CountDownLatch latch) {
    this.startSkier = startSkier;
    this.endSkier = endSkier;
    this.startTime = startTime;
    this.endTime = endTime;
    this.liftId = liftId;
    this.numRequestsToSend = numRequestsToSend;
    this.apiClient = apiClient;
    this.count = count;
    this.latch = latch;

  }

  @Override
  public void run() {

    SkiersApi resortsApi = new SkiersApi(apiClient);

    for (int i = 0; i < numRequestsToSend; i++) {
      int randSkiId = ThreadLocalRandom.current().nextInt(startSkier,endSkier);
      int randLiftId = ThreadLocalRandom.current().nextInt(0, liftId);
      int randTime = ThreadLocalRandom.current().nextInt(startTime, endTime);
      int randWaitTime = ThreadLocalRandom.current().nextInt(0,10);
      LiftRide liftRide = new LiftRide();
      liftRide.setTime(randTime);
      liftRide.setLiftID(randLiftId);
      liftRide.setWaitTime(randWaitTime);
      boolean tryCall = true;
      int numTries = 0;
      long startTime = System.nanoTime();
      while (tryCall) {
        numTries++;
        if (numTries == 5) {
          tryCall = false;
          count.addFailure();
          continue;
        }
        try {
          long threadId = Thread.currentThread().getId();
          //System.out.println("Posting! " + threadId);
          resortsApi.writeNewLiftRide(liftRide, 1, "1", "1", randSkiId);
          //System.out.println("Complete! " + threadId);
          count.addSuccess();
          tryCall = false;
        } catch (ApiException e) {

          e.printStackTrace();
        }
      }
      long endTime = System.nanoTime();
      long duration = (endTime - startTime)/1000000;
      count.addToLatency(duration);

    }
    latch.countDown();

  }
}
