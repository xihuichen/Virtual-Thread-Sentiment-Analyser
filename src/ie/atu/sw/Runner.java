package ie.atu.sw;

import java.util.Scanner;

import java.util.concurrent.ConcurrentSkipListMap;

import static java.lang.System.out;
import java.io.File;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URI;
import java.net.URL;

/**
 * @author xihui chen
 * @version 1.0
 * @since 21 
 * This java application uses console-based menu-driven UI to input
 *        the path to the lexicon(s) that user wish to use and the path to the
 *        Twitter / X users(s) to process, produce a sentiment score for each
 *        tweet or (in future development) by user choice an overall score of
 *        all the tweets.
 * 
 * 
 */
public class Runner {

	private DirectorySpecification fp = new DirectorySpecification(); /* A Runner HAS-A DirectorySpecification
																		 * To specify directory, default no directories
																		 * were specified, so this variable can't be
																		 * static. To reference it in main method
																		 * (static), this needs to be wrapped in a
																		 * class, hence here an object of
																		 * DirectorySpecification is created within
																		 * Runner class.
																		 */
	private ConcurrentSkipListMap<String, Double> lc;
	private String outputchoice;

	public ConcurrentSkipListMap<String, Double> getLc() { /**
															 * Method that gets the saved ConcurrentSkipListMap, if no
															 * other action was taken before evoking this getter, there
															 * will be no lexicon file processed, default map is empty.
															 * 
															 * @param takes no parameter
															 * @return ConcurrentSkipListMap<String,Double> a
															 *         dictionary-like stored result after processing a
															 *         lexicon file, specifies words and their sentiment
															 *         scores.
															 * @throws no exception thrown. Getter for the private mat
															 *            variable stored after the lexicon file
															 *            processed, time complexity O(1)
															 */
		return this.lc;
	}



	private String deliminitor;
	private String OptionAlgo = "SaS";
	private String logdir = "./log.txt";
	private Scanner s; // Prepared for Inputs by users, specify it at the class level to make recursive
// entry scanning
	private boolean keepRunning = true; /*
										 * Prepare the control variable for menu, using this control variable provides
										 * coming back to main menu option each time after one action executed by user,
										 * and also provide an option of quitting the program.
										 */
	private String sbLog = "";/*
								 * The string is trying to log what had been the historical commands user
								 * entered, for future development the log automatically saved even program was
								 * interrupted by exceptions
								 */

	public static void main(String[] args) throws Exception {
		/**
		 * Main method takes String array as input but not used afterwards. Shows in
		 * console a menu for user to choose from, execute different functions of this
		 * application, also provides user an option to quit gracefully
		 * 
		 * @param String[] args not neccessary to include, not used anywhere else
		 *                 afterwards.
		 * @return void method, no return.
		 * @throws See the depending methods for different types of exceptions to be
		 *             thrown.
		 * 
		 *             Complexity: O(n) Rationale: The main loop runs while keepRunning
		 *             is true. The complexity depends on the number of iterations this
		 *             loop makes, which is determined by user input. Assuming n
		 *             iterations, the complexity is O(n).
		 */
		Runner myobject = new Runner(); // Create an instance of the Runner class to sync with user's specified inputs
		myobject.s = new Scanner(System.in);
		while (myobject.keepRunning) {
			Menu.main();

			int choice = Integer.parseInt(myobject.s.next());
			myobject.sbLog += ("User entered choice " + choice + " from main menu");// keep adding user's trace of
			// action into log
			switch (choice) { // Execute user's choice according to the main menu displayed
			case 1 -> myobject.SpecifyInputDir();
			case 2 -> myobject.SpecifyInputURL();
			case 3 -> myobject.SpecifyOutputDir();
			case 4 -> myobject.lexicon();
			case 5 -> myobject.SentimentProcess();
			case 6 -> myobject.Options();
			case 7 -> myobject.setKeepRunning(false);
			default -> out.println("[Error] Invalid Selection");
			}

		}
		myobject.s.close();
		out.println("[INFO] Exiting...Bye!");

	}

