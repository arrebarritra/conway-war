package conwaywar;

import conwaywar.enums.Turn;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    private int fps;
    private Thread thread;
    public boolean running = false;
    public static Window window;
    public static Turn turn = Turn.redTurn;
    public static int gridSize;
    public static int cellSize;

    public int WIDTH = 640, HEIGHT = 704;
    private int rows = 10;
    private int baseLength = 3;
    private Team[] teams;

    private Grid grid;
    private ButtonContainer buttons;

    public Game() {
        gridSize = WIDTH;
        cellSize = gridSize / rows;
        teams = new Team[]{new Team(Color.red, baseLength), new Team(Color.blue, rows - baseLength)};
        grid = new Grid(this, teams, rows, baseLength, gridSize, cellSize);
        buttons = new ButtonContainer(this, teams);
        window = new Window(WIDTH, HEIGHT, "Conway's Game of War", this);
        this.start();
    }

    public void newRound() {
        for (Team team : teams) {
            team.newRound();
        }
    }

    public void killUnits(){
        grid.killUnits();
    }
    
    public void markDeletion() {
        grid.markDeletion();
    }

    public void resetGame() {
        grid.resetTeams();
        turn.resetTurns();
        turn = turn.redTurn;
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 20.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                frames = 0;
            }
        }
        stop();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        grid.render(g);
        buttons.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Game();
    }
}
