package game_logic;

import data_model.Move;
import data_model.Piece;
import data_model.PlayerType;

public class GameState {

    private Board board;
    private PlayerType currentPlayer;
    private Integer diceValue;
    private int turnNumber;

    public GameState(Board board, PlayerType currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.diceValue = null;
        this.turnNumber = 1;
    }

    public GameState(GameState other) {
        this.board = new Board(other.board);
        this.currentPlayer = other.currentPlayer;
        this.diceValue = other.diceValue;
        this.turnNumber = other.turnNumber;
    }


    public Board getBoard() {
        return board;
    }

    public PlayerType getCurrentPlayer() {
        return currentPlayer;
    }

    public Integer getDiceValue() {
        return diceValue;
    }

    public int getTurnNumber() {
        return turnNumber;
    }


    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentPlayer(PlayerType player) {
        this.currentPlayer = player;
    }

    public void setDiceValue(Integer value) {
        this.diceValue = value;
    }

    public void setTurnNumber(int turn) {
        this.turnNumber = turn;
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }
        public int getCost(PlayerType player) {
        return Cost.calculateTotalCost(board, player);
    }
    
    public double getCumulativeCost(PlayerType player) {
        return Cost.calculateCumulativeCost(this, player);
    }
    
    public void printCostInfo(PlayerType player) {
        Cost.printCostDetails(this, player);
    }


    public PlayerType getWinner() {
        return board.getWinner();
    }

    public void changeTurn() {
        currentPlayer = currentPlayer.getOpponent();
        diceValue = null;
        turnNumber++;
    }


    @Override
    public GameState clone() {
        return new GameState(this);
    }





public void applyMove(Move move) {
    applyMove(move, true);
}
public void applyMove(Move move, boolean silent) {
    Piece pieceInThisBoard = board.findP(
        move.getPiece().getId(), 
        move.getPiece().getOwner()
    );
    
    if (pieceInThisBoard == null) {
        Rules.applyMove(board, move, silent);
        return;
    }
    
    Move correctedMove = new Move(
        pieceInThisBoard,
        move.getFromPosition(),
        move.getToPosition(),
        move.getSteps(),
        move.isSwap(),
        move.isExit()
    );
    
    Rules.applyMove(board, correctedMove, silent);
}
    public java.util.List<Move> getValidMoves() {
        if (diceValue == null) {
            return new java.util.ArrayList<>();
        }
        return Rules.getValidMoves(board, currentPlayer, diceValue);
    }

    public void printState() {
        System.out.println("\n┌───────────────────────────────────────┐");
        System.out.printf(
            "│ Turn #%d - Player: %s %s           │\n",
            turnNumber,
            currentPlayer.name(),
            currentPlayer == PlayerType.WHITE ? "(●)" : "(○)"
        );

        if (diceValue != null) {
            System.out.printf("│ Roll: %d                               │\n", diceValue);
        }

        System.out.println("└───────────────────────────────────────┘");
    }

    @Override
    public String toString() {
        return String.format(
            "GameState[Turn=%d, Player=%s, Dice=%s]",
            turnNumber,
            currentPlayer.name(),
            diceValue == null ? "?" : diceValue
        );
    }
}
