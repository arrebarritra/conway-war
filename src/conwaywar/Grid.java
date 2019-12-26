package conwaywar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Iterator;

public class Grid implements MouseListener {

    Game frame;

    private int rows = 10;
    private int baseLength = 3;
    public static int gridSize;
    public static int cellSize;
    Team[] teams;

    public Grid(Game frame, Team[] teams, int rows, int baseLength, int gridSize, int cellSize) {
        this.frame = frame;
        this.teams = teams;
        this.rows = rows;
        this.baseLength = baseLength;
        Grid.gridSize = gridSize;
        Grid.cellSize = cellSize;
        frame.addMouseListener(this);

        gridSize = frame.getWidth();
        cellSize = gridSize / rows;
        this.teams = teams;

    }

    public boolean isInBounds(int[] position) {
        for (int val : position) {
            if (!(val >= 0 && val <= rows - 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean isAdjacent(int[] pos1, int[] pos2) {
        if (!Arrays.equals(pos1, pos2) && Math.abs(pos1[0] - pos2[0]) <= 1 && Math.abs(pos1[1] - pos2[1]) <= 1) {
            return true;
        }
        return false;
    }

    public boolean isInTeamBase(int teamNumber, int column) {
        if (teamNumber == 0) {
            if (column < teams[0].getBaseEdge()) {
                return true;
            }
        } else {
            if (column >= teams[1].getBaseEdge()) {
                return true;
            }
        }
        return false;
    }
    
    public void killUnits() {
        for (int i = 0; i < teams.length; i++) {
            Team team = teams[i];
            Iterator<Unit> units = team.getUnits().iterator();
            while (units.hasNext()) {
                Unit unit = units.next();
                if (unit.toBeDeleted()) {
                    units.remove();
                }
            }
        }
    }

    public void markDeletion() {
        boolean unitDeleted = true;
        for (int i = 0; i < teams.length; i++) {
            for (Unit unit : teams[i].getUnits()) {
                unit.resetDeletion();
            }
        }
        while (unitDeleted) {
            unitDeleted = false;
            for (int i = 0; i < teams.length; i++) {
                Team team = teams[i];
                Team opposingTeam = teams[Math.abs(i - 1)];
                for (Unit unit : team.getUnits()) {
                    unit.resetTeamAdjacent();
                    unit.resetEnemyAdjacent();
                    for (Unit teamMate : team.getUnits()) {
                        if (isAdjacent(unit.getPosition(), teamMate.getPosition()) && !teamMate.toBeDeleted()) {
                            unit.incrementTeamAdjacent();
                        }
                    }
                    for (Unit enemy : opposingTeam.getUnits()) {
                        if (isAdjacent(unit.getPosition(), enemy.getPosition())) {
                            unit.incrementEnemyAdjacent();
                        }
                    }

                    if (!unit.toBeDeleted()) {
                        if (unit.getTeamAdjacent() == 0 && unit.getEnemyAdjacent() > 0) {
                            unit.markDeletion();
                            unitDeleted = true;
                        }

                        if (unit.getEnemyAdjacent() >= 3) {
                            unit.markDeletion();
                            unitDeleted = true;
                        }
                    }
                }
            }
        }
    }

    public void resetTeams() {
        teams = new Team[]{new Team(Color.red, baseLength), new Team(Color.blue, rows - baseLength)};
    }

    public void render(Graphics2D g) {
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(255, 0, 0, 100));
        g.fillRect(0, 0, baseLength * cellSize, gridSize);
        g.setColor(new Color(0, 0, 255, 100));
        g.fillRect((teams[1].getBaseEdge()) * cellSize, 0, baseLength * cellSize, gridSize);
        g.setColor(new Color(0, 255, 0, 100));
        g.fillRect((teams[0].getBaseEdge()) * cellSize, 0, (teams[1].getBaseEdge() - teams[0].getBaseEdge()) * cellSize, gridSize);

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.black);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }

        g.setStroke(new BasicStroke(1));
        for (Team team : teams) {
            team.render(g);
        }

        g.setStroke(new BasicStroke(10));
        g.setColor(teams[Game.turn.ordinal()].getTeamColour());
        g.drawLine(0, gridSize + 6, gridSize * Game.turn.getTurns() / 5, gridSize + 6);
        g.setStroke(new BasicStroke(1));

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        int[] mousePosition = new int[]{(mouseLocation.x - frame.getLocationOnScreen().x) / cellSize, (mouseLocation.y - frame.getLocationOnScreen().y) / cellSize};

        g.setColor(Color.yellow);
        int turnNumber = Game.turn.ordinal();
        Team team = teams[turnNumber];
        Team opposingTeam = teams[Math.abs(turnNumber - 1)];
        if (team.unitSelected() && Game.turn.getTurns() > 0) {
            Unit unitSelected = team.getSelectedUnit();
            int[] selectedUnitPos = unitSelected.getPosition();
            g.setStroke(new BasicStroke(2));
            g.drawRect(unitSelected.getPosition()[0] * cellSize, unitSelected.getPosition()[1] * cellSize, cellSize, cellSize);
            g.setStroke(new BasicStroke(1));
            if ((isAdjacent(mousePosition, selectedUnitPos) && isInBounds(mousePosition)) || team.occupied(mousePosition)) {
                g.drawRect(mousePosition[0] * cellSize, mousePosition[1] * cellSize, cellSize, cellSize);
            }

        } else if (isInBounds(mousePosition) && Game.turn.getTurns() > 0) {
            if (team.occupied(mousePosition)) {
                g.drawRect(mousePosition[0] * cellSize, mousePosition[1] * cellSize, cellSize, cellSize);
            }
            if (!opposingTeam.occupied(mousePosition) && isInTeamBase(turnNumber, mousePosition[0])) {
                boolean baseOccupied = false;
                for (Unit unit : opposingTeam.getUnits()) {
                    if (isInTeamBase(turnNumber, unit.getPosition()[0])) {
                        baseOccupied = true;
                        break;
                    }
                }
                if (!baseOccupied) {
                    g.drawRect(mousePosition[0] * cellSize, mousePosition[1] * cellSize, cellSize, cellSize);
                }

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int[] clickPos = {(me.getLocationOnScreen().x - frame.getLocationOnScreen().x) / cellSize, (me.getLocationOnScreen().y - frame.getLocationOnScreen().y) / cellSize};
        if (isInBounds(clickPos) && Game.turn.getTurns() > 0) {
            int turnNumber = Game.turn.ordinal();
            Team team = teams[turnNumber];
            Team opposingTeam = teams[Math.abs(turnNumber - 1)];
            if (!opposingTeam.occupied(clickPos)) {
                if (team.occupied(clickPos)) {
                    team.unitClicked(clickPos);
                } else if (team.unitSelected()) {
                    int[] selectedUnitPos = team.getSelectedUnit().getPosition();
                    if (isAdjacent(clickPos, selectedUnitPos)) {
                        team.moveUnit(selectedUnitPos, clickPos);
                        Game.turn.turnUsed();
                    }
                } else if (!opposingTeam.occupied(clickPos) && isInTeamBase(turnNumber, clickPos[0])) {
                    boolean baseOccupied = false;
                    for (Unit unit : opposingTeam.getUnits()) {
                        if (isInTeamBase(turnNumber, unit.getPosition()[0])) {
                            baseOccupied = true;
                            break;
                        }
                    }
                    if (!baseOccupied) {
                        team.addUnit(clickPos);
                        Game.turn.turnUsed();
                    }

                }
                markDeletion();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}
