package DataObjects;

public class Skier {

  private Season[] seasons;
  private String skierId;

  public Skier(Season[] seasons, String skierId) {
    this.seasons = seasons;
    this.skierId = skierId;
  }

  public Season[] getSeasons() {
    return seasons;
  }

  public void setSeasons(Season[] seasons) {
    this.seasons = seasons;
  }

  public String getSkierId() {
    return skierId;
  }

  public void setSkierId(String skierId) {
    this.skierId = skierId;
  }
}
