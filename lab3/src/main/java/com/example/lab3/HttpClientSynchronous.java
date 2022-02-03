package com.example.lab3;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

public class HttpClientSynchronous {
  final static private int NUMTHREADS = 1000;


  private static final HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  public static void main(String[] args) throws IOException, InterruptedException {
    CountDownLatch  completed = new CountDownLatch(NUMTHREADS);
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalTime begin = LocalTime.now();
    System.out.println(dtf.format(begin));

    final HttpClientSynchronous counter = new HttpClientSynchronous();

    for (int i = 0; i < NUMTHREADS; i++) {
      Runnable thread = () -> {
        try {
          pushRequest(); completed.countDown();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      };
      new Thread(thread).start();
    }
    completed.await();
    LocalTime end = LocalTime.now();
    System.out.println(dtf.format(end));


  }

  private static void pushRequest() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create("http://18.233.157.10:8080/lab3_war"))
        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // print response headers
    HttpHeaders headers = response.headers();
    //headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

    // print status code
    //System.out.println(response.statusCode());

    // print response body
    //System.out.println(response.body());
  }

}
