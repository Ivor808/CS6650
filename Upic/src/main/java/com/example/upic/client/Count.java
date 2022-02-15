package com.example.upic.client;

import java.util.ArrayList;

public class Count {
  Integer count;
  Integer failures;
  ArrayList<ResponseRecord> records = new ArrayList<>();
  ArrayList<Long> latency = new ArrayList<>();

  public Count() {
    this.count = 0;
    this.failures = 0;
  }

  synchronized void addSuccess() {
    count ++;
  }
  synchronized void addFailure() {failures ++; }
  synchronized void addNewRecord(String startTime, String requestType, double latency, int responseCode) {
    records.add(new ResponseRecord(startTime, requestType, latency, responseCode));
  }
  synchronized void addToLatency(Long time) {
    latency.add(time);
  }
}
