package helpers;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import org.threeten.bp.format.DateTimeFormatter;

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
  CountDownLatch phase2End;


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
    phase2End = null;
  }

  //overload to provide optional phase2end latch
  public UpicThread(Integer startSkier, Integer endSkier, Integer startTime,
      Integer endTime,Integer liftId, long numRequestsToSend, ApiClient apiClient, Count count, CountDownLatch latch, CountDownLatch phase2End) {
    this.startSkier = startSkier;
    this.endSkier = endSkier;
    this.startTime = startTime;
    this.endTime = endTime;
    this.liftId = liftId;
    this.numRequestsToSend = numRequestsToSend;
    this.apiClient = apiClient;
    this.count = count;
    this.latch = latch;
    this.phase2End = phase2End;
  }

  @Override
  public void run() {

    SkiersApi skiersApi = new SkiersApi(apiClient);


    for (int i = 0; i < numRequestsToSend; i++) {
      int randSkiId = ThreadLocalRandom.current().nextInt(startSkier,endSkier);
      int randLiftId = ThreadLocalRandom.current().nextInt(1, liftId);
      int randTime = ThreadLocalRandom.current().nextInt(startTime, endTime);
      int randWaitTime = ThreadLocalRandom.current().nextInt(0,10);

      LiftRide liftRide = new LiftRide();
      liftRide.setTime(randTime);
      liftRide.setLiftID(randLiftId);
      liftRide.setWaitTime(randWaitTime);
      boolean tryCall = true;
      int numTries = 0;
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
      LocalTime localTime = LocalTime.now();
      long startTime = System.nanoTime();
      ApiResponse<Void> response = new ApiResponse<>(408,null);
      while (tryCall) {
        numTries++;
        if (numTries == 5) {
          tryCall = false;
          count.addFailure();
          continue;
        }
        try {
          response = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, 1, "1", "1", randSkiId);
          if (response.getStatusCode() == 201 ) {
            count.addSuccess();
          }
          else {
            count.addFailure();
            continue;
          }
          tryCall = false;
        } catch (ApiException e) {

          e.printStackTrace();
        }
      }

      long endTime = System.nanoTime();
      long duration = (endTime - startTime)/1000000;
      count.addToLatency(duration);
      count.addNewRecord(localTime.toString(),"POST",duration,response.getStatusCode());

    }
    latch.countDown();
    if (phase2End != null) {
      phase2End.countDown();
    }

  }
}
