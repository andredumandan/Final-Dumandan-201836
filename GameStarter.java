/**
	
    This object containes the main method of the Game. It accepts an input for the IP Address and the port number.

	@author Andre Matthew G. Dumandan (201836)
	@version May 16, 2021
**/

/*
	I have not discussed the Java language code in my program 
	with anyone other than my instructor or the teaching assistants 
	assigned to this course.

	I have not used Java language code obtained from another student, 
	or any other unauthorized source, either modified or unmodified.

	If any Java language code or documentation used in my program 
	was obtained from another source, such as a textbook or website, 
	that has been clearly noted with a proper citation in the comments 
	of my program.
*/
import java.util.Scanner;

public class GameStarter {
    public static void main(String[] args){
        System.out.println("====== GAME CLIENT ======");
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the IP Address: ");
        String ip = input.next();
        System.out.print("Please enter the port number: ");
        int port = input.nextInt();
        input.close();
        GameFrame g = new GameFrame();
        g.setUpServerIP(ip, port);
        g.connectToServer();
        g.setUpGUI();
    }
}
