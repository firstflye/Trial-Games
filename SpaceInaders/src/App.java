import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // Window variables
        int tilesize = 32;
        int rows = 16;
        int columns = 16;
        int boardWidth = tilesize * columns; // 32 * 16 = 512px
        int boardHeight = tilesize * rows;

        JFrame frame = new JFrame("Space Invaders");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);
    }
}