	public void setKeepRunning(boolean b) { /**
											 * This method controls whether the menu will be displayed again after
											 * current entry
											 * 
											 * @param Boolean true or false,to kill the program enter false .
											 * @return void method, no return.
											 * @throws no exception thrown. Complexity: O(1) Rationale: This method sets
											 *            a boolean value, which is a constant-time operation.
											 */
		this.keepRunning = b;
	}

	public void lexicon() throws Exception {
		/**
		 * This method promps user to enter a file directory and then process the txt
		 * file in specified directory to save lexicon dictionary to a
		 * ConcurrentSkipListMap
		 * 
		 * @param takes no parameter.
		 * @return void method, nothing returns.
		 * @throws Exception: if user input is not the same as instructed, will throw
		 *                    type mismatch exception. Otherwise, will through exception
		 *                    depending on the configuration of class LexiconConfig, the
		 *                    getMap() method in the created instance of LexiconConfig
		 *                    class. 
		 *Complexity: O(n) 
		 *Rationale: The method invokes
		 *                    LexiconConfig.main, which in turn calls LexiconConfig.go.
		 *                    The complexity is linear with the number of lines in the
		 *                    lexicon file.
		 */
		out.println("Please enter a lexicon file used for sentiment analysis: default my own directory");

		String[] dir = new String[] { "//Users//xihui//Downloads//lexicons//vader.txt", "Y" };
		dir[0] = s.next();
		out.println("Is that file vader? [Y/N]");
		String IsVader = s.next();
		dir[1] = IsVader;
		LexiconConfig temp = new LexiconConfig();
		try {
			temp.main(dir);
			lc = temp.getMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	public void SentimentProcess() throws Exception {
		/**
		 * This method processes txt files in input directory specified by user earlier
		 * (if not specified, will throw error) and output a file containing the
		 * sentiment scores
		 * 
		 * @param Takes no input parameter, all depending variables were specified
		 *              earlier by user.
		 * @return void method, returns no value.
		 * @throws ExceptionType depending on following methods used: -File()
		 *                       -printProgress() -MapwithLexicon() constructor taking
		 *                       this Runner instance as input -FileInputStream()
		 *                       -BufferedWriter() -FileWriter() -MapwithLexicon.go()
		 *                       -URL class
		 * 
		 *Complexity: O(n * k log m) 
		 *Rationale: This method
		 *                       processes each file and each line within those files.
		 *                       The complexity depends on the number of files (n), the
		 *                       number of words per line (k), and the size of the
		 *                       ConcurrentSkipListMap (m). Future enhancement: if this
		 *                       is using virtual thread, the time complexity could be
		 *                       reduced. The reason not using virtual threads
		 *                       processing files is that the structure to process url
		 *                       needs to be further studies.
		 */
		if (fp.getInputdir() != null && fp.getInputURL() == null) {

			File folder = new File(fp.getInputdir()); // Parse all the txt files saved under that folder, below loop is
														// looping
			// through txt files in the folder, encrypt them and then write the encrypted
			// string to txt files
			File[] listoffiles = folder.listFiles();
			for (int i = 0; i < listoffiles.length; i++) {
				// You may want to include a progress meter in you assignment!
				System.out.print(ConsoleColour.YELLOW); // Change the colour of the console text
				// The size of the meter. 100 equates to 100%
				// The loop equates to a sequence of processing steps
				printProgress(i + 1, listoffiles.length); // After each (some) steps, update the progress meter
				// Slows things down so the animation is visible

				File file = listoffiles[i];
				MapwithLexicon mp = new MapwithLexicon(this);
				if (file.isFile() && file.getName().endsWith(".txt")) {
					FileInputStream fis = new FileInputStream(file);
					BufferedWriter writer = new BufferedWriter(new FileWriter(fp.getOutputdir() + file.getName()));
					mp.go(fis, writer);

					writer.close();

				}
			}
		} else if (fp.getInputURL() != null && fp.getInputdir() == null) {

			MapwithLexicon mp = new MapwithLexicon(this);
			URL url = new URI(fp.getInputURL()).toURL();
			BufferedWriter writer = new BufferedWriter(new FileWriter(fp.getOutputdir() + "/outfromurl.txt"));
			mp.go(url.openStream(), writer);
			writer.close();

		} else {
			out.println("Please specify either Input Directory or Input URL, but not both of them");
		}
	}

	public void Options() throws IOException {
		/**
		 * This method allows user to use different extra options available in the
		 * application
		 * 
		 * @param takes no parameter, there will be a scanner taking user's input
		 * @return void method, returns no value
		 * @throws ExceptionType Depending on which option user choose,see the methods
		 *                       SentimentScores() SpecifyOutputResult() ExportLog()
		 *Complexity: O(1) 
		 *Rationale: This method mainly involves simple I/O operations and a switch statement, all of
		 *                       which are constant time operations.
		 */
		out.println("Optional Functionalities:1-2)");
		out.println("1)Specify the sentiment score calculation algorithm");
		out.println("2)Specify the desired output file format");
		out.println("3)Export the log");
		int choiceoption = s.nextInt();
		switch (choiceoption) {
		case 1 -> SentimentScores();
		case 2 -> SpecifyOutputResult();
		case 3 -> ExportLog();
		default -> out.println("[Error] Invalid Selection");
		}

	}

	public void SentimentScores() {
		/**
		 * Complexity: O(1) Rationale: This method includes a few I/O operations and
		 * conditional statements, which are all constant-time operations.
		 */
		out.println("Specify which sentiment score aggregation for text:");
		out.println("1)Score and Sum (SaS): Score all of the individual words in a text and then sum up their values");
		out.println(
				"2)Score from Total (SfT): Subtract the total number of negative words from the total number of positive words and divide the result by the total number of words");
		int choicescore = s.nextInt();
		switch (choicescore) {
		case 1 -> setOptionAlgo("SaS");
		case 2 -> setOptionAlgo("SfT");
		default -> out.println("Invalid Selection, proceed with default option Score and Sum (SaS)");
		}

	}

	public void setOptionAlgo(String s) {
		/**
		 * This method sets the algorithm user chose aggregating each word's sentiment
		 * score to get an overall sentiment score for the tweet
		 * 
		 * @param String s Currently support two choices "SaS": Score and Sum and "SfT":
		 *               Score from Total
		 * @return void method, no return.
		 * @throws No exception thrown. 
		 * Complexity: O(1) 
		 * Rationale: This method involves
		 *            a simple assignment operation, which is constant time.
		 */
		this.OptionAlgo = s;
	}

	public void SpecifyOutputResult() {
		/**
		 * This method allows user to select from either getting an overall result for
		 * each txt document or get sentiment scores for each line in the txt. Current
		 * version only support one choice, leave the other choice for future version
		 * development
		 * 
		 * @param Takes no parameter.
		 * @return void method, no return.
		 * @throws depending on setOutputChoice() method exception thrown. 
		 * Complexity:O(1) 
		 * Rationale: Consists of basic I/O operations and
		 *                   conditional logic, which are constant time operations.
		 */
		out.println(
				"Specify what sentiment analysis output you want: by default, the output is a txt file with original tweet and a specified deliminitor then the sentiment score of that tweet");
		out.println("1)One sentiment score for one txt file");
		out.println("2)One sentiment score for each line in original txt file (default)");
		int choicesout = s.nextInt();
		switch (choicesout) {
		case 1 -> setOutputChoice("Summary");
		case 2 -> setOutputChoice("PerLine");
		default -> out.println("Invalid Selection, proceed with default option 2)");
		}

	}

	public void setOutputChoice(String s) {
		/**
		 * This method stores user's choice of output file to current object
		 * 
		 * @param String s Currently supporting two entries "Summary" or "PerLine".
		 * @return void method no return.
		 * @throws depending on SpecifyDeliminitor() method exception thrown.
		 *Complexity: O(1) 
		 *Rationale: This method performs assignment and a simple conditional check, 
		 *both are constant time operations.*/
		 
		this.outputchoice = s;
		if (s == "PerLine") {
			SpecifyDeliminitor();
		}
	}

	public void SpecifyDeliminitor() {
		/**
		 * This method creates an user interface for user to enter the deliminitor used
		 * to separate original text from the processed sentiment score * @param no
		 * parameter needed, scanner was initialized in the class Runner and used here
		 * to take user input.
		 * 
		 * @return void method no return.
		 * @throws depending on the method setDeliminitor() and scanner.next() exception
		 *                   thrown. 
		 * Complexity: O(1) 
		 * Rationale: This method involves reading input and assigning it, which are constant time operations.*/
		 
		out.println("Enter the deliminitor:");
		setDeliminitor(s.next());}

	public void setDeliminitor(String s) {
		/**This method sets deliminitor used in the output file separating the original text and processed sentiment score
		   * @param String s the deliminitor between original text and processed sentiment score
		   * @return void method, returns no value
		   * @throws No exception thrown.
		 * Complexity: O(1) 
		 * Rationale: Simple assignment operation, a constant time operation.*/
		this.deliminitor = s;}

	public String getDeliminitor() {
		/** Getter for the class object stored deliminitor used to separate original text and processed sentiment score
		 * @param takes no parameters
		 * @return returns string, if it is not set by user the default value is null
		 * @throws No exception thrown 
		 * Complexity:o(1)
		 * Rational:Simple getting string operation, constant time complexity */
		return this.deliminitor;}

	public String getOptionAlgo() {
		/**Getter for the Runner class object stored sentiment score aggregation option chosen by user
		 * @param No parameter input.
		 * @return String Abbreviation specifying which option chosen by user
		 * @throws No exception thrown.
		 * Complexity: O(1) 
		 * Rationale: Returns a string value, a constant time operation. */
		return this.OptionAlgo;
	}

	public void ExportLog() throws IOException {
		/**This method exports the log recorded each step user's choice to a specified directory
		 * @param No parameter taken.
		 * @return void method no return value.
		 * @throws IOException user will be promped to specify an output directory for the log, if FileWriter can't write to the epecified output directory there will be IOException thrown.
		 * Complexity: O(1) 
		 * Rationale: Writing the log to a file; whilst the log is just a string, it is a constant time operation */
		out.println("Please enter the path you want the log file to be exported: default: ./log.txt");
		String logdir = s.next() == null ? getLogdir() : s.next();
		try (FileWriter wrlog = new FileWriter(logdir)) {
			wrlog.write(sbLog);
		}
		out.println("Log created and saved as " + logdir);

	}

	public void SpecifyInputDir() {
		/**This method promps user to enter a directory containing all the input files
		 * @param does not take any input parameters
		 * @return void method, does not return any value
		 * @throws NO exception thrown.
		 * Complexity: O(1) 
		 * Rationale: Involves basic I/O and assignment, which are constant time operations.*/
		out.println("[INFO] Specify the file directory of input");

		out.println("Enter input directory>");
		String sdir = s.next();

		try {
			fp.setInputdir(sdir);
			out.println("[INFO]  " + sdir + "Set as input directory");
			sbLog += (sdir + "Set as input directory");
		} catch (Exception e) {
			out.println("[INFO] Cannot find directory " + sdir);
			sbLog += ("attemp to set " + sdir + " as input directory failed");
		}

	}

	public void SpecifyInputURL() {
		/**This method promps user to enter an url as input for processing the files
		 * @param: no parameters taken @return void method does not return anything @throws no exception thrown
		 * Complexity: O(1) 
		 * Rationale: Similar to SpecifyInputDir(), involves I/O and assignment operations.*/

		out.println("[INFO] Specify the file url of input");

		out.println("Enter input URL>");
		String sdir = s.next();

		try {
			fp.setInputURL(sdir);
			out.println("[INFO]  " + sdir + "Set as input url");
			sbLog += (sdir + "Set as input url");
		} catch (Exception e) {
			out.println("[INFO] Cannot find url " + sdir);
			sbLog += ("attemp to set " + sdir + " as input url failed");
		}

	}

	private void SpecifyOutputDir() {
		/**This method promps user to enter a path for exporting the output files
		 * @param: no parameters taken @return void method does not return anything @throws no exception thrown
		 * Complexity: O(1) 
		 * Rationale: Similar to SpecifyInputDir(), involves I/O and
		 * assignment operations. */
		out.println("[INFO] Specify the output directory");
		out.println("Enter desired output directory>");
		String sdirout = s.next();

		try {
			fp.setOutputdir(sdirout);
			out.println("[INFO] Output files will be saved under " + sdirout);

			sbLog += (sdirout + "Set as output directory");
		} catch (Exception e) {
			out.println("[INFO] Cannot find directory " + sdirout);
			sbLog += ("attemp to set " + sdirout + " as output directory failed");
		}
	}


	public static void printProgress(int index, int total) {
		/**
		 * 	
	 * Terminal Progress Meter ----------------------- You might find the progress
	 * meter below useful. The progress effect works best if you call this method
	 * from inside a loop and do not call System.out.println(....) until the
	 * progress meter is finished.
	 * 
	 * Please note the following carefully:
	 * 
	 * 1) The progress meter will NOT work in the Eclipse console, but will work on
	 * Windows (DOS), Mac and Linux terminals.
	 * 
	 * 2) The meter works by using the line feed character "\r" to return to the
	 * start of the current line and writes out the updated progress over the
	 * existing information. If you output any text between calling this method,
	 * i.e. System.out.println(....), then the next call to the progress meter will
	 * output the status to the next line.
	 * 
	 * 3) If the variable size is greater than the terminal width, a new line escape
	 * character "\n" will be automatically added and the meter won't work properly.
	 * 
	 * 
	 *
		 * Complexity: O(1) Rationale: This method prints progress, which is a constant
		 * time operation, assuming the console output does not significantly affect the
		 * time complexity.
		 */
		if (index > total)
			return; // Out of range
		int size = 50; // Must be less than console width
		char done = '█'; // Change to whatever you like.
		char todo = '░'; // Change to whatever you like.

		// Compute basic metrics for the meter
		int complete = (100 * index) / total;
		int completeLen = size * complete / 100;

		/*
		 * A StringBuilder should be used for string concatenation inside a loop.
		 * However, as the number of loop iterations is small, using the "+" operator
		 * may be more efficient as the instructions can be optimized by the compiler.
		 * Either way, the performance overhead will be marginal.
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append((i < completeLen) ? done : todo);
		}

		/*
		 * The line feed escape character "\r" returns the cursor to the start of the
		 * current line. Calling print(...) overwrites the existing line and creates the
		 * illusion of an animation.
		 */
		System.out.print("\r" + sb + "] " + complete + "%");

		// Once the meter reaches its max, move to a new line.
		if (done == total)
			System.out.println("\n");
	}

	public String getLogdir() {
		/**This method returns pre-specified log directory for this object
		 * @param: no parameters taken @return String the specified output log directory @throws no exception thrown
		 * Complexity: O(1) Rationale: Returns a string value, a constant time operation.*/
		return this.logdir;
	}

	public void setLogdir(String logdir) {
		/**This method sets a directory to store the log
		 * @param: String, the path that specifies log directory @return void method does not return anything @throws no exception thrown
		 * Complexity: O(1) 
		 * Rationale: Assigns a string value, a constant time operation */
		this.logdir = logdir;
	}

	public String getOutputChoice() {
		/**This method returns pre-specified output directory for this object
		 * @param: no parameters taken @return String the specified output directory @throws no exception thrown
		 * Complexity: O(1) Rationale: Returns a string value, a constant time operation.*/
		return outputchoice;
	}

}