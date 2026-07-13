import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Tricolor Snake Game");

        GamePanel gamePanel = new GamePanel();

        frame.setVisible(true);
        gamePanel.requestFocusInWindow();

        frame.add(gamePanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.pack();

        frame.setLocationRelativeTo(null);
    }
}