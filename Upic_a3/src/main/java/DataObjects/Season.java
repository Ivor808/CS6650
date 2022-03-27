package DataObjects;

public class Season {

  private String seasonId;
  public Day[] days;

  public Season(String seasonId, Day[] days) {
    this.seasonId = seasonId;
    this.days = days;
  }

  public String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public Day[] getDays() {
    return days;
  }

  public void setDays(Day[] days) {
    this.days = days;
  }
}
