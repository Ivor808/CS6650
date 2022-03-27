import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.swagger.client.model.APIStatsEndpointStats;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


@WebServlet(name = "statistics", value = "/statistics")
public class statistics extends HttpServlet {
  private String message;
  private final static String QUEUE_NAME = "UPIC_QUEUE";
  private GenericObjectPool<Channel> channelPool;
  private final static String HOST_NAME = "3.85.1.207";
  private final static int PORT = 5672;

  public void init() {
    GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<Channel>();
    config.setMinIdle(15);
    config.setMaxIdle(25);
    config.setMaxTotal(50);
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
    message = "Hello World!";
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String urlPath = request.getPathInfo();
    if (!isUrlValid(urlPath)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

      // Hello
      PrintWriter out = response.getWriter();
    APIStatsEndpointStats stats = new APIStatsEndpointStats();
    stats.max(15);
    String statsString = new Gson().toJson(stats);
    out = response.getWriter();
    out.println(statsString);
    out.flush();


  }

  private boolean isUrlValid(String url) {
    return url == null;
  }


  public void destroy() {
  }
}
