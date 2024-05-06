/*

*/

package serverFramework;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
   The HttpApplicationServer class delegates to this class
   the rsponsibility of handling one client connection.
<p>
   This class reads the request line, the request headers,
   and the (optional) request body from the client. It
   bundles that information into an HttpRequest object
   and it also parses that information for use by the
   HTTP methods. This class dispatches the HttpRequest
   object to the appropriate implementation of an HTTP
   method. When the HTTP method returns, this class
   uses the returned HttpResponse object to send the
   web application's results to the client.
*/
public class ClientConnection
{
   private final Socket clientSocket;
   private final int clientNumber;
   private HttpApplicationServer server;


   /**
      This constructor reads and parses the client's request
      line and all the request headers. This constructor
      puts that information into a HttpRequest object.
      This constructor then dispatches the appropriate
      HTTP method handler.
   */
   @SuppressWarnings("deprecation")
   public ClientConnection(Socket clientSocket,
                           int clientNumber,
                           HttpApplicationServer server)
   throws UnsupportedEncodingException, IOException
   {
      this.clientSocket = clientSocket;
      this.clientNumber = clientNumber;
      this.server = server;

      // Make the low-level input and output streams easier to use.
      DataInputStream inFromClient = new DataInputStream(
                                        clientSocket.getInputStream());

      PrintStream outToClient = new PrintStream(
                                   clientSocket.getOutputStream());

      System.out.println("===> Reading request line & request headers from Client " + clientNumber);

      // Create an HttpRequest object to pass
      // to the HTTP method handlers.
      final HttpRequest req = new HttpRequest();

      // Create a HttpResponse object to be
      // filled in by the HTTP method handlers.
      final HttpResponse res = new HttpResponse();

      //**** Read the request line.
      @SuppressWarnings("deprecation")
      String requestLine = inFromClient.readLine();

      // Log the request line.
      System.out.println(">" + requestLine);

      if (null == requestLine)
      {
         System.out.println("===> null Request Line");
         clientSocket.close();
         System.out.println("SERVER: Client " + clientNumber + ": Closed socket.");
         return;  // Finished with this client.
      }
      requestLine = requestLine.trim();
      req.setRequestLine(requestLine);


      //**** Read the request headers.
      req.connectionClose = false; // default for HTTP/1.1

      String requestHeader;
      //@SuppressWarnings("deprecation")
      while ( (requestHeader = inFromClient.readLine()) != null )
      {
         req.addHeader(requestHeader);
         // Log the request header.
         System.out.println(">" + requestHeader);
         if ( requestHeader.startsWith("Connection") )
         {
            final int i = requestHeader.indexOf(':');
            final String value = requestHeader.substring(i+1).trim();
            if ( value.equals("close") )
            {
               req.connectionClose = true;
            }
         }
         else if ( requestHeader.startsWith("Content-Length") )
         {
            final int i = requestHeader.indexOf(':');
            req.entityLength = Integer.parseInt(requestHeader.substring(i+2));
            // Log the value of Content-Length
            //System.out.println("===> Content-Length = " + req.entityLength);
         }
         else if ( requestHeader.isEmpty() )  // stop on a blank line
         {
            break;
         }
      }
      //**** Done reading the request headwers.


      //**** Parse and validate the request line.
      // Break the request line into a request method, url, and http version.
      final int index1 = requestLine.indexOf(" ");
      final int index2 = requestLine.lastIndexOf(" ");
      final String requestMethod = requestLine.substring(0, index1).toUpperCase();
      final String requestURL = requestLine.substring(index1, index2).trim();
      final String requestHTTP = requestLine.substring(index2).trim().toUpperCase();
      req.requestMethod = requestMethod;
      req.requestURL = requestURL;
      req.requestHTTP = requestHTTP;

      // Check for a proper version of HTTP.
      if ( ! ( requestHTTP.equals("HTTP/1.1")
            || requestHTTP.equals("HTTP/1.0")
            || requestHTTP.equals("HTTP/0.9") ) )
      {
         server.httpMethods.sendErrorResponse(req, res,
             "505", "HTTP Version Not Supported",
             "The HTTP version in this request<br>" + requestLine +
                  "<br>is unsupported.");

         clientSocket.close();
         System.out.println("SERVER: Client " + clientNumber + ": Closed socket.");
         return;  // Finished with this client.
      }

      // Check that the request method is supported.
      if ( ! ( requestMethod.equals("GET")
            || requestMethod.equals("HEAD")
            || requestMethod.equals("OPTIONS")
            || requestMethod.equals("POST")
            || requestMethod.equals("PUT")
            || requestMethod.equals("PATCH")
            || requestMethod.equals("DELETE")
            || requestMethod.equals("TRACE")
            || requestMethod.equals("CONNECT") ) )
      {
         server.httpMethods.sendErrorResponse(req, res,
             "501", "Not Implemented",
             "The HTTP method in this request<br>" + requestLine +
                  "<br>is not supported.");

         clientSocket.close();
         System.out.println("SERVER: Client " + clientNumber + ": Closed socket.");
         return;  // Finished with this client.
      }

      // Important: We need to decode the URL from its urlencoded form.
      // See
      //   http://www.w3schools.com/tags/ref_urlencode.asp
      // or
      //   http://en.wikipedia.org/wiki/URL_encoding
      final String decodedURL = URLDecoder.decode(requestURL, "UTF-8"); // throws UnsupportedEncodingException
      req.decodedURL = decodedURL;

      // Log the results of decoding the URL.
      System.out.println("===> requestURL = " + requestURL);
      System.out.println("===> decodedURL = " + decodedURL);

      // Break the decoded URL into a resource and a query.
      final String resourceName;
      final String queryString;
      if ( 0 < decodedURL.indexOf('?') )
      {
         final String[] temp = decodedURL.split("\\Q?\\E");
         resourceName  = temp[0];
         queryString   = temp[1];
      }
      else
      {
         resourceName = decodedURL;
         queryString = null;
      }
      req.resourceName = resourceName;
      req.queryString = queryString;

      // Important: Check that the resource is not a file
      // that "escapes" from the server's root directory.
      // This is a form of server attack that tries to
      // sneek in a URL like this.
      //    /../../../../Windows/System32/cmd.exe
      final File file1 = new File(server.serverRoot).getCanonicalFile();
      final File file2 = new File(server.serverRoot + resourceName).getCanonicalFile();
      if (! file2.getAbsolutePath().startsWith(file1.getAbsolutePath()))
      {
         server.httpMethods.sendErrorResponse(req, res,
             "403", "Forbidden",
             "You can't pull this stunt<br>"+requestLine+"<br>on us.");

         clientSocket.close();
         System.out.println("SERVER: Client " + clientNumber + ": Closed socket.");
         return;  // Finished with this client.
      }

      // Log the results of parsing the request line.
      System.out.println("===> Request method = " + requestMethod);
      System.out.println("===> Resource name = " + resourceName);
      System.out.println("===> Query string = " + queryString);
      System.out.println("===> HTTP version = " + requestHTTP);
      //**** Done parsing the request line.


      //**** Read the entity body, if one exists.
      if (req.entityLength > 0)
      {
         System.out.println("===> Request has an entity body.");
         // Read the entity body.
         System.out.println("===> Reading the entity body.");
         final int entityLength = req.entityLength;
         final byte[] entityBodyBytes = new byte[entityLength];
         int totalBytesRcvd = 0;  // Total bytes received so far.
         int bytesRcvd;           // bytes received in last read.
         while ( totalBytesRcvd < entityLength )
         {
            try
            {
               bytesRcvd = inFromClient.read(entityBodyBytes,
                                             totalBytesRcvd,
                                             entityLength - totalBytesRcvd);
               if (-1 == bytesRcvd)
               {
                  break; // reached end-of-file
               }
               totalBytesRcvd += bytesRcvd;
               System.out.println("===> bytes received so far = " + totalBytesRcvd);
            }
            catch (IOException e)
            {
               System.out.println("SERVER: doPost: Unable to read entity body.");
               System.out.println( e );
            }
            System.out.println("===> Total bytes received = " + totalBytesRcvd);
         }
         req.setEntityBody(entityBodyBytes);
      }


      //**** Dispatch the request method.
      if ( requestMethod.equals("GET")
        || requestMethod.equals("HEAD") )
      {
         server.httpMethods.doGet(req, res);
      }
      else if ( requestMethod.equals("OPTIONS") )
      {
         server.httpMethods.doOptions(req, res);
      }
      else if ( requestMethod.equals("POST") )
      {
         server.httpMethods.doPost(req, res);
      }
      else if ( requestMethod.equals("PUT") )
      {
         System.out.println("SERVER: Calling doPut:");
         server.httpMethods.doPut(req, res);
      }
      else if ( requestMethod.equals("PATCH") )
      {
         server.httpMethods.doPut(req, res);
      }
      else if ( requestMethod.equals("DELETE") )
      {
         server.httpMethods.doDelete(req, res);
      }
      else if ( requestMethod.equals("TRACE") )
      {
         server.httpMethods.doTrace(req, res);
      }
      else if ( requestMethod.equals("CONNECT") )
      {
         server.httpMethods.doConnect(req, res);
      }
      else  // unsupported request method
      {
         server.httpMethods.sendErrorResponse(req, res,
             "501", "Not Implemented",
             "This server does not implement this method: " + requestMethod);
      }

      //**** Send the response to the client.
      // Send the response line.
      outToClient.print(res.getResponseLine() + "\r\n");
      System.out.println("<" + res.getResponseLine());
      // The response headers.
      for (final String header : res.headers)
      {
         outToClient.print(header + "\r\n");
         System.out.println("<" + header);
      }
      // Add a blank line to denote end of response headers.
      outToClient.print("\r\n");
      System.out.println("<");

      // Send the entity body.
      outToClient.write(res.entityBody, 0, res.entityBody.length);
      System.out.println("<=== Resource " + resourceName + " sent to Client " + clientNumber);

      outToClient.flush();

      clientSocket.close();
      System.out.println("SERVER: Client " + clientNumber + ": Closed socket.");
      return;  // Finished with this client.
   }
}
