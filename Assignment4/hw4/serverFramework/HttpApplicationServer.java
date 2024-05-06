/*

*/

package serverFramework;

import java.io.IOException;
import java.net.*;

/**
   This class defines a "framework" for implementing
   simple web applications. Provide this class with
   a reference to an implementation of the HttpMethods
   interface, and this framework will use that
   implementation to run your web app.
*/
public class HttpApplicationServer
{
   private static final int SERVER_PORT = 8080; // Should be above 1023.
   private static final String SERVER_ROOT = "public_html";

   public final int portNumber;
   public final String serverRoot;
   public final HttpMethods httpMethods;

   /**
      This constructor intializes the server's port number
      and root folder. Then it creates the server socket
      and begins listening on it. When a client connection
      is established, it is dispatched to a ClientConnection
      object.
   */
   public HttpApplicationServer(int portNumber,
                                String serverRoot,
                                HttpMethods httpMethods)
   {
      this.portNumber = portNumber;
      this.serverRoot = serverRoot;
      this.httpMethods = httpMethods;

      // Give the HTTP method implementations
      // a reference to this server object.
      httpMethods.setServer(this);

      // Get this server's process id number (PID). This helps
      // to identify the server in TaskManager or TCPView.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("SERVER: Process ID number (PID): " + pid );

      // Get the name and IP address of the local host and
      // print them on the console for information purposes.
      try
      {
         final InetAddress address = InetAddress.getLocalHost();
         System.out.println("SERVER Hostname: " + address.getCanonicalHostName() );
         System.out.println("SERVER IP address: " +address.getHostAddress() );
         System.out.println("SERVER Using port no. " + portNumber);
         System.out.println("SERVER root: " + serverRoot);
      }
      catch (UnknownHostException e)
      {
         System.out.println("SERVER: Unable to determine this host's address.");
         System.out.println( e );
      }

      // Create the server's listening socket.
      ServerSocket serverSocket = null;
      try
      {
         serverSocket = new ServerSocket(portNumber);
         System.out.println("SERVER online:");
      }
      catch (IOException e)
      {
         System.out.println("SERVER: Error creating network connection.");
         e.printStackTrace(System.out);
         System.exit(-1);
      }

      int clientCounter = 0;
      while (true) // Run forever, accepting and servicing clients.
      {
         // Wait for an incoming client request.
         try
         {
            System.out.println("\nSERVER: Waiting for client " + (1+clientCounter) + " to connect.");
            final Socket clientSock = serverSocket.accept();

            ++clientCounter;
            // Log information about the connection that was just established.
            final InetAddress clientIP = clientSock.getInetAddress();
            final String clientHostAddress = clientIP.getHostAddress();
            final String clientHostName = clientIP.getCanonicalHostName();
            final int clientPortNumber = clientSock.getPort();
            System.out.println();
            System.out.println("SERVER: Client " + clientCounter + ": "
               + clientHostName + " at " + clientHostAddress + ":" + clientPortNumber);

            new ClientConnection(clientSock,
                                 clientCounter,
                                 this);
         }
         catch (IOException e)
         {
            System.out.println("SERVER: Error communicating with client (Client no. " + clientCounter + ")");
            System.out.println( e );
         }
      }
   }
}
