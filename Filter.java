/*


*/

import java.util.Scanner;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**

*/
public class Filter
{
   public static void main(String[] args)
   {

      Scanner scanner = new Scanner(System.in);

      int columnCount = 0;
      int maxColumnCount = 0;

      if (args.length > 0) {
         try {
             maxColumnCount = Integer.parseInt(args[0]);
         } catch (NumberFormatException e) {
             System.err.println("Invalid argument for number of columns. Using default.");
         }
     }

      while (scanner.hasNextDouble()){
         
         double number = scanner.nextDouble();

         System.out.printf("%16.13f  ", number);

         columnCount++;

         if(columnCount == maxColumnCount){
            System.out.println();
   
            columnCount = 0;
         }
      
      }

      scanner.close();
   }
}
