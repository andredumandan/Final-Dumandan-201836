import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class GameFrame extends JFrame{

    private int width;
    private int height;
    private Container contentPane;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private GameCanvas gc;
    private Socket socket;
    private int playerID,p1Score,p2Score;
    double playerX;
    double playerY;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    // String playerWriteToServer;
    // String playerReadFromServer;
    JLabel scoreLabel;
    double playerVSpeed, playerHSpeed;
    double puckVSpeed = 0;
    double puckHSpeed = 0;
    double puckX,puckY,playerSize,puckSize,playerRadius,puckRadius;
    boolean resetPlayers;



    public GameFrame(int width, int height){
        this.width = width;
        this.height = height;
        gc = new GameCanvas();
        up = false;
        down = false;
        left = false;
        right = false;
        puckX = 382;
        puckY = 182;
        playerSize=42;
        playerRadius=21;
        puckSize=36;
        puckRadius=18;
        p1Score=0;
        p2Score=0;
        scoreLabel = new JLabel(p1Score+"-"+p2Score);
        resetPlayers = false;

    }

    public void setUpGUI(){
        contentPane = this.getContentPane();
        this.setTitle("PLAYER #" + playerID);
        contentPane.setPreferredSize(new Dimension(width,height));
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
        setUpAnimation();
        setUpKeyListener();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        
    }

    public void setUpAnimation(){
        ActionListener a = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){ 
                scoreLabel.setText(p1Score+"  -  "+p2Score);
                if(up == true){
                    if(gc.getPlayer(playerID).getY() > 0){
                        playerVSpeed = -4;
                        gc.getPlayer(playerID).moveV(playerVSpeed);
                    }
                }
                if(down == true){
                    if(gc.getPlayer(playerID).getY()+42 < 400){
                        playerVSpeed = 4;
                        gc.getPlayer(playerID).moveV(playerVSpeed);
                    }
                }
                if(right == true){
                    if(playerID == 1){
                        if(gc.getPlayer(playerID).getX() + 42 < 396){
                            playerHSpeed = 4;  
                            gc.getPlayer(playerID).moveH(playerHSpeed);
                        }
                    }
                    else{
                        if(gc.getPlayer(playerID).getX() + 42 < 800){
                            playerHSpeed = 4;
                            gc.getPlayer(playerID).moveH(playerHSpeed);
                        }
                    }
                }
                if(left == true){
                    if(playerID == 1){
                        if(gc.getPlayer(playerID).getX() > 0){
                            playerHSpeed = -4;
                            gc.getPlayer(playerID).moveH(playerHSpeed);
                        }
                    }
                    else{
                        if(gc.getPlayer(playerID).getX() > 404){
                            playerHSpeed = -4;
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
        };
        Timer aTimer = new Timer(10,a);
        aTimer.start();
    }

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
                        break;
                    case KeyEvent.VK_S :
                        down = false;
                        break;
                    case KeyEvent.VK_D :
                        right = false;
                        break;
                    case KeyEvent.VK_A :
                        left = false;
                        break;
                }
            }
        };
        contentPane.addKeyListener(kl);
        contentPane.setFocusable(true);
    }

    public void connectToServer(){
        try{
            socket = new Socket("localhost",50301);
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
                        gc.getEnemy(playerID).setX(dataIn.readDouble());
                        gc.getEnemy(playerID).setY(dataIn.readDouble());
                        gc.getPuck().setX(dataIn.readDouble());
                        gc.getPuck().setY(dataIn.readDouble());
                        p1Score = dataIn.readInt();
                        p2Score = dataIn.readInt();
                        resetPlayers = dataIn.readBoolean();
                        if(resetPlayers == true){
                            playerHSpeed = 0;
                            playerVSpeed = 0;
                            if(playerID == 1){
                                gc.getPlayer(playerID).setX(169);
                                gc.getPlayer(playerID).setY(179);
                                gc.getEnemy(playerID).setX(589);
                                gc.getEnemy(playerID).setY(179);
        
                            }
                            else{
                                gc.getEnemy(playerID).setX(169);
                                gc.getEnemy(playerID).setY(179);
                                gc.getPlayer(playerID).setX(589);
                                gc.getPlayer(playerID).setY(179);
                            }
                            resetPlayers=false;
                        }
                        // playerReadFromServer = dataIn.readUTF();
                        // System.out.println(playerReadFromServer);
                        // String delims = "[,]";
                        // String[] data = playerReadFromServer.split(delims);
                        // gc.getEnemy(playerID).setX(Double.parseDouble(data[0]));
                        // gc.getEnemy(playerID).setY(Double.parseDouble(data[1]));
                        // if (playerID == 1){
                        //     p1IsHittingPuck = Boolean.parseBoolean(data[2]);
                        // }
                        // else{
                        //     p2IsHittingPuck = Boolean.parseBoolean(data[2]);
                        // }
                    }
                }
            } catch(IOException ex){
                System.out.println("IOException from RFC run()");
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

    private class WriteToServer implements Runnable{
        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream dataOut){
            this.dataOut = dataOut;
            System.out.println("WTS Runnable created.");
        }

        @Override
        public void run() {
            // if(gc.getPlayer(playerID) != null){
            //     playerWriteToServer = playerX +","+playerY;
            // }
            try {
                while(true){
                    if(gc.getPlayer(playerID) != null){
                        dataOut.writeDouble(gc.getPlayer(playerID).getX());
                        dataOut.writeDouble(gc.getPlayer(playerID).getY());
                        dataOut.writeDouble(playerHSpeed);
                        dataOut.writeDouble(playerVSpeed);
                        // dataOut.writeUTF(playerWriteToServer);
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(25);
                    } catch(InterruptedException ex){
                        System.out.println("InterruptedException from WTS run()");
                    }
                }
            } catch (IOException ex){
                System.out.println("IOException from WTS run()");
            }
        }
    }
}
