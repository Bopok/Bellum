/**
 * Created by Bopok on 7/24/2016.
 */

package com.bopok.bellum;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import com.bopok.bellum.graphics.Screen;


public class Game extends Canvas implements Runnable {


    public static int width = 320;
    public static int height = width * 9 / 16;
    public static int scale = 4;

    private boolean running = false;
    private JFrame frame;
    private Thread gameThread;
    public static String title = "Bellum";

    private Screen screen;

    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();


    public Game(){
        Dimension dimension = new Dimension(width * scale, height * scale);
        setPreferredSize(dimension);

        screen = new Screen(width, height);

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
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0;
        int updates = 0;

        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            try {
                render();
            } catch (IOException e) {
                e.printStackTrace();
            }
            frames++;

                if (System.currentTimeMillis() - timer > 1000){
                    timer += 1000;
                    frame.setTitle(title + " | " + updates + " ups" + frames + " fps");
                    frames = 0;
                    updates = 0;
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

        screen.clear();
        screen.render();

        for(int i = 0; i < pixels.length; i++){
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

   public static void main(String[] args){
       Game game = new Game();
       game.frame.setResizable(false);
       game.frame.setTitle("game.title");
       game.frame.add(game);
       game.frame.pack();
       game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       game.frame.setLocationRelativeTo(null);
       game.frame.setVisible(true);

       game.start();

   }

}
