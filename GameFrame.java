/**
	
    This java file extends JFrame. It controls most of the player movements and runs threads that send data from the server and receive data from the server.

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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class GameFrame extends JFrame{

    private static final int frameWidth = 800;
    private static final int frameHeight = 448;
    private int playerID, p1Score, p2Score;
    private int port;
    private boolean up, down, left, right, playerPlayAgain, playerStopMovement, resetPlayers;
    private double puckVSpeed, puckHSpeed, directionalSpeed;
    private double playerX, playerY, playerVSpeed, playerHSpeed, playerSize;
    private String ip;
    private Container contentPane;
    private GameCanvas gc;
    private Socket socket;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    private JLabel scoreLabel;
    private File audio;
    private AudioInputStream audioStream;
    private Clip airHorn;
    private JButton playAgain;
    private ButtonListener bl;
    
    
    /**
     *  Initializes the instance fields used in the GameFrame.
     */
    public GameFrame(){
        up = false;
        down = false;
        left = false;
        right = false;
        resetPlayers = false;
        playerSize=42;
        puckVSpeed = 0;
        puckHSpeed = 0;
        p1Score=0;
        p2Score=0;
        directionalSpeed = 3;
        gc = new GameCanvas();
        bl = new ButtonListener();
        scoreLabel = new JLabel(p1Score+"  -  "+p2Score);
        playAgain = new JButton("Play Again");
        //File, AudioStream, Clip. https://www.youtube.com/watch?v=SyZQVJiARTQ
        try{
            audio = new File("AirHornSoundEffect.wav"); 
            audioStream = AudioSystem.getAudioInputStream(audio);
            airHorn = AudioSystem.getClip();
            airHorn.open(audioStream);
        }
        catch(IOException ex){
            System.out.println("IOException from RFS run()");
        }
        catch (UnsupportedAudioFileException ua) {
            System.out.println("The audio file is unsupported.");
        } 
        catch (LineUnavailableException l) {
            System.out.println("The audio line cannot be opened because it is unavailable.");
        } 
    }

    /**
     * A void method that sets up the Graphical User Interface of the game.
     */
    public void setUpGUI(){
        contentPane = this.getContentPane();
        this.setTitle("PLAYER #" + playerID);
        contentPane.setPreferredSize(new Dimension(frameWidth,frameHeight));
        gc.createSprites();
        contentPane.setLayout(new BorderLayout());
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setVerticalAlignment(SwingConstants.NORTH);
        scoreLabel.setFont(new Font("Arial",Font.PLAIN,40));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setOpaque(true);
        contentPane.add(scoreLabel,BorderLayout.NORTH);
        contentPane.add(gc,BorderLayout.CENTER);
        contentPane.add(playAgain,BorderLayout.SOUTH);
        playAgain.setVisible(false);
        playAgain.addActionListener(bl);
        setUpAnimation();
        setUpKeyListener();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    /**
     * A void method that creates an ActionListener for the movement of the player by updating the position of the player. It contains a timer that constantly repaints the DrawingCanvas.
     */
    public void setUpAnimation(){
        ActionListener a = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){ 
                if(playerStopMovement == false){
                    scoreLabel.setText(p1Score+"  -  "+p2Score);
                if(up == true){
                    if(gc.getPlayer(playerID).getY() > 0){
                        playerVSpeed = -directionalSpeed;
                        gc.getPlayer(playerID).moveV(playerVSpeed);
                    }
                }
                if(down == true){
                    if(gc.getPlayer(playerID).getY() + playerSize < 400){
                        playerVSpeed = directionalSpeed;
                        gc.getPlayer(playerID).moveV(playerVSpeed);
                    }
                }
                if(right == true){
                    if(playerID == 1){
                        if(gc.getPlayer(playerID).getX() + playerSize < 396){
                            playerHSpeed = directionalSpeed;  
                            gc.getPlayer(playerID).moveH(playerHSpeed);
                        }
                    }
                    else{
                        if(gc.getPlayer(playerID).getX() + playerSize < 800){
                            playerHSpeed = directionalSpeed;
                            gc.getPlayer(playerID).moveH(playerHSpeed);
                        }
                    }
                }
                if(left == true){
                    if(playerID == 1){
                        if(gc.getPlayer(playerID).getX() > 0){
                            playerHSpeed = -directionalSpeed;
                            gc.getPlayer(playerID).moveH(playerHSpeed);
                        }
                    }
                    else{
                        if(gc.getPlayer(playerID).getX() > 404){
                            playerHSpeed = -directionalSpeed;
                            gc.getPlayer(playerID).moveH(playerHSpeed);
                        }
                    }
                    
                }
                playerX = gc.getPlayer(playerID).getX();
                playerY = gc.getPlayer(playerID).getY();
                gc.getPuck().moveH(puckHSpeed);
                gc.getPuck().moveV(puckVSpeed);
                gc.repaint();
                }
            }
        };
        Timer aTimer = new Timer(5,a);
        aTimer.start();
    }

    /**
     * A void method that creates a KeyListener instance that determines whether the movement keys are pressed. It makes use of booleans to determine if the player should move. 
     */
    public void setUpKeyListener(){
        KeyListener kl = new KeyListener(){
            @Override
            public void keyTyped(KeyEvent ke){
            }

            @Override
            public void keyPressed(KeyEvent ke){
                int keyCode = ke.getKeyCode();

                switch(keyCode){
                    case KeyEvent.VK_W :
                        up = true;
                        break;
                    case KeyEvent.VK_S :
                        down = true;
                        break;
                    case KeyEvent.VK_D :
                        right = true;
                        break;
                    case KeyEvent.VK_A :
                        left = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke){
                int keyCode = ke.getKeyCode();

                switch(keyCode){
                    case KeyEvent.VK_W :
                        up = false;
                        playerVSpeed = 0;
                        break;
                    case KeyEvent.VK_S :
                        down = false;
                        playerVSpeed = 0;
                        break;
                    case KeyEvent.VK_D :
                        right = false;
                        playerHSpeed = 0;
                        break;
                    case KeyEvent.VK_A :
                        left = false;
                        playerHSpeed = 0;
                        break;
                }
            }
        };
        contentPane.addKeyListener(kl);
        contentPane.setFocusable(true);
    }

    /**
     *  A void method that creates a socket used to connect to the server.
     */
    public void connectToServer(){
        try{
            socket = new Socket(ip,port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("You are Player#" + playerID);
            if (playerID == 1){
                System.out.println("Waiting for connections...");
            }

            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);

            rfsRunnable.waitForStartMsg();
            
        } catch(IOException ex){
            System.out.println("IOException occurred at connectToServer()");
        }
    }

    
    /** 
     * Sets the IP Address and the Port Number for the Socket.
     * @param ip - the IP Address of the Server.
     * @param port - the port number of the ServerSocket.
     */
    public void setUpServerIP(String ip, int port){
        this.ip = ip;
        this.port = port;

    }

    /**
     * An inner class that implements Runnable. This thread continuously reads data from the server.
     */
    private class ReadFromServer implements Runnable{
        
        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream dataIn){
            this.dataIn = dataIn;
            System.out.println("RFS Runnable created.");
        }

        @Override
        public void run() {
            try{
                while(true){
                    if(gc.getEnemy(playerID) != null){
                        resetPlayers = dataIn.readBoolean();
                        if(resetPlayers == true){
                            airHorn.setMicrosecondPosition(0);
                            airHorn.start();
                            playerHSpeed = 0;
                            playerVSpeed = 0;

                            if(playerID == 1){
                                gc.getPlayer(playerID).setX(169);
                                gc.getPlayer(playerID).setY(179);
                                gc.getEnemy(playerID).setX(589);
                                gc.getEnemy(playerID).setY(179);
        
                            }
                            if(playerID == 2){
                                gc.getEnemy(playerID).setX(169);
                                gc.getEnemy(playerID).setY(179);
                                gc.getPlayer(playerID).setX(589);
                                gc.getPlayer(playerID).setY(179);
                            }
                            resetPlayers=false;
                        }
                        gc.getEnemy(playerID).setX(dataIn.readDouble());
                        gc.getEnemy(playerID).setY(dataIn.readDouble());
                        gc.getPuck().setX(dataIn.readDouble());
                        gc.getPuck().setY(dataIn.readDouble());
                        p1Score = dataIn.readInt();
                        p2Score = dataIn.readInt();

                        if(p1Score == 0 && p2Score == 0){
                            playerStopMovement = false;
                            playerPlayAgain = false;
                            playAgain.setVisible(false);
                        }

                        if (p1Score == 15 || p2Score == 15){
                            playAgain.setVisible(true);
                            playerStopMovement = true;
                            if(p1Score==15){
                                scoreLabel.setText("WINNER  -  LOSER");
                            }
                            else{
                                scoreLabel.setText("LOSER  -  WINNER");
                            }
                            
                        }
                    }
                }
            } catch(IOException ex){
                System.out.println("IOException from RFS run()");
            }
        }
        
        public void waitForStartMsg(){
            try{
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();
            }
            catch(IOException ex){
                System.out.println("IOException at waitForStartMsg()");
            }
        }
        
    }

    /**
     * This inner class implements Runnable. It continuously sends the player data to the Server.
     */
    private class WriteToServer implements Runnable{

        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream dataOut){
            this.dataOut = dataOut;
            System.out.println("WTS Runnable created.");
        }

        @Override
        public void run() {
            try {
                while(true){
                    if(gc.getPlayer(playerID) != null){
                        dataOut.writeDouble(gc.getPlayer(playerID).getX());
                        dataOut.writeDouble(gc.getPlayer(playerID).getY());
                        dataOut.writeDouble(playerHSpeed);
                        dataOut.writeDouble(playerVSpeed);
                        dataOut.writeBoolean(playerPlayAgain);
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(8);
                    } catch(InterruptedException ex){
                        System.out.println("InterruptedException from WTS run()");
                    }
                }
            } 
            catch (IOException ex){
                System.out.println("IOException from WTS run()");
            }
        }
    }

    /**
     * An inner class that implements ActionListener. Created for the playAgain button after one Player wins the game.
     */
    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            playerPlayAgain = true;
            resetPlayers = true;
        }

    }

}
