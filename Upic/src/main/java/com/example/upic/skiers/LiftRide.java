package com.example.upic.skiers;

public class LiftRide {

  public int time;
  public int liftID;
  public int waitTime;

  public LiftRide(int time, int liftID, int waitTime) {
    this.time = time;
    this.liftID = liftID;
    this.waitTime = waitTime;
  }

  @Override
  public String toString() {
    return "LiftRide{" +
        "time=" + time +
        ", liftID=" + liftID +
        ", waitTime=" + waitTime +
        '}';
  }
}
