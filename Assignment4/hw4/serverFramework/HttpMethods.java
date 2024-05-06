/*

*/

package serverFramework;

import java.time.format.DateTimeFormatter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
   This is part of the HttpApplicationServer framework for
   implementing simple web applications. An implementation
   of this interface defines the HTTP methods that implement
   whatever it is that you want your web app to do.
*/
public interface HttpMethods
{
   public void doGet    (HttpRequest req, HttpResponse res);
   public void doHead   (HttpRequest req, HttpResponse res);
   public void doPost   (HttpRequest req, HttpResponse res);
   public void doPut    (HttpRequest req, HttpResponse res);
   public void doPatch  (HttpRequest req, HttpResponse res);
   public void doDelete (HttpRequest req, HttpResponse res);
   public void doOptions(HttpRequest req, HttpResponse res);
   public void doTrace  (HttpRequest req, HttpResponse res);
   public void doConnect(HttpRequest req, HttpResponse res);
   public void setServer(HttpApplicationServer server);


   /**
      Every web app should be able to send error responses
      back to clients. So this interface has this method
      as a default method. Any of the other HTTP methods
      can call this method to send an error response to
      a client.
      <p>
      See
         https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
         https://httpwg.org/specs/rfc9110.html#overview.of.status.codes

      @param req       an HTTP request message object
      @param res       an HTTP response message object
      @param code      an HTTP response status code
      @param shortMsg  the HTTP reason phrase describing the status code
      @param longMsg   an optional longer description of the error
   */
   public default void sendErrorResponse(HttpRequest  req,
                                         HttpResponse res,
                                         String code,
                                         String shortMsg,
                                         String longMsg)
   {
      System.out.println("SERVER: sendErrorResponse: " + code + " " + shortMsg);

      // Build the response body (if there is a long message).
      String body = longMsg == null || longMsg.isEmpty() ? "" :
                "<!doctype html>\r\n" +
                "<html>\r\n" +
                "<head>\r\n" +
                "<title>" + code + " " + shortMsg + "</title>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "<h1>" + shortMsg + "</h1>\r\n" +
                "<p>" + code + ": " + shortMsg + "</p>\r\n" +
                "<p>" + longMsg + "</p>\r\n" +
                "<hr>\r\n" +
                "</body>\r\n" +
                "</html>\r\n";

      // Add the response line.
      res.setResponseLine("HTTP/1.1 " + code + " " + shortMsg);

      // Add the response headers.
      final String date = OffsetDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.RFC_1123_DATE_TIME);
      res.addHeader("Date: " + date);
      res.addHeader("Server: HttpApplicationServer.java");
      res.addHeader("Connection: close");
      res.addHeader("Content-type: text/html");
      res.addHeader("Content-Length: " + body.length());

      // Add the entity body.
      res.setEntityBody( body.getBytes() );

      return;
   }
}
