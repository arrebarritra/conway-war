package conwaywar.enums;

public enum Turn {
    redTurn,
    blueTurn;
    
    int turns;
    Turn(){
        turns = 5;
    }
    
    public void turnUsed(){
        turns--;
    }
    
    public int getTurns(){
        return turns;
    }
    
    public void resetTurns(){
        turns = 5;
    }
}
