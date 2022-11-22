import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.io.IOException;
import java.text.DecimalFormat;
import java.lang.management.*;

/*

  Author: Taher Patanwala
  Email: tpatanwala2016@my.fit.edu
  Pseudccode: Philip Chan

  Usage: EvalSmartWord oldMessageFile newMessageFile 

  Description:

  The goal is to evaluate SmartWord.  The program simulates keystokes
  from a user by reading in newMessageFile, asks for guesses from
  SmartWord, provides feedback on correctness, and measures
  performance.  SmartWord is provided with oldMessageFile for
  initialization.

     a.  Pseudocode for simulating user keystokes.

     SmartWord smartWord = new SmartWord(wordFile) // a list of English words
     smartWord.processOldMessages(oldMessageFile)  // old messages that the system has seen

     while not end of newMessageFile // to simulate new messages being typed in
       while not end of line
         if a word has more than 1 letter
           while not end of word and incorrect guesses
   	     guesses = smartWord.guess(letter, letterPosition, wordPosition) 
	     measure performance for the first 3 guesses (ignore the rest if any)
	     smartWord.feedback(correctGuess, word)

     report performance

     b.  Measuring Performance

         * average percentage of skipped letters for a word

         * average time per guess
     
         * memory usage--before EvalSmartWord exits.

	 * overall score--accuracy/sqrt(time * memory)  
 */

public class EvalSmartWord
{
    /*

    */
    public static void main(String[]args) throws IOException{

	if (args.length != 3) 
	    {
		System.err.println("Usage: EvalSmartWord wordFile oldMessageFile newMessageFile");
		System.exit(-1);
	    }

	// for getting cpu time
	ThreadMXBean bean = ManagementFactory.getThreadMXBean();        
	if (!bean.isCurrentThreadCpuTimeSupported())
	    {
		System.err.println("cpu time not supported, use wall-clock time:");
                System.err.println("Use System.nanoTime() instead of bean.getCurrentThreadCpuTime()");
		System.exit(-1);
	    }
	    
        //Preprocessing in SmartWord
	System.out.println("Preprocessing in SmartWord...");
        long startPreProcTime = bean.getCurrentThreadCpuTime();
        SmartWord sw = new SmartWord(args[0]);
        sw.processOldMessages(args[1]);
        long endPreProcTime = bean.getCurrentThreadCpuTime();

	// report time and memory spent on preprocessing
        DecimalFormat df = new DecimalFormat("0.####E0");
	System.out.println("cpu time in seconds (not part of score): "  + 
			   df.format((endPreProcTime - startPreProcTime)/1E9));
        Runtime runtime = Runtime.getRuntime();
	runtime.gc();
        System.out.println("memory in bytes (not part of score): " + peakMemoryUsage());


        File file = new File(args[2]);
        Scanner input = new Scanner(file);
        
        double totalPercSkipped = 0.0;
        double totalWords = 0.0;
        double totalGuessess = 0.0;
        double totalElapsedTime = 0.0;
        
	System.out.println("SmartWord is guessing...");
        //Perform operations for each line in the file
        while(input.hasNextLine()){
            //Each line is split into words
            String[]words = input.nextLine().replaceAll("\\s+", " ").split(" ");
	    
            //This for loop will go through each and every word
            for(int indexWord=0; indexWord < words.length; indexWord++){
                totalWords++;
                //Remove punctuation from each word
                words[indexWord] = words[indexWord].replaceAll("[^a-zA-Z]", "");
                //words[indexWord] = words[indexWord].replaceAll("[^a-zA-Z]", "").toLowerCase();
                //Stores the number of letters in the word.
                int noOfLettersInWord = words[indexWord].length();
                int indexLetter = 0;
                boolean isCorrectGuess = false;

                //Go through every letter in the word, and stop is a correct guess was made.
                while(indexLetter < noOfLettersInWord && !isCorrectGuess){
                    totalGuessess++;
                    //Record start time of the guess
                    long startTime = bean.getCurrentThreadCpuTime();
                    //Each letter is passed to the SmartWord program to return 3 gussess
                    String[]guesses = sw.guess(words[indexWord].charAt(indexLetter), indexLetter, indexWord);
                    //To calculate the time taken for each guess operation
                    long endTime = bean.getCurrentThreadCpuTime();
                    totalElapsedTime = totalElapsedTime + (endTime - startTime);
                    
                    //Go through the three guesses, to see whether there was a correct guess
                    String correctGuess = null;
                    for(int indexGuess=0; indexGuess < 3; indexGuess++){
                        //If there was a correct guess, call the feedback method and calculate percentage of letters skipped
                        if(words[indexWord].equalsIgnoreCase(guesses[indexGuess])){
                            isCorrectGuess = true;
                            correctGuess = guesses[indexGuess];
                            //Calculates the percentage of letters skipped
                            totalPercSkipped += ((noOfLettersInWord-1-indexLetter)*100)/noOfLettersInWord;
                            break;
                        }
                    }
                    //This is to call feedback
                    //If the letter entered was the last letter in the word, then pass the correct word to the feedback
		    startTime = bean.getCurrentThreadCpuTime();
                    if(indexLetter == noOfLettersInWord - 1)
                        sw.feedback(isCorrectGuess, words[indexWord]);
                    else
                        sw.feedback(isCorrectGuess, correctGuess);
		    endTime = bean.getCurrentThreadCpuTime();
		    totalElapsedTime = totalElapsedTime + (endTime - startTime);

                    //Increment counter to check next letter in the word
                    indexLetter++;
                }
            }
        }
	input.close();
	
        //Calculate the accuracy
        double accuracy = totalPercSkipped/totalWords;
        System.out.printf("Accuracy: %.4f\n",accuracy);
        
        //Convert elapsed time into seconds, and calculate the Average time
        double avgTime = (totalElapsedTime/1.0E9)/totalGuessess;
        
        //To format the Average time upto 4 decimal places.
        //DecimalFormat df = new DecimalFormat("0.####E0"); // moved to near initialization
        System.out.println("Average time per guess in seconds: " + df.format(avgTime));
        
        // Get the Java runtime
        // Runtime runtime = Runtime.getRuntime();  // moved to near initialization
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = peakMemoryUsage();
        System.out.println("Used memory in bytes: " + memory);
        //OverAll Score
        System.out.printf("Overall Score: %.4f\n",accuracy*accuracy/Math.sqrt(avgTime * memory));

	SmartWord sw2 = sw;  // keep sw used to avoid garbage collection of sw
    }


    /*
     * return peak memory usage in bytes
     *
     * adapted from

     * https://stackoverflow.com/questions/34624892/how-to-measure-peak-heap-memory-usage-in-java 
     */
    private static long peakMemoryUsage() 
    {

    List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
    long total = 0;
    for (MemoryPoolMXBean memoryPoolMXBean : pools)
        {
        if (memoryPoolMXBean.getType() == MemoryType.HEAP)
        {
            long peakUsage = memoryPoolMXBean.getPeakUsage().getUsed();
            // System.out.println("Peak used for: " + memoryPoolMXBean.getName() + " is: " + peakUsage);
            total = total + peakUsage;
        }
        }

    return total;
    }

}
