/*


*/

import serverFramework.*;

import java.time.format.DateTimeFormatter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import java.util.Scanner;

import Assignment4.hw4.serverFramework.HttpApplicationServer;
import Assignment4.hw4.serverFramework.HttpMethodsAdaptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;

/**
   Use the serverFramework package to
   implement a simple web application.
*/
public class Hw4 extends HttpMethodsAdaptor
{
   /**
      Give your application server an implementation of GET.
      <p>
      This GET method expects one URL query parameter
      that is an integer between 1 and 25,000. This GET
      method should open the local file called photo-urls.txt
      and read the line of that file specified by the URL
      query parameter. That line of the file is a URL to a
      photo. Imbedd that URL into an approprite HTML web
      page. See the demo program for an example of what
      that HTML should be.
   */
   @Override
   public void doGet(HttpRequest req, HttpResponse res)
   {
      if ( ! req.resourceName.equals("/photo.html") )
      {
         doStaticGet(req, res);
         return;
      }

      System.out.println("SERVER: Hw4 GET photo method:");

      // Get the URL's query string from the req object.
      String queryString = req.queryString;

      // Parse it.
      int n = Integer.parseInt(queryString);int n = 


      System.out.println("=====> Parsed query parameter to: " + n);

      // Read line n from the local file photo-urls.txt.
      String photoURL = readPhotoURL(n);


      System.out.println("=====> Found URL: " + photoURL);

      // Build the HTML response page.
      // Run the demo program to see what the HTML should be.
      String body = "<html><head><title>Photo</title></head><body>"
                    + "<h1>Photo</h1>"
                    + "<img src=\"" + photoURL + "\">"
                    + "</body></html>";

      // Add the response line.
      res.setResponseLine("HTTP/1.1 200 OK");

      // Add the response headers.
      final String date = OffsetDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.RFC_1123_DATE_TIME);
      res.addHeader("Date: " + date);
      res.addHeader("Server: SampleServer_ver2.java");
      res.addHeader("Connection: close");
      res.addHeader("Content-type: text/html");
      res.addHeader("Content-Length: " + body.length());

      // Add the entity body.
      res.setEntityBody( body.getBytes() );

      return;
   }

   /**
      Give your application server an implementation of POST.
      <p>
      Your POST implementation can be very simple. It should
      do the same thing as the GET method. All you need to
      do is copy the entity body field in the req object into
      the query string filed in the req object. Then just make
      the method call doGet(req, res). In other words, you
      essentially forward the do post request to the do get
      handler.
   */
   @Override
   public void doPost(HttpRequest req, HttpResponse res)
   {
      System.out.println("SERVER: Hw4 POST photo method:");

      // Set the req object's query string
      // using the req object's entity body.
      req.queryString = req.entityBody;



      // Call doGet().
      doGet(req, res);
   }

   private String readPhotoURL(int lineNumber) {
      String photoURL = "";
      try (Scanner scanner = new Scanner(new FileInputStream("photo-urls.txt"))) {
          for (int i = 0; i < lineNumber && scanner.hasNextLine(); i++) {
              photoURL = scanner.nextLine();
          }
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      return photoURL;
  }

   /**
      Send a static resource to the client.
      <p>
      You do NOT need to make any changes to this method.
   */
   public void doStaticGet(HttpRequest req,
                           HttpResponse res)
   {
      System.out.println("SERVER: doStaticGet: ");

      File file = new File(server.serverRoot + req.resourceName);
      if ( ! file.exists() || ! file.canRead() )
      {
         if (! file.exists())
         {
            sendErrorResponse(req, res,
                "404", "Not Found",
                "The requested URL "+req.resourceName+" was not found on this server.");
         }
         else
         {
            sendErrorResponse(req, res,
                "403", "Forbidden",
                "The requested URL "+req.resourceName+" is not readable.");
         }
         return;  // Finished with this request.
      }
      if ( file.isDirectory() )
      {
         // Check for existence of an index.html file.
         final File file2 = new File(server.serverRoot + req.resourceName + "index.html");
         if ( file2.exists() && file2.canRead() )
         {
            req.resourceName = req.resourceName + "index.html";
            file = new File(server.serverRoot + req.resourceName);
         }
         else
         {
            sendErrorResponse(req, res,
                "400", "Bad Request",
                "The requested URL, "+req.resourceName+", does not contain a file name.");
            return;  // Finished with this request.
         }
      }

      final int numOfBytes = (int)file.length();
      try
      {
         final FileInputStream inFile = new FileInputStream(file);
         final byte[] fileInBytes = new byte[numOfBytes];
         inFile.read(fileInBytes);

         // Add the response line.
         res.setResponseLine("HTTP/1.1 200 OK");
         // Add the response headers.
         final String date = OffsetDateTime.now(ZoneOffset.UTC)
                               .format(DateTimeFormatter.RFC_1123_DATE_TIME);
         res.addHeader("Date: " + date);
         res.addHeader("Server: HttpApplicationServer.java");
         res.addHeader("Connection: close");
         final String contentType;
         if ( req.resourceName.endsWith(".html") || req.resourceName.endsWith(".htm") )
            contentType = "text/html";
         else if ( req.resourceName.endsWith(".css") )
            contentType = "text/css";
         else if ( req.resourceName.endsWith(".js") )
            contentType = "application/javascript";
         else if ( req.resourceName.endsWith(".jpg") || req.resourceName.endsWith(".jpeg") )
             contentType = "image/jpeg";
         else if ( req.resourceName.endsWith(".gif") )
            contentType = "image/gif";
         else if ( req.resourceName.endsWith(".png") )
             contentType = "image/png";
         else  // default content type
            contentType = "text/plain";
         res.addHeader("Content-Type: " + contentType);
         res.addHeader("Content-Length: " + numOfBytes);

         // Add the entity body.
         if ( ! req.requestMethod.equals("HEAD") )
         {
            res.entityBody = fileInBytes;
         }
      }
      catch (FileNotFoundException e)
      {
         System.out.println("SERVER: doStaticGet: Unable to find file.");
         System.out.println( e );
      }
      catch (IOException e)
      {
         System.out.println("SERVER: doStaticGet: Unable to read file.");
         System.out.println( e );
      }
   }


   /**
      The main() method configures the server using
      the httpserver.properties file and command-line
      arguments.
   */
   public static void main(String[] args)
   {
      String serverRoot = "public_html"; // default value
      int portNumber = 8080;             // default value

      if (0 < args.length) portNumber = Integer.parseInt(args[0]);
      if (1 < args.length) serverRoot = args[1];

      // Instantiate and start up an instance of the server.
      new HttpApplicationServer(portNumber,
                                serverRoot,
                                new Hw4()); // HTTP methods
   }
}
