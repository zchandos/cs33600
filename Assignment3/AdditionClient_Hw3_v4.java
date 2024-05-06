/*


*/

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
   This version of the client will,
     1) Either send the server a negative integer and go to step 6,
        or go to step 2.
     2) Send the server a sequence of positive integers.
     3) Send the server a negative integer to end the sequence.
     4) Receive back from the server the sum of the sequence.
     5) Go to step 1.
     6) Close the connection to the server.
*/
public class AdditionClient_Hw3_v4
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   public static void main (String[] args)
   {
      Socket          socket = null;
      BufferedReader  in = null;
      PrintWriter     out = null;

      final String hostName;
      if (args.length > 0)
      {
         hostName = args[0];
      }
      else
      {
         hostName = "localhost";
      }

      final int portNumber;
      if (args.length > 1)
      {
         portNumber = Integer.parseInt(args[1]);
      }
      else
      {
         portNumber = SERVER_PORT;
      }

      // Get this client's process id number (PID). This helps
      // to identify the client in the server's transcrip.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("CLIENT: Process ID number (PID): " + pid );

      // Make a connection to the server
      try
      {
         System.out.println("CLIENT: connecting to server: " + hostName + " on port " + portNumber );
         socket = new Socket(InetAddress.getByName(hostName), portNumber);

         in = new BufferedReader(
                  new InputStreamReader(
                       socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream());
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot connect to server.");
         //System.out.println( e );
         e.printStackTrace();
         System.exit(-1);
      }

      // Implement the appropriate client/server protocol.
      try {

         File file = new File("data_v4");
         Scanner scanner = new Scanner(file);

         while (scanner.hasNextLine()) {
             String line = scanner.nextLine();
             if (line.equals("0")) break;
             out.println(line);
             out.flush();
         }
         out.println(-1); 
         out.flush();

         scanner.close();
     } catch (FileNotFoundException e) {
         System.out.println("CLIENT: File not found.");
         e.printStackTrace();
     } catch (IOException e) {
         System.out.println("CLIENT: Error occurred while reading file.");
         e.printStackTrace();
     }

     try {
         socket.close();
         System.out.println("CLIENT: Connection closed.");
     } catch (IOException e) {
         System.out.println("CLIENT: Error occurred while closing connection.");
         e.printStackTrace();
     }



   }
}
