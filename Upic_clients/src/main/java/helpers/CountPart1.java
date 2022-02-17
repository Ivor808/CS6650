package helpers;

import java.util.ArrayList;

public class CountPart1 {
  private Integer count;
  private Integer failures;

  public CountPart1() {
    this.count = 0;
    this.failures = 0;
  }

  synchronized void addSuccess() {
    count ++;
  }
  synchronized void addFailure() {failures ++; }

  public Integer getCount() {
    return count;
  }

  public Integer getFailures() {
    return failures;
  }

}
