/*


*/

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
   This version of the client will,
     1) Send the server a positive integer indicating the
        number of integer sequences that will follow.
     2) Send the server a sequence of positive integers.
     3) Send the server a negative integer to end the sequence.
     4) Receive back from the server the sum of the sequence.
     5) If not the last sequence, then go back to step 2.
     6) Close the connection to the server.
*/
public class AdditionClient_Hw3_v2
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

         File file = new File("data_v2");
         Scanner scanner = new Scanner(file);

         int numSequences = scanner.nextInt();
         out.println(numSequences);
         out.flush();

         for (int i = 0; i < numSequences; i++) {
             int num;
             do {
                 num = scanner.nextInt();
                 out.println(num);
                 out.flush();
             } while (num >= 0);

             out.println(-1);
             out.flush();
         }

         socket.close();
         System.out.println("CLIENT: Connection closed.");
     } catch (Exception e) {
         System.out.println("CLIENT: Error occurred.");
         e.printStackTrace();
     }
   }
}
