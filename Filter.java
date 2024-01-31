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
      int maxColumnCount = 3;
      int precision = 13;
      int groupCount = 0;
      int maxGroupCount = 0;
      boolean groups = false;

      if (args.length > 0) {
         try {
             maxColumnCount = Integer.parseInt(args[0]);

             if(args.length > 1){
               precision = Integer.parseInt(args[1]);
             }
             if(args.length > 2){
               groups = true;
               maxGroupCount = Integer.parseInt(args[2]);
             }

         } catch (NumberFormatException e) {
             System.err.println("Invalid command-line arguments, using defaults.");
         }
     }

      while (scanner.hasNextDouble()){
         
         double number = scanner.nextDouble();

         String format = String.format("%%%d.%df  ", precision + 5, precision);
         System.out.printf(format, number);
     
         columnCount++;
         groupCount++;

         if(columnCount == maxColumnCount){
               System.out.println();
      
               columnCount = 0;
         }
         if(groups){
            if(groupCount == maxGroupCount){
                     System.out.println();
                     System.out.println();
                     groupCount = 0;
                  }
               } 
      }

      scanner.close();
   }
}
