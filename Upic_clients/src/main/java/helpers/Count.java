package helpers;

import java.util.ArrayList;

public class Count {
  private Integer count;
  private Integer failures;
  private ArrayList<ResponseRecord> records = new ArrayList<>();
  private ArrayList<Long> latency = new ArrayList<>();

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

  public Integer getCount() {
    return count;
  }

  public Integer getFailures() {
    return failures;
  }

  public ArrayList<ResponseRecord> getRecords() {
    return records;
  }

  public ArrayList<Long> getLatency() {
    return latency;
  }
}
