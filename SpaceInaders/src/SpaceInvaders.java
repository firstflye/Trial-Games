import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean alive = true; // used for aliens
        boolean used = false; // used for bullets

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    // board
    int tilesize = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tilesize * columns;
    int boardHeight = tilesize * rows;

    Image shipImage;
    Image alienCyanImage;
    Image alienmagentaImage;
    Image alienyellowImage;
    Image alienImage;
    ArrayList<Image> alienImageArray;

    // ship
    int shipWidth = tilesize * 2; // 64px
    int shipHeight = tilesize;
    int shipX = tilesize * columns / 2 - tilesize; // move 1 tile back to the center
    int shipY = boardHeight - tilesize * 2;
    int shipVelocityX = tilesize;
    Block ship;

    // aliens
    ArrayList<Block> alienArray;
    int alienWidth = tilesize * 2;
    int alienHeight = tilesize;
    int alienX = tilesize;
    int alienY = tilesize;

    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0; // number of aliens to defeat
    int alienVelocityX = 1; // alien moving speed

    // bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tilesize / 8;
    int bulletHeight = tilesize / 2;
    int bulletVelocity = -10; // bullet movement speed

    Timer gameloop;
    int score = 0;
    boolean gameover = false;

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // load images
        shipImage = new ImageIcon(getClass().getResource("ship.png")).getImage();
        alienCyanImage = new ImageIcon(getClass().getResource("alien-cyan.png")).getImage();
        alienmagentaImage = new ImageIcon(getClass().getResource("alien-magenta.png")).getImage();
        alienyellowImage = new ImageIcon(getClass().getResource("alien-yellow.png")).getImage();
        alienImage = new ImageIcon(getClass().getResource("alien.png")).getImage();

        alienImageArray = new ArrayList<Image>();
        alienImageArray.add(alienCyanImage);
        alienImageArray.add(alienmagentaImage);
        alienImageArray.add(alienyellowImage);
        alienImageArray.add(alienImage);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImage);
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();

        // game timer
        gameloop = new Timer(1000 / 60, this);
        createAliens();
        gameloop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        // aliens
        for (int i = 0; i < alienArray.size(); i++) {
            Block alien = alienArray.get(i);
            if (alien.alive) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        // bullets
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height); // fillRect for white bullets
            }
        }

        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameover) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        } else {
            g.drawString(String.valueOf(score), 10, 35);
        }

    }

    public void move() {
        // aliens
        for (int i = 0; i < alienArray.size(); i++) {
            Block alien = alienArray.get(i);
            if (alien.alive) {
                alien.x += alienVelocityX;

                // if alien touches the borders
                if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX * 2;

                    // move aliens down by one row
                    for (int j = 0; j < alienArray.size(); j++) {
                        alienArray.get(j).y += alienHeight; // aliens are 1 tile size
                    }
                }
                if (alien.y >= ship.y) {
                    gameover = true;
                }
            }
        }

        // bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocity;

            // bullet collision with aliens
            for (int j = 0; j < alienArray.size(); j++) {
                Block alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }
        // clear bullets not seen on screen
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
            bulletArray.remove(0); // removes the first element of the array
        }

        // next level
        if (alienCount == 0) {
            // increase the number of aliens in columns and rows by 1
            score += alienColumns * alienRows * 100;
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2); // cap column at 16 / 2 - 2 - 6
            alienRows = Math.min(alienRows + 1, rows - 6); // Cap row at 16 - 6 = 10
            alienArray.clear();
            bulletArray.clear();
            alienVelocityX = 1;
            createAliens();
        }
    }

    public void createAliens() {
        Random random = new Random();
        for (int r = 0; r < alienRows; r++) {
            for (int c = 0; c < alienColumns; c++) {
                int randomImageIndex = random.nextInt(alienImageArray.size());
                Block alien = new Block(
                        alienX = c * alienWidth,
                        alienY + r * alienHeight,
                        alienWidth,
                        alienHeight,
                        alienImageArray.get(randomImageIndex));
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameover) {
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameover) { // any key to restart
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameover = false;
            createAliens();
            gameloop.start();

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0) {
            ship.x -= shipVelocityX; // move left 1 tile
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth) {
            ship.x += shipVelocityX; // move right 1 tile
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Block bullet = new Block(ship.x + shipWidth * 15 / 32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }

    }

}
