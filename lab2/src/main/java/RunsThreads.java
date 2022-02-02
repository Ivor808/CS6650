import java.text.SimpleDateFormat;
import java.util.Date;

public class RunsThreads {

  final static private int NUMTHREADS = 100000;

  private int count = 0;

  public int getCount() {
    return count;
  }

  synchronized public void incCount(int numTimes) {
    for (int i = 0 ; i < numTimes; i ++) {
      count ++;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    final RunsThreads counter = new RunsThreads();
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date date = new Date(System.currentTimeMillis());
    System.out.println(formatter.format(date));

    final AddThread[] threads = new AddThread[NUMTHREADS];

    for (int i =0; i < NUMTHREADS; i ++) {
      AddThread thread = new AddThread(counter);
      new Thread(thread).start();

    }

    Thread.sleep(1000);

    Date end = new Date(System.currentTimeMillis());
    System.out.println(formatter.format(end));
    System.out.println(counter.count);
  }

}
