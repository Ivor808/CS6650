public class LiftRide {

  public String time;
  public String liftID;
  public String waitTime;
  public String skierId;

  public LiftRide(String time, String liftID, String waitTime) {
    this.time = time;
    this.liftID = liftID;
    this.waitTime = waitTime;
  }

  public LiftRide(String time, String liftID, String waitTime, String skierId) {
    this.time = time;
    this.liftID = liftID;
    this.waitTime = waitTime;
    this.skierId = skierId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getLiftID() {
    return liftID;
  }

  public void setLiftID(String liftID) {
    this.liftID = liftID;
  }

  public String getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(String waitTime) {
    this.waitTime = waitTime;
  }

  public String getSkierId() {
    return skierId;
  }

  public void setSkierId(String skierId) {
    this.skierId = skierId;
  }

  @Override
  public String toString() {
    return "LiftRide{" +
        "time=" + time +
        ", liftID=" + liftID +
        ", waitTime=" + waitTime +
        ", skierId='" + skierId + '\'' +
        '}';
  }
}
