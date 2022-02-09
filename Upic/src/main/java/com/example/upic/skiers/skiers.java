package com.example.upic.skiers;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "skiers", value = "/skiers/*")
public class skiers extends HttpServlet {

  public static final String VERTICAL = "vertical";
  public static final String SEASONS = "seasons";

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
      System.out.println(totalHits);
      response.getWriter().write("write successful");

    }
  }

    private boolean isValidURL (String urlPath){
      String[] urlParts = urlPath.split("/");

      if (urlParts.length <= 3 && urlParts[2].equals(VERTICAL)) {
        return true;
      } else
        return urlParts[2].equals("seasons") && urlParts[4].equals("days") && urlParts[6].equals(
            "skiers");
    }
  }

