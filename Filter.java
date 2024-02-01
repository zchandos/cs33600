/*
    Course: CS 33600
    Name: Zachary Chandos
    Email: zchandos@pnw.edu
    Assignment: HW1

*/

import java.util.Scanner;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
Class Javadoc.
*/
public class Filter
{
   public static void main(String[] args)
   {

      Scanner scanner = new Scanner(System.in);

      //default values
      int columnCount = 0;
      int maxColumnCount = 3;
      int precision = 13;
      int groupCount = 0;
      int maxGroupCount = 0;
      boolean groups = false;

      //env values
      String envMaxColumnCount = System.getenv("columns");
      String envPrecision = System.getenv("precision");
      String envMaxGroupCount = System.getenv("groups");

      //load properties file
      Properties properties = new Properties();
      try (FileInputStream input = new FileInputStream("filter.properties")) {
          
        properties.load(input);
          maxColumnCount = Integer.parseInt(properties.getProperty("columns", String.valueOf(maxColumnCount)));
          precision = Integer.parseInt(properties.getProperty("precision", String.valueOf(precision)));
          maxGroupCount = Integer.parseInt(properties.getProperty("groups", String.valueOf(maxGroupCount)));
          if(maxGroupCount >= 1){
            groups = true;
          }

      } catch (IOException e) {
    
      }

      //override values
      if (envMaxColumnCount != null) {
          maxColumnCount = Integer.parseInt(envMaxColumnCount);
      }
      if (envPrecision != null) {
          precision = Integer.parseInt(envPrecision);
      }
      if (envMaxGroupCount != null) {
          maxGroupCount = Integer.parseInt(envMaxGroupCount);
      }

      //reads command line args
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
                     columnCount = 0;
                  }
               } 
      }

      scanner.close();
   }
}
