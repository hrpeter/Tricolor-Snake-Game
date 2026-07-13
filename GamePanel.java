import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;

    private boolean running = true;

    private int[] snakeX;
    private int[] snakeY;

    private final Color GREMIO_BLUE = new Color(0, 155, 255);
    private final Color GREMIO_BLACK = new Color(0, 0, 0);
    private final Color GREMIO_WHITE = new Color(255, 255, 255);
    private final Color GREMIO_DARK = new Color(60, 60, 60);

    private int foodX;
    private int foodY;

    private int corpoSnake = 3;
    private char direcao = 'D';

    static final int tamanho_move = 25;

    private Timer timer;
    private Random random;

    static final int GAME_UNITS =
        (SCREEN_WIDTH * SCREEN_HEIGHT) / (tamanho_move * tamanho_move);

    public GamePanel() {
        this.setPreferredSize(
                new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        this.setBackground(GREMIO_BLACK);
        this.setFocusable(true);

        random = new Random();

        snakeX = new int[GAME_UNITS];
        snakeY = new int[GAME_UNITS];

        snakeX[0] = 100;
        snakeY[0] = 100;

        snakeX[1] = 75;
        snakeY[1] = 100;

        snakeX[2] = 50;
        snakeY[2] = 100;

        gerarComida();

        addKeyListener(new MyKeyAdapter());

        timer = new Timer(150, this);
        reiniciarJogo();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTela(g);
    }

    public void drawTela(Graphics g) {
        if (running) {
            // Comida
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, tamanho_move, tamanho_move);

            g.setColor(GREMIO_WHITE);
            g.drawOval(foodX, foodY, tamanho_move, tamanho_move);

            // Cobra
            for (int i = 0; i < corpoSnake; i++) {
                if (i == 0) {
                    g.setColor(GREMIO_BLUE);
                } else if (i % 2 == 0) {
                    g.setColor(GREMIO_WHITE);
                } else {
                    g.setColor(GREMIO_DARK);
                }

                g.fillRect(
                    snakeX[i],
                    snakeY[i],
                    tamanho_move,
                    tamanho_move
                );
            }

            // Pontuação
            g.setFont(new Font("Calibri", Font.BOLD, 25));
            g.setColor(GREMIO_BLUE);
            g.drawString("Pontuação: " + (corpoSnake - 3), 20, 40);

            // Título
            Font font = new Font("Calibri", Font.BOLD, 40);
            g.setFont(font);
            g.setColor(GREMIO_BLUE);

            String titulo = "Tricolor Snake Game";
            int larguraTexto = g.getFontMetrics().stringWidth(titulo);

            g.drawString(titulo, (SCREEN_WIDTH - larguraTexto) / 2, 100);
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        // Título
        g.setColor(GREMIO_WHITE);
        g.setFont(new Font("Calibri", Font.BOLD, 40));

        String tituloOver = "Fim de Jogo - Grêmio é insano!";
        int larguraTitulo = g.getFontMetrics().stringWidth(tituloOver);

        g.drawString(tituloOver,(SCREEN_WIDTH - larguraTitulo) / 2,100);

        // Pontuação
        String score = "Pontuação: " + (corpoSnake - 3);

        g.setColor(GREMIO_BLUE);
        g.setFont(new Font("Calibri", Font.BOLD, 30));

        int larguraScore = g.getFontMetrics().stringWidth(score);

        g.drawString(score,(SCREEN_WIDTH - larguraScore) / 2,160);

        String mensagem = "Pressione ENTER para jogar novamente";

        g.setFont(new Font("Calibri", Font.PLAIN, 20));

        int larguraMensagem = g.getFontMetrics().stringWidth(mensagem);

        g.drawString(mensagem, (SCREEN_WIDTH - larguraMensagem) / 2,220);
    }

    public void move() {
        for (int i = corpoSnake; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        switch (direcao) {
            case 'W':
                snakeY[0] -= tamanho_move;
                break;

            case 'S':
                snakeY[0] += tamanho_move;
                break;

            case 'A':
                snakeX[0] -= tamanho_move;
                break;

            case 'D':
                snakeX[0] += tamanho_move;
                break;
        }
    }

    private void gerarComida() {
        boolean posicaoValida;
        do {
            posicaoValida = true;
            foodX = random.nextInt(SCREEN_WIDTH / tamanho_move) * tamanho_move;
            foodY = random.nextInt(SCREEN_HEIGHT / tamanho_move) * tamanho_move;

            for (int i = 0; i < corpoSnake; i++) {
                if (snakeX[i] == foodX && snakeY[i] == foodY) {
                    posicaoValida = false;
                    break;
                }
            }
        } while (!posicaoValida);
    }

    public void colisao() {
        // Parede
        if (snakeX[0] < 0 || snakeX[0] >= SCREEN_WIDTH || snakeY[0] < 0 || snakeY[0] >= SCREEN_HEIGHT) {
            morrer();
        }

        // Próprio corpo
        for (int i = 1; i < corpoSnake; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                morrer();
            }
        }
    }

    private void morrer() {
        running = false;

        if (timer != null) {
            timer.stop();
        }
    }

    public void checkFood() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            if (corpoSnake < GAME_UNITS - 1) {
                corpoSnake++;
            }
            gerarComida();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            colisao();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (!running && key == KeyEvent.VK_ENTER) {
                reiniciarJogo();
                return;
            }

            if (key == KeyEvent.VK_W && direcao != 'S') {
                direcao = 'W';
            }

            if (key == KeyEvent.VK_S && direcao != 'W') {
                direcao = 'S';
            }

            if (key == KeyEvent.VK_A && direcao != 'D') {
                direcao = 'A';
            }

            if (key == KeyEvent.VK_D && direcao != 'A') {
                direcao = 'D';
            }
        }
    }

    private void reiniciarJogo() {
        corpoSnake = 3;
        direcao = 'D';
        running = true;

        for (int i = 0; i < GAME_UNITS; i++) {
            snakeX[i] = 0;
            snakeY[i] = 0;
        }

        snakeX[0] = 100;
        snakeY[0] = 100;

        snakeX[1] = 75;
        snakeY[1] = 100;

        snakeX[2] = 50;
        snakeY[2] = 100;

        gerarComida();

        timer.start();
    }
}