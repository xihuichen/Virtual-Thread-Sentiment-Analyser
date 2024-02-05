package ie.atu.sw;
/**
 * @author xihui chen
 * @version 1.0
 * @since 1.8
 * The Menu class displays a menu for the console-based UI, it is using the ConsoleColour class to control color displayed.
 * To make this class re-usable, future development can include a configure method that takes input params determine what options to display
 * in the menu, hence when user chose an option in the main menu this class can be called again to display a sub-menu
 */

public class Menu {
	public static void main() throws Exception { /** The complexity of this main() method is O(1)
													* Reason: there's no loop within this main() function, just printing things.
													* Thus it is not dependent on the number of elements, the time complexity is constant
													*/
		
	
	System.out.println(ConsoleColour.WHITE); 
	System.out.println("************************************************************");
	System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
	System.out.println("*                                                          *");
	System.out.println("*             Virtual Threaded Sentiment Analyser          *");
	System.out.println("*                                                          *");
	System.out.println("************************************************************");
	System.out.println("(1) Specify a Text File");
	System.out.println("(2) Specify a URL");
	System.out.println("(3) Specify an Output Directory (default: ./out+originalFileName.txt)");
	System.out.println("(4) Configure Lexicons");
	System.out.println("(5) Execute, Analyse and Report");
	System.out.println("(6) Optional Extras...");
	System.out.println("(7) Quit");
	
	//Output a menu of options and solicit text from the user
	System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
	System.out.print("Select Option [1-7]>");
	System.out.println();

}
}