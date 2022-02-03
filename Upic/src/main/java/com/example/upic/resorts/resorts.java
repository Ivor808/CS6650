package com.example.upic.resorts;

import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "resorts", value = "/resorts/*")
public class resorts extends HttpServlet {

  public static final String SEASONS = "seasons";
  public static final String SKIERS = "skiers";
  private String message;

  public void init() {
    message = "Hello World!";
  }

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

    if (Arrays.asList(urlParts).contains(SKIERS)) {
      // Hello
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + message + "</h1>");
      out.println("</body></html>");
    }
    else if (Arrays.asList(urlParts).contains(SEASONS)) {
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + "seasons!" + "</h1>");
      out.println("</body></html>");
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    String urlPath = request.getPathInfo();

    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }
    String[] urlParts = urlPath.split("/");

    if (Arrays.asList(urlParts).contains(SEASONS)) {
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + "the Post!" + "</h1>");
      out.println("</body></html>");

    }

  }

  public void destroy() {
  }
}
