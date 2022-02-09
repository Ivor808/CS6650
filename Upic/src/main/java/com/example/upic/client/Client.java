package com.example.upic.client;

import io.swagger.client.ApiClient;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

public class Client {

  Integer DAY_MAX = 420;

  public static void main(String[] args) throws InterruptedException {
    ApiClient client = new ApiClient();
    client.setBasePath("http://localhost:8080");
    Integer numThreads = 64;
    Integer numSkiers = 1024;
    Integer numLifts = 100;
    Integer numRuns = 20;

    if (args.length > 0) {
      numThreads = Integer.valueOf(args[0]);
      if (numThreads > 1024) {
        numThreads = 1024;
      }
      numSkiers = Integer.valueOf(args[1]);
      if (numSkiers > 1000000) {
        numSkiers = 1000000;
      }
      if (args.length > 2) {
        numLifts = Integer.valueOf(args[2]);
        if (numLifts < 5) {
          numLifts = 40;
        } else if (numLifts > 60) {
          numLifts = 40;
        }
      }
      if (args.length > 3) {
        numRuns = Integer.valueOf(args[3]);
        if (numRuns < 0) {
          numRuns = 10;
        } else if (numRuns > 20) {
          numRuns = 10;
        }
      }
      if (args.length > 4) {
        client.setBasePath(args[4]);
      }
    }

    // PHASE 1
    Integer threadsToLaunch = numThreads / 4;
    Integer skiIdsToPass = numSkiers / (threadsToLaunch);
    Long numRequestsToSend = Math.round(numRuns * 0.2 * skiIdsToPass);
    Long phase2Start = Math.round(0.2 * threadsToLaunch);
    System.out.println("Thread to launch: " + threadsToLaunch);
    System.out.println("Ski Ids to pass: " + skiIdsToPass);
    System.out.println("Requests to send per thread: " + numRequestsToSend);
    System.out.println("20% mark: " + phase2Start);
    System.out.println("-----");
    int idStart = 0;
    int startTime = 1;
    int endTime = 90;
    Count count = new Count(0);
    CountDownLatch phase1Latch = new CountDownLatch(phase2Start.intValue());
    for (int i = 0; i < threadsToLaunch; i++) {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
      LocalTime localTime = LocalTime.now();
      System.out.println("num thread " + i + " " + dtf.format(localTime));
      UpicThread thread = new UpicThread(idStart, idStart + skiIdsToPass, startTime, endTime
          , numLifts, numRequestsToSend, client, count, phase1Latch);
      idStart += skiIdsToPass;
      Thread real = new Thread(thread);
      real.start();
    }

    phase1Latch.await();
    System.out.println("Phase 2, BEGIN!");
    Long phase3Start = Math.round(0.2 * numThreads);
    CountDownLatch phase2Latch = new CountDownLatch(phase3Start.intValue());
    for (int i = 0; i < numThreads; i++) {
      UpicThread thread = new UpicThread(idStart, idStart + skiIdsToPass, startTime, endTime
          , numLifts, numRequestsToSend, client, count, phase2Latch);
      idStart += skiIdsToPass;
      Thread real = new Thread(thread);
      real.start();

    }
    phase2Latch.await();
    System.out.println("PHase 3, BEGIN!");

    Long phase3Threads = Math.round(0.1 * numThreads);
    Long phase3NumRequests = Math.round(0.1 * numRuns);
    CountDownLatch phase3Latch = new CountDownLatch(phase3Threads.intValue());

    for (int i = 0; i < phase3Threads; i++) {
      UpicThread thread = new UpicThread(idStart, idStart + skiIdsToPass, startTime, endTime
          , numLifts, numRequestsToSend, client, count, phase3Latch);
      idStart += skiIdsToPass;
      Thread real = new Thread(thread);
      real.start();

    }
    phase3Latch.await();
    System.out.println("done!");

  }
}