package com.example.upic.skiers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "skiers", value = "/skiers/*")
public class skiers extends HttpServlet {

  public static final String VERTICAL = "vertical";
  public static final String SEASONS = "seasons";
  private final static String QUEUE_NAME = "UPIC_QUEUE";
  private final static String HOST_NAME = "54.175.237.216";
  private final static int PORT = 1883;

  private int totalHits = 0;

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

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(HOST_NAME);
    factory.setPort(PORT);

    String[] urlParts = urlPath.split("/");
    if (Arrays.asList(urlParts).contains(SEASONS)) {
      response.setStatus(HttpServletResponse.SC_CREATED);
      String resortId = urlParts[1];
      String seasonId = urlParts[3];
      String dayId = urlParts[5];
      String skierId = urlParts[7];
      // Hello
      PrintWriter out = response.getWriter();
      //write to db
      totalHits ++;

      try (Connection connection = factory.newConnection();
          Channel channel = connection.createChannel()) {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicPublish("", QUEUE_NAME, null, skierId.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + skierId + "'");
      } catch (TimeoutException e) {
        e.printStackTrace();
      }
      System.out.println(totalHits);
      response.getWriter().write("write successful");

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

