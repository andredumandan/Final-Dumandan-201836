import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class GameFrame extends JFrame{

    private int width;
    private int height;
    private Container contentPane;
    private double speed;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private GameCanvas gc;
    private Socket socket;
    private int playerID;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    public GameFrame(int width, int height){
        this.width = width;
        this.height = height;
        gc = new GameCanvas();
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public void setUpGUI(){
        contentPane = this.getContentPane();
        this.setTitle("PLAYER #" + playerID);
        contentPane.setPreferredSize(new Dimension(width,height));
        gc.createSprites();
        contentPane.add(gc);
        setUpAnimation();
        setUpKeyListener();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        
    }

    public void setUpAnimation(){
        ActionListener a = new ActionListener(){
            double speed = 1;
            
            @Override
            public void actionPerformed(ActionEvent ae){ 
                if(up == true){
                    gc.getPlayer(playerID).moveV(-speed);
                }
                if(down == true){
                    gc.getPlayer(playerID).moveV(speed);
                }
                if(right == true){
                    gc.getPlayer(playerID).moveH(speed);
                }
                if(left == true){
                    gc.getPlayer(playerID).moveH(-speed);
                }
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
                    case KeyEvent.VK_UP :
                        up = true;
                        break;
                    case KeyEvent.VK_DOWN :
                        down = true;
                        break;
                    case KeyEvent.VK_RIGHT :
                        right = true;
                        break;
                    case KeyEvent.VK_LEFT :
                        left = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke){
                int keyCode = ke.getKeyCode();

                switch(keyCode){
                    case KeyEvent.VK_UP :
                        up = false;
                        break;
                    case KeyEvent.VK_DOWN :
                        down = false;
                        break;
                    case KeyEvent.VK_RIGHT :
                        right = false;
                        break;
                    case KeyEvent.VK_LEFT :
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
            try {
                while(true){
                    if(gc.getPlayer(playerID) != null){
                        dataOut.writeDouble(gc.getPlayer(playerID).getX());
                        dataOut.writeDouble(gc.getPlayer(playerID).getY());
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
