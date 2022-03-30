package DataObjects;

import io.swagger.client.model.LiftRide;

public class Skier {

  private Season season;
  private String skierId;
  private String vertTotal;
  private String day;
  private LiftRide liftRide;

  public Skier(Season season, String skierId, String vertTotal, String day, LiftRide liftRide) {
    this.season = season;
    this.skierId = skierId;
    this.vertTotal = vertTotal;
    this.day = day;
    this.liftRide = liftRide;
  }

  public Season getSeasons() {
    return season;
  }

  public void setSeasons(Season seasons) {
    this.season = seasons;
  }

  public String getSkierId() {
    return skierId;
  }

  public void setSkierId(String skierId) {
    this.skierId = skierId;
  }

  public Season getSeason() {
    return season;
  }

  public void setSeason(Season season) {
    this.season = season;
  }

  public String getVertTotal() {
    return vertTotal;
  }

  public void setVertTotal(String vertTotal) {
    this.vertTotal = vertTotal;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public LiftRide getLiftRide() {
    return liftRide;
  }

  public void setLiftRide(LiftRide liftRide) {
    this.liftRide = liftRide;
  }
}
