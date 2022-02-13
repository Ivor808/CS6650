package com.example.upic.client;

import java.util.ArrayList;

public class Count {
  Integer count;
  Integer failures;
  ArrayList<Long> latency = new ArrayList<>();

  public Count() {
    this.count = 0;
    this.failures = 0;
  }

  synchronized void addSuccess() {
    count ++;
  }
  synchronized void addFailure() {failures ++; }
  synchronized void addToLatency(Long time) {
    latency.add(time);
  }
}
