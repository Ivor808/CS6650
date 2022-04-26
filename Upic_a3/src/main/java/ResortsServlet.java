import DataObjects.Day;
import DataObjects.Lift;
import DataObjects.Resort;
import DataObjects.Season;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.ResortSkiers;
import io.swagger.client.model.ResortsList;
import io.swagger.client.model.ResortsListResorts;
import io.swagger.client.model.SeasonsList;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPooled;


@WebServlet(name = "resorts", value = "/resorts/*")
public class ResortsServlet extends HttpServlet {

  public static final String SEASONS = "seasons";
  public static final String SKIERS = "skiers";
  private final static String QUEUE_NAME = "Resorts";
  private GenericObjectPool<Channel> channelPool;
  private final static String RABBIT_HOST = "52.4.57.182";
  private final static String REDIS_HOST="18.212.93.180";
  private final static int PORT = 5672;
  private JedisPooled jedis;

  public void init() {
    GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<Channel>();
    config.setMinIdle(15);
    config.setMaxIdle(25);
    config.setMaxTotal(50);
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(RABBIT_HOST);
    connectionFactory.setPort(PORT);
    ChannelFactory factory = null;

    jedis = new JedisPooled(REDIS_HOST, 6379);
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

  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    String urlPath = request.getPathInfo();
    PrintWriter out = response.getWriter();
    if (!isValidURL(urlPath)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }

    if (urlPath == null) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ResortsList resorts = new ResortsList();
      ResortsListResorts resort = new ResortsListResorts();
      resort = resort.resortName("foo");
      resort.setResortID(1);
      resorts.addResortsItem(resort);

      String resortJsonString = new Gson().toJson(resort);
      out = response.getWriter();
      out.println(resortJsonString);
      out.flush();
      return;
    }


    String[] urlParts = urlPath.split("/");
    String resortId = urlParts[1];

    if (Arrays.asList(urlParts).contains(SKIERS)) {
      response.setStatus(HttpServletResponse.SC_OK);
      String seasonId = urlParts[3];
      String dayId = urlParts[5];
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      String redisKey = seasonId + dayId + resortId;
      Long count = jedis.scard(redisKey);
      out = response.getWriter();
      out.println(count);
      out.flush();
    } else if (Arrays.asList(urlParts).contains(SEASONS)) {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      ResortSkiers skiers = new ResortSkiers();
      skiers = skiers.numSkiers(1);
      skiers.setTime("15");
      String skiersJsonString = new Gson().toJson(skiers);
      // Hello
      out = response.getWriter();
      out.println(skiersJsonString);
      out.flush();
    }
    else  {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      SeasonsList seasons = new SeasonsList();
      seasons.addSeasonsItem("winter2021");
      seasons.addSeasonsItem("winter2022");

      String seasonsString = new Gson().toJson(seasons);
      out = response.getWriter();
      out.println(seasonsString);
      out.flush();
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    String urlPath = request.getPathInfo();
    String[] urlParts = urlPath.split("/");
    String resortId = urlParts[1];
    String liftId = String.valueOf(ThreadLocalRandom.current().nextInt(1, 15 + 1));
    String day = String.valueOf(ThreadLocalRandom.current().nextInt(1, 365 + 1));

    if (!isValidURL(urlPath)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }



    if (Arrays.asList(urlParts).contains(SEASONS)) {
      response.setStatus(HttpServletResponse.SC_CREATED);
      PrintWriter out = response.getWriter();
      // write to db
      //


      String season = new Gson().toJson(request.getReader().lines().collect(Collectors.joining()));
      season = season.replaceAll("\\\\", "");
      season = season.substring(1,season.length()-1);
      JsonObject jSeason = new Gson().fromJson(season, JsonObject.class);
      String seasonYear = jSeason.get("year").toString();
      Resort res = new Resort("Aspen", resortId,new Lift[]{new Lift(liftId, new Season[]{new Season(season, new Day[]{new Day(day,new LiftRide[]{})})})});
      String stringRes = new Gson().toJson(res);
      JsonObject jRes = new Gson().fromJson(stringRes, JsonObject.class);
      Channel channel = null;

      try {
        channel = channelPool.borrowObject();
        channel.basicPublish("", QUEUE_NAME, null, stringRes.getBytes(StandardCharsets.UTF_8));
        channelPool.returnObject(channel);

      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println(" [x] Sent '" + seasonYear + "'");
      response.getWriter().write("new season created for " + season + " at resort " + resortId);

    }

  }

  private boolean isValidURL(String urlPath) {
    if (urlPath == null) {
      return true;
    }
    String[] urlParts = urlPath.split("/");
    if (urlParts.length <= 2) {
      return false;
    }
    if (urlParts.length == 3 && urlParts[2].equals("seasons")) {
      return true;
    } else if (urlParts.length <= 7 ) {
      return urlParts[2].equals("seasons") && urlParts[4].equals("day") && urlParts[6].equals("skiers");
  }
  else {
  return false;}
  }

  public void destroy() {
  }
}
