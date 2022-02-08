package com.example.upic.statistics;

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
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");

    if(urlParts[0].equals("statistics")){
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("text/html");

      // Hello
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      out.println("<h1>" + message + "</h1>");
      out.println("</body></html>");
    }

  }


  public void destroy() {
  }
}
