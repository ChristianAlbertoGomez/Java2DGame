import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    /**
     * GamePanel works as a kind of a game screen.
     */

    //Screen Settings
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    public final int tilesSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;

    public final int screenWidth = tilesSize * maxScreenCol; //768 pixels
    public final int screenHeight = tilesSize * maxScreenRow; // 576 pixels

    // FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this,keyH);


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // This improves the rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);//This means this class GamePanel
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread!=null){

            // Update information such as character positions, etc.
            update();

            // Draw the screen with the updated information.
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics g2 = (Graphics2D)g;
        tileM.draw((Graphics2D) g2);
        player.draw((Graphics2D) g2);
        g2.dispose();
    }
}
