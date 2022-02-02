import java.text.SimpleDateFormat;
import java.util.Date;

public class AddThread implements Runnable{
  RunsThreads run;

  public AddThread(RunsThreads run) {
    this.run = run;
  }


  @Override
  public void run() {

    run.incCount(10);

  }
}
