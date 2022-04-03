import DataObjects.Day;
import DataObjects.Resort;
import DataObjects.Season;
import DataObjects.Skier;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.swagger.client.model.LiftRide;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@WebServlet(name = "skiers", value = "/skiers/*")
public class skiers extends HttpServlet {

  public static final String VERTICAL = "vertical";
  public static final String SEASONS = "seasons";
  private final static String QUEUE_NAME = "Skiers";
  private final static String RESORT_QUEUE = "resort";
  private final static String HOST_NAME = "52.91.133.89";
  private final static int PORT = 5672;
  private GenericObjectPool<Channel> channelPool;
  private int totalHits = 0;


  public void init() {
    GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<Channel>();
    config.setMinIdle(64);
    config.setMaxIdle(128);
    config.setMaxTotal(256);
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(HOST_NAME);
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

  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    String urlPath = request.getPathInfo();

    if (urlPath == null || !isValidURL(urlPath)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");

    if (Arrays.asList(urlParts).contains(VERTICAL)) {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      String skierId = urlParts[1];
      // Hello
      PrintWriter out = response.getWriter();
      String totalVert = new Gson().toJson(1000);
      out = response.getWriter();
      out.println(totalVert);
      out.flush();
    } else if (Arrays.asList(urlParts).contains(SEASONS)) {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      String resortId = urlParts[1];
      String seasonId = urlParts[3];
      String dayId = urlParts[5];
      String skierId = urlParts[7];
      PrintWriter out = response.getWriter();
      String totalVert = new Gson().toJson(1000);
      out = response.getWriter();
      out.println(totalVert);
      out.flush();

    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html");
    String urlPath = request.getPathInfo();
    if (urlPath == null || !isValidURL(urlPath)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }


    String[] urlParts = urlPath.split("/");
    if (Arrays.asList(urlParts).contains(SEASONS)) {
      response.setStatus(HttpServletResponse.SC_CREATED);
      String resortId = urlParts[1];
      String seasonId = urlParts[3];
      String dayId = urlParts[5];
      String skierId = urlParts[7];
      PrintWriter out = response.getWriter();
      //write to db
      totalHits ++;

      String lift = new Gson().toJson(request.getReader().lines().collect(Collectors.joining()));
      lift = lift.replaceAll("\\\\", "");
      lift = lift.substring(1,lift.length()-1);
      if (!(lift.contains("time") && lift.contains("liftID") && lift.contains("waitTime"))) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Must provide time, liftID, and waitTime params!");
        return;
      }
      LiftRide liftRide = new Gson().fromJson(lift, LiftRide.class);
      Day day = new Day(dayId, new LiftRide[]{liftRide},String.valueOf(liftRide.getLiftID() * 10));
      Season season = new Season(seasonId,new Day[]{day});
      Skier skier = new Skier(season,skierId, String.valueOf(liftRide.getLiftID() * 10), dayId, liftRide, resortId);
      String stringSkier = new Gson().toJson(skier);
      JsonObject jSkier = new Gson().fromJson(stringSkier, JsonObject.class);


      Channel channel = null;
      try {
        channel = channelPool.borrowObject();
        channel.basicPublish("", QUEUE_NAME, null, stringSkier.getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("",RESORT_QUEUE,null,stringSkier.getBytes(StandardCharsets.UTF_8));
        channelPool.returnObject(channel);

      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println(" [x] Sent '" + jSkier.toString() + "'");
      response.getWriter().write(jSkier.toString());

    }
  }

    private boolean isValidURL (String urlPath){
      String[] urlParts = urlPath.split("/");
      if (urlParts.length <= 2) {
        return false;
      }
      if (urlParts.length <= 3 && urlParts[2].equals(VERTICAL)) {
        return true;
      } else
        return urlParts[2].equals("seasons") && urlParts[4].equals("days") && urlParts[6].equals(
            "skiers");
    }
  }

