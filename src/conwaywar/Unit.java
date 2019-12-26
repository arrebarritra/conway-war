package conwaywar;

import java.awt.Color;
import java.awt.Graphics2D;

public class Unit {

    private Color teamColour;
    private boolean selected;
    private int[] position;
    private int teamAdjacent;
    private int enemyAdjacent;
    private int size;
    private boolean delete = false;

    public Unit(int[] position, Color teamColour) {
        this.position = position;
        this.teamColour = teamColour;
        size = Grid.cellSize;
    }

    public void render(Graphics2D g) {
        g.setColor(teamColour);
        g.fillRect(position[0] * size, position[1] * size, size, size);
        if (delete) {
            g.setColor(Color.black);
            g.fillRect(position[0] * size + size / 3, position[1] * size + size / 3, size / 3, size / 3);
        }
    }

    @Override
    public Unit clone() {
        return new Unit(position, teamColour);
    }

    public void checkNeighbours() {
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public Color getTeamColour() {
        return teamColour;
    }

    public void setTeamColour(Color teamColour) {
        this.teamColour = teamColour;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        this.selected = true;
    }

    public void unSelect() {
        this.selected = false;
    }

    public void incrementTeamAdjacent() {
        teamAdjacent++;
    }

    public void resetTeamAdjacent() {
        teamAdjacent = 0;
    }

    public int getTeamAdjacent() {
        return teamAdjacent;
    }

    public void incrementEnemyAdjacent() {
        enemyAdjacent++;
    }

    public void resetEnemyAdjacent() {
        enemyAdjacent = 0;
    }

    public int getEnemyAdjacent() {
        return enemyAdjacent;
    }

    public void markDeletion() {
        delete = true;
    }

    public void resetDeletion() {
        delete = false;
    }

    public boolean toBeDeleted() {
        return delete;
    }
}
