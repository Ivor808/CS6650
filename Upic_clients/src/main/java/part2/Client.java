package part2;

import helpers.Count;
import helpers.ResponseRecord;
import helpers.UpicThread;
import io.swagger.client.ApiClient;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class Client {

  Integer DAY_MAX = 420;

  public static void main(String[] args) throws InterruptedException {
    long totalStart = System.nanoTime();
    ApiClient client = new ApiClient();
    client.setBasePath("http://3.83.157.24:8080/Upic_war/");
    Integer numThreads = 256;
    Integer numSkiers = 20000;
    Integer numLifts = 40;
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
    int totalThreadsMade = 0;
    System.out.println("----Run params!----");
    System.out.println("Total threads " + numThreads);
    System.out.println("Num Skiers " + numSkiers);
    System.out.println("Num lift rides " + numLifts);

    System.out.println("----PHASE 1----");
    System.out.println("Phase 1 Threads to launch: " + threadsToLaunch);
    System.out.println("Ski Ids to pass: " + skiIdsToPass);
    System.out.println("Requests to send per thread: " + numRequestsToSend);
    System.out.println("20% mark: " + phase2Start);
    int idStart = 0;
    int startTime = 1;
    int endTime = 90;
    Count count = new Count();
    CountDownLatch phase1Latch = new CountDownLatch(phase2Start.intValue());
    for (int i = 0; i < threadsToLaunch; i++) {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
      LocalTime localTime = LocalTime.now();
      UpicThread thread = new UpicThread(idStart, idStart + skiIdsToPass, startTime, endTime
          , numLifts, numRequestsToSend, client, count, phase1Latch);
      idStart += skiIdsToPass;
      Thread real = new Thread(thread);
      totalThreadsMade ++;
      real.start();
    }

    phase1Latch.await();


    // PHASE 2
    System.out.println("----Phase 2, BEGIN!----");
    Long phase3Start = Math.round(0.2 * numThreads);
    CountDownLatch phase2Latch = new CountDownLatch(phase3Start.intValue());
    int phase2SkierIdRange = numSkiers / numThreads;
    Long phase2NumRequestsToSend = Math.round(numRuns * 0.6 * phase2SkierIdRange);
    startTime = 91;
    endTime = 360;
    CountDownLatch phase2End = new CountDownLatch(numThreads);
    System.out.println("Requests to send: " + phase2NumRequestsToSend);
    System.out.println("Total threads in phase 2 " + numThreads);
    for (int i = 0; i < numThreads; i++) {
      UpicThread thread = new UpicThread(idStart, idStart + phase2SkierIdRange, startTime, endTime
          , numLifts, phase2NumRequestsToSend, client, count, phase2Latch, phase2End);
      idStart += phase2SkierIdRange;
      Thread real = new Thread(thread);
      totalThreadsMade ++;
      real.start();

    }
    phase2Latch.await();


    // PHASE 3
    System.out.println("----Phase 3, BEGIN!----");

    Long phase3Threads = Math.round(0.1 * numThreads);
    Long phase3NumRequests = Math.round(0.1 * numRuns);
    startTime = 361;
    endTime = 420;
    CountDownLatch phase3Latch = new CountDownLatch(phase3Threads.intValue());
    System.out.println("Requests to send: " + phase3NumRequests);
    System.out.println("Total threads in phase 3 " + phase3Threads);
    for (int i = 0; i < phase3Threads; i++) {
      UpicThread thread = new UpicThread(idStart, idStart + skiIdsToPass, startTime, endTime
          , numLifts, phase3NumRequests, client, count, phase3Latch);
      idStart += skiIdsToPass;
      Thread real = new Thread(thread);
      totalThreadsMade ++;
      real.start();

    }
    phase3Latch.await();
    phase2End.await();

    // All phases done
    System.out.println("----Done----");
    System.out.println("Total successful requests sent " + count.getCount());
    System.out.println("Total failures " + count.getFailures());
    long totalEnd = System.nanoTime();
    double totalTime = totalEnd - totalStart;
    totalTime = totalTime / 1_000_000_000;
    System.out.println("Wall time (seconds): " + totalTime);
    System.out.println("Throughput " + (count.getFailures() + count.getCount()) / totalTime);
    System.out.println(
        "Mean response time " + count.getLatency().stream().mapToDouble(a -> a).average().getAsDouble());
    System.out.println("Min response time (ms) " + Collections.min(count.getLatency()));
    System.out.println("Max response time (ms) " + Collections.max(count.getLatency()));
    Collections.sort(count.getLatency());
    System.out.println("Median response time " + count.getLatency().get(count.getLatency().size()/2));
    System.out.println("P99 latency (ms) " + count.getLatency().get((int) (count.getLatency().size() * 0.99)));
    System.out.println("Total threads made " + totalThreadsMade);
    System.out.println("--------");
    System.out.println("Generating csv...");

    try {
      FileWriter file = new FileWriter("client_times.csv");
      PrintWriter write = new PrintWriter(file);
      ArrayList<ResponseRecord> test = count.getRecords();
      write.println("Start_time, Request_Type, Latency, Response_Code");
      for (ResponseRecord record : test) {
        write.println(record);
      }
      write.close();
    } catch (IOException exe) {
      System.out.println("Cannot create file");
    }

  }
}