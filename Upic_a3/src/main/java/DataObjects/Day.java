package DataObjects;

import io.swagger.client.model.LiftRide;

public class Day {

  private String dayId;
  private LiftRide[] liftRides;
  private String vertTotal;

  public Day(String dayId, LiftRide[] liftRides, String vertTotal) {
    this.dayId = dayId;
    this.liftRides = liftRides;
    this.vertTotal = vertTotal;
  }

  public Day(String dayId, LiftRide[] liftRides) {
    this.dayId = dayId;
    this.liftRides = liftRides;
    this.vertTotal = "0";
  }

  public String getDayId() {
    return dayId;
  }

  public void setDayId(String dayId) {
    this.dayId = dayId;
  }

  public LiftRide[] getLiftRides() {
    return liftRides;
  }

  public void setLiftRides(LiftRide[] liftRides) {
    this.liftRides = liftRides;
  }

  public String getVertTotal() {
    return vertTotal;
  }

  public void setVertTotal(String vertTotal) {
    this.vertTotal = vertTotal;
  }
}
