package conwaywar;

import conwaywar.enums.Turn;
import conwaywar.gui.Button;
import conwaywar.gui.ButtonListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Iterator;

public class ButtonContainer implements ButtonListener {

    private int gridSize, cellSize;
    private Button reset, done, newGame;
    private Team[] teams;
    private Game frame;

    public ButtonContainer(Game frame, Team[] teams) {
        this.teams = teams;
        this.frame = frame;
        gridSize = Grid.gridSize;
        cellSize = Grid.cellSize;
        reset = new Button(frame, this, "Reset", new Font("Verdana", Font.PLAIN, 30), 0, gridSize + 10, gridSize / 3, 64 - 11, Color.red, null, Color.white, Color.white);
        done = new Button(frame, this, "Done", new Font("Verdana", Font.PLAIN, 30), gridSize / 3, gridSize + 10, gridSize / 3, 64 - 11, Color.green, null, Color.white, Color.white);
        newGame = new Button(frame, this, "New Game", new Font("Verdana", Font.PLAIN, 30), gridSize * 2 / 3, gridSize + 10, gridSize / 3 + 1, 64 - 11, Color.blue, null, Color.white, Color.white);
    }

    public void render(Graphics2D g) {
        reset.render(g);
        done.render(g);
        newGame.render(g);
    }

    @Override
    public void buttonPressed(Button b) {
        int turnNumber = Game.turn.ordinal();
        if (b.getText().equals("Reset")) {
            teams[turnNumber].resetUnits();
            frame.markDeletion();
        } else if (b.getText().equals("Done")) {            
            frame.killUnits();
            frame.newRound();
            Game.turn = Turn.values()[Math.abs(turnNumber - 1)];
        } else if (b.getText().equals("New Game")) {
            frame.resetGame();
        }
    }

}
