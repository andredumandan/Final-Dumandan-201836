import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class GameServer {
    
    private ServerSocket ss;
    private Socket p1Socket;
    private Socket p2Socket;
    private int numPlayers;
    private int maxPlayers;
    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;
    private double p1X, p1Y, p2X, p2Y,puckX,puckY,puckHSpeed,puckVSpeed,p1VSpeed,p1HSpeed,p2VSpeed,p2HSpeed,playerSize,puckSize,playerRadius,puckRadius;
    String dataFromClient;
    private int p1Score,p2Score;
    boolean p1IsHittingPuck,p2IsHittingPuck,p1Scored,p2Scored,resetPlayers;
    

    public GameServer(){
        System.out.println("======SERVER======");
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
        playerSize=42;
        playerRadius=21;
        puckSize=36;
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

    
    public void setUpPuckMovement(){
        ActionListener a = new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent ae) {
                p1IsHittingPuck = isHittingPuck(1);
                p2IsHittingPuck = isHittingPuck(2);
                p1Scored = playerScored(1);
                p2Scored = playerScored(2);
                
                if (p1IsHittingPuck){
                    double xDif = p1X - puckX;
                    double yDif = p1Y - puckY;
                    double distance = Math.sqrt((puckX-p1X)*(puckX-p1X) + (puckY-p1Y)*(puckY-p1Y));
                    double xCollisionNorm = xDif/distance;
                    double yCollisionNorm = yDif/distance;
                    double xRelativeVelocity = p1HSpeed - puckHSpeed;
                    double yRelativeVelocity = p1VSpeed - puckVSpeed;
                    double speed = xRelativeVelocity * xCollisionNorm + yRelativeVelocity * yCollisionNorm;
                    puckHSpeed = (speed * xCollisionNorm);
                    puckVSpeed = (speed * yCollisionNorm);


                }
                else if (p2IsHittingPuck){
                    double xDif = p2X - puckX;
                    double yDif = p2Y - puckY;
                    double distance = Math.sqrt((puckX-p2X)*(puckX-p2X) + (puckY-p2Y)*(puckY-p2Y));
                    double xCollisionNorm = xDif/distance;
                    double yCollisionNorm = yDif/distance;
                    double xRelativeVelocity = p1HSpeed - puckHSpeed;
                    double yRelativeVelocity = p1VSpeed - puckVSpeed;
                    double speed = xRelativeVelocity * xCollisionNorm + yRelativeVelocity * yCollisionNorm;
                    puckHSpeed = (speed * xCollisionNorm);
                    puckVSpeed = (speed * yCollisionNorm);

                }
                // if (p1IsHittingPuck && p1VSpeed != 0 && p1HSpeed != 0){
                //     puckVSpeed = (puckVSpeed * (5 - 8) + (2 * 8 * p1VSpeed)) / (8 + 5); //second ball = puck
                //     puckHSpeed = (puckHSpeed * (5 - 8) + (2 * 8 * p1HSpeed)) / (8 + 5); //first ball = player
                // }

                // else if (p2IsHittingPuck && p2VSpeed != 0 && p2HSpeed != 0){
                //     puckVSpeed = (puckVSpeed * (5 - 8) + (2 * 8 * p2VSpeed)) / (8 + 5); //second ball = puck
                //     puckHSpeed = (puckHSpeed * (5 - 8) + (2 * 8 * p2HSpeed)) / (8 + 5); //first ball = player
                // }

                if (puckX+36 >= 800 || puckX <= 0){
                    puckHSpeed = puckHSpeed * -1;
                }

                if(puckY+36 >= 400 || puckY <= 0){
                    puckVSpeed = puckVSpeed * -1;
                }
                
                if (puckHSpeed > 0 || puckVSpeed > 0){
                    puckVSpeed -= 0.02;
                    puckHSpeed -= 0.02;
                }
  
                if (puckHSpeed < 0 || puckVSpeed < 0){
                    puckVSpeed += 0.02;
                    puckHSpeed += 0.02;
                }
                puckX += puckHSpeed;
                puckY += puckVSpeed;

                if(p2Scored==true){
                    p2Score+=1;
                    puckX = 382;
                    puckY = 182;
                    puckVSpeed = 0;
                    puckHSpeed = 0;
                    resetPlayers = true;
                    resetPlayers = false;
                }
                if(p1Scored==true){
                    p1Score+=1;
                    puckX = 382;
                    puckY = 182;
                    puckVSpeed = 0;
                    puckHSpeed = 0;
                    resetPlayers = true;
                    resetPlayers = false;
                }

  
            }
            
        };
        Timer puckTimer = new Timer(10,a);
        puckTimer.start();
        
    }

    public boolean isHittingPuck(int i){
        double radiiSum = playerRadius + puckRadius;
        if (i==1){
            double xDif = p1X - puckX;
            double yDif = p1Y - puckY;
            
            if ((xDif*xDif + yDif*yDif) <= radiiSum*radiiSum){
                System.out.println("P1 COLLIDING WITH PUCK");
                return true;
            }
            return false;
        }
        else{
            double xDif = p2X - puckX;
            double yDif = p2Y - puckY;
            if ((xDif*xDif + yDif*yDif) <= radiiSum*radiiSum){
                System.out.println("P2 COLLIDING WITH PUCK");
                return true;
            }
            return false;
        }
    }

    public boolean playerScored(int i){
        if (i == 2){
            if (puckX < 4 && puckY+18 > 148 && puckY+18 < 254){
                System.out.println("Player 2 Scored");
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(puckX+36 > 796 && puckY+18 > 148 && puckY+18 < 254){
                System.out.println("Player 1 Scored");
                return true;
            }
            else{
                return false;
            }
        }
    }

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
                        // dataFromClient = dataIn.readUTF();
                        // System.out.println(dataFromClient);
                        // String delims = "[,]";
                        // String[] data = dataFromClient.split(delims);
                        // p1X = Double.parseDouble(data[0]);
                        // p1Y = Double.parseDouble(data[1]);
                        p1X = dataIn.readDouble();
                        p1Y = dataIn.readDouble();
                        p1HSpeed=dataIn.readDouble();
                        p1VSpeed=dataIn.readDouble();
                    }
                    else{
                        // dataFromClient = dataIn.readUTF();
                        // String[] data = dataFromClient.split(",");
                        // p2X = Double.parseDouble(data[0]);
                        // p2Y = Double.parseDouble(data[1]);
                        p2X = dataIn.readDouble();
                        p2Y = dataIn.readDouble();
                        p2HSpeed=dataIn.readDouble();
                        p2VSpeed=dataIn.readDouble();
                    }
                }
            } catch(IOException ex){
                System.out.println("IOException from RFC run()");
            }
            
        }
    }

    private class WriteToClient implements Runnable {
        // String p1DataWriteToClient = p1X+","+p1Y+","+p1IsHittingPuck;
        // String p2DataWriteToClient = p2X+","+p2Y+","+p2IsHittingPuck;
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
                        // dataOut.writeUTF(p2DataWriteToClient);
                        dataOut.writeDouble(p2X);
                        dataOut.writeDouble(p2Y);
                        dataOut.writeDouble(puckX);
                        dataOut.writeDouble(puckY);
                        dataOut.writeInt(p1Score);
                        dataOut.writeInt(p2Score);
                        dataOut.writeBoolean(resetPlayers);
                        dataOut.flush();
                    }
                    else{
                        // dataOut.writeUTF(p1DataWriteToClient);
                        dataOut.writeDouble(p1X);
                        dataOut.writeDouble(p1Y);
                        dataOut.writeDouble(puckX);
                        dataOut.writeDouble(puckY);
                        dataOut.writeInt(p1Score);
                        dataOut.writeInt(p2Score);
                        dataOut.writeBoolean(resetPlayers);
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(25);
                    } catch(InterruptedException ex){
                        System.out.println("InterruptedException from WTC run()");
                    }
                }
            } catch(IOException ex){
                System.out.println("IOException from RFC run()");
            }
            
        }

        public void sendStartMsg(){
            try{
                dataOut.writeUTF("Both players have connected. Starting the game...");
            }
            catch(IOException ex){
                System.out.println("IOException from sendStartMsg()");
            }
        }
    }




    public static void main(String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
        gs.setUpPuckMovement();
        
    }

}