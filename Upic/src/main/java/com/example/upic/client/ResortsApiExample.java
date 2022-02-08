package com.example.upic.client;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.model.ResortsList;
public class ResortsApiExample {

  public static void main(String[] args) {
    ApiClient client = new ApiClient();
    client.setBasePath("http://localhost:8080");
    ResortsApi apiInstance = new ResortsApi(client);
    try {
      ResortsList result = apiInstance.getResorts();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ResortsApi#getResorts");
      e.printStackTrace();
    }
  }
}