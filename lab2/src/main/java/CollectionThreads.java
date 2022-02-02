import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class CollectionThreads {




  public static void main(String[] args) throws InterruptedException {

    Vector<Integer> vec = new Vector<>();

    List<Integer> slist = Collections.synchronizedList(new ArrayList<>());
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date date = new Date(System.currentTimeMillis());
    System.out.println(formatter.format(date));
    for (int i = 0; i < 100000; i++) {
      //date = new Date(System.currentTimeMillis());
      //System.out.println(formatter.format(date));
      int finalI1 = i;
      Runnable r = () -> slist.add(finalI1 +1);
      Runnable r2 = () -> slist.add(finalI1 +1);
      Thread t1 = new Thread(r);
      Thread t2 = new Thread(r2);
      t1.start();
      t2.start();
      t1.join(100);
      t2.join(100);
    }
    Date end = new Date(System.currentTimeMillis());
    System.out.println(formatter.format(end));
    System.out.println(slist.size());



  }

}
