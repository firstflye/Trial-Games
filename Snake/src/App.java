import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardwidth = 600;
        int boardheight = boardwidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null); // open the window at the centre
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Snakegame snakegame = new Snakegame(boardwidth, boardheight);
        frame.add(snakegame);
        frame.pack(); // so that the title bar is not added to the game dimensions
        snakegame.requestFocus();

    }
}
