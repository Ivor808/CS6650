package com.example.upic.resorts;

import com.google.gson.Gson;
import io.swagger.client.model.ResortSkiers;
import io.swagger.client.model.ResortsList;
import io.swagger.client.model.ResortsListResorts;
import io.swagger.client.model.SeasonsList;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "resorts", value = "/resorts/*")
public class ResortsServlet extends HttpServlet {

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
      // Hello
      out = response.getWriter();
      out.println(resortJsonString);
      out.flush();
      return;
    }


    String[] urlParts = urlPath.split("/");
    String resortId = urlParts[0];

    if (Arrays.asList(urlParts).contains(SKIERS)) {
      response.setStatus(HttpServletResponse.SC_OK);
      String seasonId = urlParts[2];
      String dayId = urlParts[4];
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      Resort res = new Resort("blah", resortId);
      String resortJsonString = new Gson().toJson(res);
      // Hello
      out = response.getWriter();
      out.println(resortJsonString);
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
      response.getWriter().write("new season created");

    }

  }

  private boolean isValidURL(String urlPath) {
    if (urlPath == null) {
      return true;
    }
    String[] urlParts = urlPath.split("/");

    if (urlParts.length <= 3 && urlParts[2].equals("seasons")) {
      return true;
    } else
      return urlParts[2].equals("seasons") && urlParts[4].equals("day") && urlParts[6].equals("skiers");
  }

  public void destroy() {
  }
}
