import DataObjects.Day;
import DataObjects.Lift;

public class Resort {
  private String name;
  private String resortId;
  private Lift[] lifts;

  public Resort(String name, String id, Lift[] lifts) {
    this.name = name;
    this.resortId = id;
    this.lifts = lifts;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getResortId() {
    return resortId;
  }

  public void setResortId(String resortId) {
    this.resortId = resortId;
  }

  public Lift[] getLifts() {
    return lifts;
  }

  public void setLifts(Lift[] lifts) {
    this.lifts = lifts;
  }
}
