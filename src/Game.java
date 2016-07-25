/**
 * Created by Bopok on 7/24/2016.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;


public class Game extends Canvas implements Runnable {


    public static int width = 320;
    public static int height = width * 9 / 16;
    public static int scale = 4;

    private boolean running = false;
    private JFrame frame;
    private Thread gameThread;

    Background bg = new Background();


    public Game(){
        Dimension dimension = new Dimension(width * scale, height * scale);
        setPreferredSize(dimension);

        frame = new JFrame();

    }

    public synchronized void start(){
        running = true;
        gameThread = new Thread(this, "display" );
        gameThread.start();
    }

    public synchronized  void stop(){
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        while(running){
            /*long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
              update();
            }*/

            try {
                render();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void update(){

    }

    public void render() throws IOException {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        Image img = ImageIO.read(new File("D:\\Downloads\\Bellum\\src\\field.jpg"));
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, null);
        g.setColor(Color.RED);
        g.fillRect(width * scale / 2 - 32, height * scale / 2 - 32, 64, 64);
        g.dispose();
        bs.show();
    }

   public static void main(String[] args){
       Game game = new Game();
       game.frame.setResizable(false);
       game.frame.setTitle("Bellum");
       game.frame.add(game);
       game.frame.pack();
       game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       game.frame.setLocationRelativeTo(null);
       game.frame.setVisible(true);

       game.start();

   }

}
