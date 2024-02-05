package ie.atu.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;

public class MapwithLexicon {

	Runner myobject;

	public MapwithLexicon(Runner myobject) {
		/**This consctructer takes a runner object as input, constructs a MapwithLexicon
		 * class that can use the runner class object's specified constants 
		 * @param Runner class object as input.
		 * @return No return, constructor.
		 * @throws No exception thrown. Complexity: O(1) Rationale: The constructor is
		 *            simply assigning a reference to an existing object, which is a
		 *            constant-time operation. */
		this.myobject = myobject;
		// TODO Auto-generated constructor stub
	}

	public void go(InputStream in, BufferedWriter writer) throws Exception {
		/**This go method starts virtual threads to process file and write the output to a txt file
		 * @param InputStream in: created by either reading a file path or an URL.
		 * @return void method, does not return anything, output will be stored in runner class object inputted.
		 * @throws ExceptionType dependent on the newvirtualthreadpertaskexecutor() and process()
		 * Complexity: O(n) 
		 * Rationale: This method reads lines from an input stream,
		 * where n is the number of lines. It uses a virtual thread for each line, but
		 * the overall time complexity is still dependent on the number of lines because
		 * each line is processed independently. The concurrent processing does not
		 * change the overall count of operations, though it can improve real-time performance. */
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			new BufferedReader(new InputStreamReader(in)).lines().forEach(text -> pool.execute(() -> {
				try {

					process(text, writer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}));}}

	public void process(String text, BufferedWriter writer) throws Exception {
		/**
		 * This method does not parse the string input (in case there're information
		 * about sentiment that could be recognized by vader. it takes the input text,
		 * process with specified lexicon, use specified aggregation method to get an
		 * overall sentiment score for the input, and writes it to an output file
		 * @param: no parameters taken
		 * @return Void method returns nothing @throws throws exceptions I don't know which types
		 * Complexity: O(k log m) Rationale: In this method, the string (representing a
		 * line of text) is split into words (let's say k words). For each word, it
		 * checks the ConcurrentSkipListMap (of size m) for a corresponding score. The
		 * get operation in a ConcurrentSkipListMap has a time complexity of O(log m).
		 * Therefore, the total complexity is O(k log m) for k words. The loop iterates
		 * over each word in the line. The get operation for each word from the
		 * ConcurrentSkipListMap is O(log m). Additional operations inside the loop
		 * (like arithmetic operations) are O(1). The complexity of splitting the string
		 * is O(k), where k is the number of words. However, since this is overshadowed
		 * by the O(k log m) complexity of the loop, it's not the dominant factor */

		String[] words = text.split("\\s+"); // Split the line into words
		Double scores = 0.0;
		ConcurrentSkipListMap<String, Double> lc = myobject.getLc();
		String OptionAlgo = myobject.getOptionAlgo();
		String deliminitor = myobject.getDeliminitor();

		for (String word : words) {
			int linewords = 0;
			int positive = 0;
			int negative = 0;
			var temp = lc.get(word); /*
										 * The time complexity of the get() operation in a ConcurrentSkipListMap in Java
										 * is O(log n), where n is the number of entries in the map. This is because
										 * ConcurrentSkipListMap is implemented as a skip list, and lookup operations in
										 * a skip list have a time complexity of O(log n)
										 */
			Double score = temp == null ? 0.0 : temp;
			if (OptionAlgo == "SaS") {
				scores += score;
			} else {
				if (score > 0) {
					positive++;
				} else if (score < 0) {
					negative++;
				}
				;
				linewords++;

			}
			scores = OptionAlgo == "SaS" ? scores : (positive - negative) / linewords;

		}
		writer.write(text + deliminitor + scores);
		writer.newLine();
	}
}

//	public static void main(InputStream in) throws Exception {
//		new MapwithLexicon().go(in);
//		out.println("Lines: " + line);
//	}
