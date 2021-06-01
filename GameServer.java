/**
	
    This is the Server program that contains the ServerSocket. This program sends, receives, and modifies data from two clients for the game.

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
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class GameServer {
    
    private int p1Score,p2Score;
    private int numPlayers, maxPlayers;
    private double p1X, p1Y, p2X, p2Y, p1VSpeed,p1HSpeed,p2VSpeed,p2HSpeed,playerRadius;
    private double puckX,puckY,puckHSpeed,puckVSpeed,puckSize,puckRadius;
    private boolean p1IsHittingPuck,p2IsHittingPuck,p1Scored,p2Scored,resetPlayers,p1PlayAgain,p2PlayAgain;
    private ServerSocket ss;
    private Socket p1Socket;
    private Socket p2Socket;
    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;

    
    /**
     * Initializes the instance fields for the GameServer.
     */
    public GameServer(){
        System.out.println("====== GAME SERVER ======");
        numPlayers = 0;
        maxPlayers = 2;
        puckHSpeed = 0;
        puckVSpeed = 0;
        p1X = 169;
        p1Y = 179;
        p2X = 589;
        p2Y = 179;
        puckX = 382;
        puckY = 182;
        playerRadius=23;
        puckSize=38;
        puckRadius=18;
        p1Score = 0;
        p2Score = 0;
        p1Scored = false;
        p2Scored = false;
        resetPlayers = false;
        try {
            ss = new ServerSocket(50301);
        } catch(IOException ex){
            System.out.println("IOException occured at constructor");
        }
    }

    /**
     * A void method that that creates a socket for each player that connects.  It assigns them a player number which helps determine which player they can control.
     */
    public void acceptConnections(){
        try{
            System.out.println("Waiting for connections...");

            while(numPlayers<maxPlayers){
                Socket s = ss.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player #" + numPlayers + " has connected.");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                if (numPlayers == 1){
                    p1Socket = s;
                    p1ReadRunnable = rfc;
                    p1WriteRunnable = wtc;
                }
                else{
                    p2Socket = s;
                    p2ReadRunnable = rfc;
                    p2WriteRunnable = wtc;
                    p1WriteRunnable.sendStartMsg();
                    p2WriteRunnable.sendStartMsg();
                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);
                    readThread1.start();
                    readThread2.start();
                    Thread writeThread1 = new Thread(p1WriteRunnable);
                    Thread writeThread2 = new Thread(p2WriteRunnable);
                    writeThread1.start();
                    writeThread2.start();
                }
            }

            System.out.println("No longer accepting connections.");
        } catch(IOException ex){
            System.out.println("IOException occured at acceptConnections");
        }
    }

    /**
     * A void methos that sets up a timer that continuously determines the position of the Puck as well as the movement.
     */
    public void setUpPuckMovement(){
        ActionListener a = new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent ae) {
                resetPlayers = false;
                p1Scored = playerScored(1);
                p2Scored = playerScored(2);
                p1IsHittingPuck = isHittingPuck(1);
                p2IsHittingPuck = isHittingPuck(2);

                if(p1PlayAgain == true && p2PlayAgain == true){
                    p1Score = 0;
                    p2Score = 0;
                    p1PlayAgain = false;
                    p2PlayAgain = false;
                }
                
                if (p1IsHittingPuck && p1VSpeed != 0  || p1IsHittingPuck && p1HSpeed != 0){
                    puckVSpeed = (puckVSpeed * (5 - 8) + (2 * 8 * p1VSpeed)) / (8 + 5);
                    puckHSpeed = (puckHSpeed * (5 - 8) + (2 * 8 * p1HSpeed)) / (8 + 5);
                }

                if (p2IsHittingPuck && p2VSpeed != 0  || p2IsHittingPuck && p2HSpeed != 0){
                    puckVSpeed = (puckVSpeed * (5 - 8) + (2 * 8 * p2VSpeed)) / (8 + 5);
                    puckHSpeed = (puckHSpeed * (5 - 8) + (2 * 8 * p2HSpeed)) / (8 + 5); 
                }

                if (puckX+puckSize >= 800 || puckX <= 0){
                    puckHSpeed = puckHSpeed * -1;
                }

                if(puckY+puckSize >= 400 || puckY <= 0){
                    puckVSpeed = puckVSpeed * -1;
                }
                
                if (puckHSpeed > 0 || puckVSpeed > 0){
                    puckVSpeed -= 0.01;
                    puckHSpeed -= 0.01;
                }
  
                if (puckHSpeed < 0 || puckVSpeed < 0){
                    puckVSpeed += 0.01;
                    puckHSpeed += 0.01;
                }
                puckX += puckHSpeed;
                puckY += puckVSpeed;
                
                if(p2Scored==true){
                    p2Score+=1;
                    puckX = 382;
                    puckY = 182;
                    puckVSpeed = 0;
                    puckHSpeed = 0;
                }
                if(p1Scored==true){
                    p1Score+=1;
                    puckX = 382;
                    puckY = 182;
                    puckVSpeed = 0;
                    puckHSpeed = 0;
                }

            }
            
        };
        Timer puckTimer = new Timer(5,a);
        puckTimer.start();
        
    }

    
    /** 
     * Detects collision between a player and the puck.
     * @param i - accepts an integer which is the playerID of the user.
     * @return boolean - returns true if the playerID is hitting the puck.
     */
    public boolean isHittingPuck(int i){
        double radiiSum = playerRadius + puckRadius;
        if (i==1){
            double xDif = p1X - puckX;
            double yDif = p1Y - puckY;
            if ((xDif*xDif + yDif*yDif) <= radiiSum*radiiSum){
                return true;
            }
            return false;
        }
        else{
            double xDif = p2X - puckX;
            double yDif = p2Y - puckY;
            if ((xDif*xDif + yDif*yDif) <= radiiSum*radiiSum){
                return true;
            }
            return false;
        }
    }

    
    /** 
     * Determines if a player's goal has been compromised by the puck.
     * @param i - accepts an integer that determines which goal the puck hit.
     * @return boolean - returns true if the Puck hits the goal of one of the players.
     */
    public boolean playerScored(int i){
        if (i == 2){
            if (puckX < 4 && puckY+18 > 148 && puckY+puckRadius < 254){
                System.out.println("Player 2 Scored");
                resetPlayers = true;
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(puckX+puckSize > 796 && puckY+puckRadius > 148 && puckY+puckRadius < 254){
                System.out.println("Player 1 Scored");
                resetPlayers = true;
                return true;
            }
            else{
                return false;
            }
        }
    }

    /**
     * An inner class that implements Runnable. It creates a thread the continuously reads data from both player clients.
     */
    private class ReadFromClient implements Runnable {
        private int playerID;
        private DataInputStream dataIn;

        public ReadFromClient(int playerID, DataInputStream dataIn) {
            this.playerID = playerID;
            this.dataIn = dataIn;
            System.out.println("RFC" + playerID + "Runnable created");
        }

        @Override
        public void run() {
            try{
                while(true){
                    if(playerID == 1){
                        p1X = dataIn.readDouble();
                        p1Y = dataIn.readDouble();
                        p1HSpeed=dataIn.readDouble();
                        p1VSpeed=dataIn.readDouble();
                        p1PlayAgain=dataIn.readBoolean();
                    }
                    else{
                        p2X = dataIn.readDouble();
                        p2Y = dataIn.readDouble();
                        p2HSpeed=dataIn.readDouble();
                        p2VSpeed=dataIn.readDouble();
                        p2PlayAgain=dataIn.readBoolean();
                    }
                }
            } catch(IOException ex){
                System.out.println("IOException from RFC run()");
                System.exit(0);
            }
            
        }
    }

    /**
     * An inner class that implements Runnable. It continuously writes data to both clients.
     */
    private class WriteToClient implements Runnable {
        private int playerID;
        private DataOutputStream dataOut;

        public WriteToClient(int playerID, DataOutputStream dataOut) {
            this.playerID = playerID;
            this.dataOut = dataOut;
            System.out.println("WTC" + playerID + "Runnable created");
        }

        @Override
        public void run() {
            try{
                while(true){
                    if(playerID == 1){
                        dataOut.writeBoolean(playerScored(1)||playerScored(2));
                        dataOut.writeDouble(p2X);
                        dataOut.writeDouble(p2Y);
                        dataOut.writeDouble(puckX);
                        dataOut.writeDouble(puckY);
                        dataOut.writeInt(p1Score);
                        dataOut.writeInt(p2Score);
                        dataOut.flush();
                    }
                    else{
                        dataOut.writeBoolean(resetPlayers);
                        dataOut.writeDouble(p1X);
                        dataOut.writeDouble(p1Y);
                        dataOut.writeDouble(puckX);
                        dataOut.writeDouble(puckY);
                        dataOut.writeInt(p1Score);
                        dataOut.writeInt(p2Score);
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(8);
                    } catch(InterruptedException ex){
                        System.out.println("InterruptedException from WTC run()");
                        System.exit(0);
                    }
                }
            } catch(IOException ex){
                System.out.println("IOException from WTC run()");
                System.exit(0);
            }
            
        }

        public void sendStartMsg(){
            try{
                dataOut.writeUTF("Both players have connected. Starting the game...");
            }
            catch(IOException ex){
                System.out.println("IOException from sendStartMsg()");
                System.exit(0);
            }
        }
    }




    
    /** 
     * Containes the main method for the server. Implements the methods in order to set up the server.
     * @param args
     */
    public static void main(String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
        gs.setUpPuckMovement();
        
    }

}