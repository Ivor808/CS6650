import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import redis.clients.jedis.JedisPooled;

public class SkierConsumer {

  private final static String QUEUE_NAME = "UPIC_QUEUE";
  private final static String HOST="100.26.219.200";
  private final static String REDIS_HOST="35.172.229.70";
  private final static int PORT=5672;

  public static void main(String[] args) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(HOST);
    factory.setPort(PORT);
    Connection connection = factory.newConnection();

    ConcurrentHashMap<String, String> consumedMessages = new ConcurrentHashMap<>();

    JedisPooled jedis = new JedisPooled(REDIS_HOST, 6379);

    ExecutorService executor = Executors.newFixedThreadPool(64);

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");

      JsonObject skier = new Gson().fromJson(message,JsonObject.class);
      String skierId = skier.get("skierId").toString();
      skierId = skierId.substring(1,skierId.length()-1);

      jedis.set("skier" + skierId,message);
      consumedMessages.put(skierId,message);
      System.out.println(skierId);


    };
    System.out.println(consumedMessages.size());

    Runnable consumerTask = () -> {
      try {
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    };

    executor.execute(consumerTask);


  }

}
