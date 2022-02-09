package com.example.upic.statistics;

import com.google.gson.Gson;
import io.swagger.client.model.APIStatsEndpointStats;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "statistics", value = "/statistics")
public class statistics extends HttpServlet {
  private String message;

  public void init() {
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
