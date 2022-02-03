package com.example.upic.skiers;

import java.io.PrintWriter;
import java.util.Arrays;
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

    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }
    String[] urlParts = urlPath.split("/");

    if (Arrays.asList(urlParts).contains(VERTICAL)) {
      // Hello
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + "vert baby" + "</h1>");
      out.println("</body></html>");
    }
    else if (Arrays.asList(urlParts).contains(SEASONS)) {
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + "seasons greetings" + "</h1>");
      out.println("</body></html>");
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
