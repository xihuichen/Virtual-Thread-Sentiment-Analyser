package ie.atu.sw;

/**
 * @author xihui chen
 * @version 2.0
 * @since 1.8 This class stores the input/output directory specified by user. No
 *        default directory was specified. Future enhancement: include a main
 *        method to avail Unit Test for each function.
 * 
 */

public class DirectorySpecification {

	private String inputdir = null;
	private String outputdir = "./out";
	private String inputurl = null;

	public String getInputdir() {
		/**
		 * @param no parameter taken.
		 * 
		 * @return returns the input directory specified by user in this instance, by
		 *         default the input directory is null.
		 * @throws no exception thrown if there's no specified input directory.
		 *            Complexity: O(1) Rationale: returns string value, constant time
		 *            operation
		 */
		return inputdir;
	}

	public String getInputURL() {
		/**
    	 * @param no parameter taken.
		 * 
		 * @return returns the input url specified by user in this instance, by
		 *         default the input url is null.
		 * @throws no exception thrown if there's no specified input url.
		 *            Complexity: O(1) Rationale: returns string value, constant time
		 *            operation
		 * Complexity: O(1) Rationale: returns string value, constant time operation
		 */
		return inputurl;
	}

	public void setInputURL(String inputurl) {
		/**
 		 * @param Takes a string as input
		 * 
		 * @return void method returns nothing, sets the input url to be the user input
		 * @throws no exception thrown if there's no specified url
		 *            Complexity: O(1) Rationale: returns string value, constant time
		 *            operation
		 * Complexity: O(1) Rationale: assigns string value, constant time operation
		 */
		this.inputurl = inputurl;
	}

	public void setInputdir(String inputdir) {
		/**
		 * @param Takes a string as input
		 * 
		 * @return void method returns nothing, sets the input directory to be the user input
		 * @throws no exception thrown if there's no specified input directory
		 *            
		 * Complexity: O(1) Rationale: assigns string value, constant time operation
		 */
		this.inputdir = inputdir;
	}

	public String getOutputdir() {
		/**
         * @param no parameter taken.
		 * 
		 * @return returns the output directory specified by user in this instance, by
		 *         default the output directory is just where this java application source code is stored.
		 * @throws no exception thrown if there's no specified output directory.
		 *            Complexity: O(1) Rationale: returns string value, constant time
		 *            operation
		 * Complexity: O(1) Rationale: returns string value, constant time operation
		 */
		return outputdir;
	}

	public void setOutputdir(String outputdir) {
		/**
 		 * @param Takes a string as input
		 * 
		 * @return void method returns nothing, sets the output directory of the instance to be the user input
		 * @throws no exception thrown if there's no specified directory
		 *            
		 * Complexity: O(1) Rationale: assigns string value, constant time operation
		 */
		this.outputdir = outputdir;
	}
}
