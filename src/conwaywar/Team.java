package conwaywar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Team {

    private ArrayList<Unit> units;
    private ArrayList<Unit> resetUnits;
    private Color teamColour;
    private int baseEdge;
    private int size;

    public Team(Color teamColour, int baseEdge) {
        units = new ArrayList<Unit>();
        resetUnits = new ArrayList<Unit>();
        this.teamColour = teamColour;
        this.baseEdge = baseEdge;
    }

    public void render(Graphics2D g) {
        for (Unit unit : units) {
            unit.render(g);
        }
    }

    public void addUnit(int[] position) {
        units.add(new Unit(position, getTeamColour()));
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void moveUnit(int[] position, int[] newPosition) {
        for (Unit unit : units) {
            if (Arrays.equals(unit.getPosition(), position)) {
                unit.setPosition(newPosition);
                unselectUnits();
            }
        }
    }

    public boolean unitSelected() {
        for (Unit unit : units) {
            if (unit.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public Unit getSelectedUnit() {
        for (Unit unit : units) {
            if (unit.isSelected()) {
                return unit;
            }
        }
        return null;
    }

    public void unitClicked(int[] position) {
        for (Unit unit : units) {
            if (Arrays.equals(unit.getPosition(), position)) {
                if (unit.isSelected()) {
                    unit.unSelect();
                } else {
                    unit.select();
                }
            } else {
                unit.unSelect();
            }
        }
    }

    public void unselectUnits() {
        for (Unit unit : units) {
            if (unit.isSelected()) {
                unit.unSelect();
            }
        }
    }

    public boolean occupied(int[] position) {
        for (Unit unit : units) {
            if (Arrays.equals(unit.getPosition(), position)) {
                return true;
            }
        }
        return false;
    }

    public void resetUnits() {
        units = new ArrayList<>();
        for(Unit unit : resetUnits){
            units.add(unit.clone());
        }
        Game.turn.resetTurns();
    }

    public void newRound() {
        resetUnits = new ArrayList<Unit>();
        for(Unit unit : units){
            resetUnits.add(unit.clone());
        }
        Game.turn.resetTurns();
    }

    public Color getTeamColour() {
        return teamColour;
    }

    public int getBaseEdge() {
        return baseEdge;
    }
}
