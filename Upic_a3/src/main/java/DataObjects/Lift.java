package DataObjects;

import io.swagger.client.model.LiftRide;

public class Lift {
  private String liftId;
  private Season[] season;

  public Lift(String liftId, Season[] seasons) {
    this.liftId = liftId;
    this.season = seasons;
  }

  public String getLiftId() {
    return liftId;
  }

  public void setLiftId(String liftId) {
    this.liftId = liftId;
  }

  public Season[] getSeason() {
    return season;
  }

  public void setSeason(Season[] season) {
    this.season = season;
  }
}
