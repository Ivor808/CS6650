import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Consumer {

  private final static String QUEUE_NAME = "UPIC_QUEUE";
  private final static String HOST="18.209.224.176";
  private final static int PORT=5672;

  public static void main(String[] args) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(HOST);
    factory.setPort(PORT);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    ConcurrentHashMap<Delivery, String> consumedMessages = new ConcurrentHashMap<>();

    ExecutorService executor = Executors.newFixedThreadPool(10);

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      consumedMessages.put(delivery,message);
      System.out.println(consumedMessages.size());

    };

    Runnable consumerTask = () -> {
      try {
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    };

    executor.execute(consumerTask);


  }

}
