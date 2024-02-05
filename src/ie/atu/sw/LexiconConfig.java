package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;

/**
 * @author xihui chen
 * @version 1.0
 * @since 21 This is a class taking txt comma deliminited lexicon files and
 *        stores it into a ConcurrentSkipListMap. The result
 *        ConcurrentSkipListMap<String,Double> can be retrieved using a getter
 *        Needs Java21 for using the newVirtualThreadPerTaskExecutor
 * 
 */
public class LexiconConfig {

	private ConcurrentSkipListMap<String, Double> lexicon = new ConcurrentSkipListMap<>();

	public void go(String dir) throws Exception {
		/**
		 * This method uses virtual thread to process input txt file's each line using
		 * process() method 
		 * @param takes a string as input, the string should be the
		 * txt file location path.
		 * 
		 * @return Void method, returns no value, the processed result would be stored
		 *         in a ConcurrentSkipListMap, see process().
		 * @throws Types of exception would be specified in the documentation of each
		 *               method used in this method:
		 *               Executors.newVirtualThreadPerTaskExecutor().execute(),
		 *               Paths.get();
		 *               Files.lines();
		 *               process();
		 *               
		 * Complexity: O(n) 
		 * Rationale: This method processes each line of a file concurrently. The overall time
		 *               complexity depends on the number of lines (n) in the file.
		 *               While concurrent processing can improve performance, the
		 *               worst-case complexity remains linear with respect to the number
		 *               of lines.
		 */
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(dir)).forEach(text -> pool.execute(() -> process(text)));
		}
	}

	public void process(String text) {
		/**
		 * This method processes input text by reading in the text line, split the text
		 * by comma, then store the String, Double into a concurrentSkipListMap * @param
		 * String text a line of text read from the input txt file.
		 * 
		 * @return Void method, returns nothing.
		 * @throws There will be exception thrown. Complexity: O(m), where m is the
		 *               number of commas in the text Rationale: The method's complexity
		 *               depends on the number of times it splits the text and processes
		 *               the splits. This is linear with respect to the number of commas
		 *               in the text.
		 */
		int commaCount = (int) text.chars().filter(ch -> ch == ',').count();
		if (commaCount == 1) {
			try {
				lexicon.put(text.split(",")[0], Double.parseDouble(text.split(",")[1]));
			} catch (Exception e) {
			}
			;
		} else {
			String[] splitted = text.split(",");
			String[] splitted1 = Arrays.copyOf(splitted, commaCount);
			String rejoin = String.join(",", splitted1);
			lexicon.put(rejoin, Double.parseDouble(text.split(",")[commaCount]));
		}
	}

	public ConcurrentSkipListMap<String, Double> getMap() {
		/**
		 * This get method returns the ConcurrentSkipListMap produced by processing
		 * lexicon txt file 
		 * @param takes no parameters as input.
		 * 
		 * @return returns a ConcurrentSkipListMap<String,Double> where the string would
		 *         be the words contain in the lexicon dictionary and the Double being
		 *         the corresponding sentiment scores.
		 * @throws no exception thrown. Complexity: O(1) Rationale: Returns a reference
		 *            to an object, a constant-time operation.
		 */
		return this.lexicon;
	}

	public void main(String[] args) throws Exception {
		/**
		 * This main method takes an array of string as input and creates & stores a
		 * concurrentskiplistmap which can be retrieved using a get method.
		 * @param
		 * String[] First element of this string array should be a path of the lexicon
		 * txt file.
		 * 
		 * @return This is a void method, nothing returned, need to invoke get() to
		 *         retrieve the result of this method.
		 * @throws ExceptionType see go(). Complexity: O(n) Rationale: This complexity
		 *                       matches that of the go method since it's the primary
		 *                       operation performed here.
		 */
		String dir = args[0];
//		String isVader=args[1];
		this.go(dir);

	}

}
