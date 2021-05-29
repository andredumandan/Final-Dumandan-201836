import java.util.Scanner;


public class GameStarter {
    public static void main(String[] args){
        // Scanner input = new Scanner(System.in);
        // System.out.print("Please enter the IP Address: ");
        // String ip = input.next();
        // System.out.print("Please enter the port number: ");
        // int port = input.nextInt();
        // input.close();
        GameFrame g = new GameFrame(800,448);
        // g.setUpServerIP(ip, port);
        g.connectToServer();
        g.setUpGUI();
    }
}
