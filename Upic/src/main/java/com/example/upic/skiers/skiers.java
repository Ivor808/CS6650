package com.example.upic.skiers;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "skiers", value = "/skiers/*")
public class skiers extends HttpServlet {

  public static final String VERTICAL = "vertical";
  public static final String SEASONS = "seasons";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    String urlPath = request.getPathInfo();
    String[] urlParts = urlPath.split("/");
    String resortId = urlParts[1];

    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }

    if (Arrays.asList(urlParts).contains(VERTICAL)) {
      String skierId = urlParts[1];
      // Hello
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + "vert baby" + "</h1>");
      out.println("</body></html>");
    }
    else if (Arrays.asList(urlParts).contains(SEASONS)) {
      String seasonId = urlParts[3];
      String dayId = urlParts[5];
      String skierId = urlParts[7];
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + "seasons greetings" + "</h1>");
      out.println("</body></html>");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html");
    String urlPath = request.getPathInfo();
    String[] urlParts = urlPath.split("/");

    if (urlPath == null || urlPath.isEmpty() || !Arrays.asList(urlParts).contains(SEASONS) ) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }


    if (Arrays.asList(urlParts).contains(SEASONS)) {
      String resortId = urlParts[1];
      String seasonId = urlParts[3];
      String dayId = urlParts[5];
      String skierId = urlParts[7];
      // Hello
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + "post da vert" + "</h1>");
      out.println("</body></html>");
    }

  }


  private boolean urlIsValid(String[] url) {

    if (Objects.equals(url[0], "skiers") && url[2] == "seasons" && url[4] == "days" &&url[6] == "skiers" ) {
      return true;
    }

    return true;

  }
}
