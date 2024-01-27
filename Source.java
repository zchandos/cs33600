/*
   When playing with file filters, it is useful to know that "end of file" is
   denoted at the command-line in Windows by Ctrl-Z and by Ctrl-D in Linux.
*/

import java.util.Random;

/**
   This program takes three optional command-line arguments.
<p>
   The first determines how many lines of output there are.
<p>
   The second determines how many random numbers there are on a line of output.
<p>
   The third determines the range of the random numbers.
*/
class Source
{
   public static void main(String[] args)
   {
      final Random random = new Random();

      final int range;
      if (args.length >= 3)
      {
         range = Integer.parseInt( args[2] ); // range for the random integers
      }
      else
      {
         range = 10_000; // default range for the random numbers
      }

      final int lines;
      if (args.length >= 1)
      {
         lines = Integer.parseInt( args[0] ); // fixed number of lines of output
      }
      else
      {
         lines = 1 + random.nextInt(100); // random number of lines of output
      }

      for (int i = 0; i < lines; ++i)
      {
         final int data_points;
         if (args.length >= 2)
         {
            data_points = Integer.parseInt( args[1] ); // fixed number of numbers on this line of output
         }
         else
         {
            data_points = 1 + random.nextInt(20); // random number of numbers on this line of output
         }

         if ( random.nextBoolean() ) // "flip a coin" to decide if there should be leading spaces
         {
            final int spaces = 1 + random.nextInt(20); // random leading spaces
            for (int j = 0; j < spaces; ++j)
            {
               System.out.printf(" ");
            }
         }

         for (int j = 0; j < data_points - 1; ++j)
         {
            final double d = range * random.nextDouble();
            System.out.printf("%.13f", d);
            final int spaces = 1 + random.nextInt(20); // At least once space between numbers,
            for (int k = 0; k < spaces; ++k)           // with some extra random spaces.
            {
               System.out.printf(" ");
            }
         }
         final double d = range * random.nextDouble(); // last number on this line
         System.out.printf("%.13f", d);

         if ( random.nextBoolean() ) // "flip a coin" to decide if there should be trailling spaces
         {
            final int spaces = 1 + random.nextInt(20); // random trailing spaces
            for (int k = 0; k < spaces; ++k)
            {
               System.out.printf(" ");
            }
         }

         System.out.printf("\n");  // end this line
       //System.out.printf("#\n"); // end this line in a way that we can see where it ends
      }
   }
}
