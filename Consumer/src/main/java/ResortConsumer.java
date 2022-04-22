import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPooled;

public class ResortConsumer {
  private final static String QUEUE_NAME = "resort";
  private final static String HOST="100.26.219.200";
  private final static String REDIS_HOST="54.159.37.116";
  private final static String RABBIT_HOST ="52.91.133.89";
  private final static int PORT=5672;

  public static void main(String[] args) throws IOException, TimeoutException {
    GenericObjectPool<Channel> channelPool;
    GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<Channel>();
    config.setMinIdle(64);
    config.setMaxIdle(128);
    config.setMaxTotal(256);
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(RABBIT_HOST);
    connectionFactory.setPort(PORT);
    ChannelFactory factory = null;
    try {
      factory = new ChannelFactory(connectionFactory.newConnection());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
    channelPool = new GenericObjectPool<Channel>(factory,config);
    try {
      channelPool.addObject();
    } catch (Exception e) {
      e.printStackTrace();
    }

    ConcurrentHashMap<String, String> consumedMessages = new ConcurrentHashMap<>();

    JedisPooled jedis = new JedisPooled(REDIS_HOST, 6379);

    ExecutorService executor = Executors.newFixedThreadPool(64);

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");

      JsonObject skier = new Gson().fromJson(message,JsonObject.class);
      String skierId = skier.get("skierId").toString();
      skierId = skierId.substring(1,skierId.length()-1);
      String season = skier.get("season").getAsJsonObject().get("seasonId").toString();
      String dayId = skier.get("day").toString();
      String liftId = skier.get("liftRide").getAsJsonObject().get("liftID").toString();
      String resortId = skier.get("resortid").toString();

      String finalSkierId = skierId;
      Runnable incr = () -> {
        jedis.sadd(season+dayId+resortId,finalSkierId);
      };
      Thread incrThread = new Thread(incr);
      incrThread.start();

      Runnable incr2 = () -> {
        jedis.incr(liftId + dayId);
      };
      Thread incr2Thread = new Thread(incr2);
      incr2Thread.start();

      Runnable lpush = () -> {
        jedis.lpush(dayId,liftId);
      };
      Thread lpushThread = new Thread(lpush);
      lpushThread.start();

    };

    Runnable consumerTask = () -> {
      Channel channel = null;
      try {
        channel = channelPool.borrowObject();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    executor.execute(consumerTask);


  }

}
