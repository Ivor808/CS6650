package helpers;

public class ResponseRecord {
  private String startTime;
  private String requestType;
  private Double latency;
  private int responseCode;

  public ResponseRecord(String startTime, String requestType, Double latency,
      int responseCode) {
    this.startTime = startTime;
    this.requestType = requestType;
    this.latency = latency;
    this.responseCode = responseCode;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public Double getLatency() {
    return latency;
  }

  public void setLatency(Double latency) {
    this.latency = latency;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  @Override
  public String toString() {
    return startTime + ", " +
          requestType + ", "  +
         latency + ", " +
        responseCode;
  }
}
