import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name="HelloWorldServlet", urlPatterns = "/hello")
public class HelloWorldServlet extends HttpServlet {


  // handle a GET request
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    System.out.println("a POST request was sent to /hello");

  }

  //this method is called when get requests are sent to /hello
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    System.out.println("a GET request was sent to /hello");

    //Set the content type in the header of the HTTP request
    //this just tells the client the type of data being sent
//        response.setContentType("text/plain");
//
//        response.getWriter().println("Here is a line");
//        response.getWriter().println("another line");

    //Content types: https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types
    response.setContentType("text/html");
    response.getWriter().println("<h1>Greetings!!!</h1>");
    response.getWriter().println("<h3>Pickles are delicious</h3>");

  }



}