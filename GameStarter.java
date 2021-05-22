public class GameStarter {
    public static void main(String[] args){
        GameFrame g = new GameFrame(800,400);
        g.connectToServer();
        g.setUpGUI();
    }
}
