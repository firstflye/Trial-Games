import java.awt.*;
import java.awt.event.*;
import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener { /*
                                                                                 * Allows to define a new class with all
                                                                                 * the
                                                                                 * functionalities of JPanel, so that
                                                                                 * we can add variables and functions I
                                                                                 * might need
                                                                                 */
    int boardwidth = 360;
    int boardheight = 640;

    // images
    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    // Bird
    int birdX = boardwidth / 8;
    int birdY = boardheight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipes
    int PipeX = boardwidth;
    int PipeY = 0;
    int PipeWidth = 64;
    int PipeHeight = 512;

    class Pipe {
        int x = PipeX;
        int y = PipeY;
        int width = PipeWidth;
        int height = PipeHeight;
        Image img; // Assign image in the constructor
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // game logic
    Bird bird;
    int velocityX = -4; // rate at which pipes moves to the left
    int velocityY = 0; // rate at which bird moves up
    int gravity = 1; // rate at which bird moves down

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameloop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        // setBackground(Color.blue);

        setFocusable(true);
        addKeyListener(this);

        // load Images
        backgroundImage = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("flappybird.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

        // bird
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        // place Pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        // game Timer
        gameloop = new Timer(1000 / 60, this);
        gameloop.start();
    }

    // place new pipes
    public void placePipes() {
        int randomPipeY = (int) (PipeY - PipeHeight / 4 - Math.random() * (PipeHeight / 2));// random position of top
                                                                                            // pipe

        int openingSpace = boardheight / 4;
        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + PipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        // System.out.println("draw");
        // background
        g.drawImage(backgroundImage, 0, 0, boardwidth, boardheight, null);

        // bird
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);

        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // two pipes
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardheight) {
            gameOver = true;
        }

    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x && // a's top right corner doesn't reach b's left corner
                a.y < b.y + b.height && // a's top left corner doesn't reach b's left corner
                a.y + a.height > b.y; // a's bottom left corner passes b's top left corner

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // move bird
        repaint();

        if (gameOver) {
            placePipesTimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) {
                // restart the game by resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameloop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
