package game_logic;

import data_model.Cell;
import data_model.CellType;
import data_model.Piece;
import data_model.PlayerType;

import java.util.*;

public class Board {
    private Cell[] cells;
    private List<Piece> whiteP;
    private List<Piece> blackP;
    
    public Board() {
        cells = new Cell[31];
        for (int i = 1; i <= 30; i++) {
            cells[i] = new Cell(i);
        }

        whiteP = new ArrayList<>();
        blackP = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            Piece whitePiece = new Piece(i + 1, PlayerType.WHITE, 2 * i + 1);
            whiteP.add(whitePiece);
            cells[2 * i + 1].setOccupant(whitePiece);
            Piece blackPiece = new Piece(i + 1, PlayerType.BLACK, 2 * i + 2);
            blackP.add(blackPiece);
            cells[2 * i + 2].setOccupant(blackPiece);
        }
    }

    public Board(Board other) {
        cells = new Cell[31];
        for (int i = 1; i <= 30; i++) {
            cells[i] = new Cell(other.cells[i]);
        }
        
        whiteP = new ArrayList<>();
        blackP = new ArrayList<>();

        for (Piece p : other.whiteP) {
            Piece newP = new Piece(p);
            whiteP.add(newP);
            if (newP.getPosition() > 0 && newP.getPosition() <= 30) {
                cells[newP.getPosition()].setOccupant(newP);
            }
        }
        
        for (Piece p : other.blackP) {
            Piece newP = new Piece(p);
            blackP.add(newP);
            if (newP.getPosition() > 0 && newP.getPosition() <= 30) {
                cells[newP.getPosition()].setOccupant(newP);
            }
        }
    }

public Piece findP(int id, PlayerType owner) {
    List<Piece> pieces = owner == PlayerType.WHITE ? whiteP : blackP;
    for (Piece p : pieces) {
        if (p.getId() == id) {
            return p;
        }
    }
    return null;
}

    public Cell getC(int index) {
        if (index < 1 || index > 30) return null;
        return cells[index];
    }


    public Piece getPositionAt(int position) {
        if (position < 1 || position > 30) return null;
        return cells[position].getOccupant();
    }
    
    public List<Piece> getPs(PlayerType player) {
        return player == PlayerType.WHITE ?
            new ArrayList<>(whiteP) : new ArrayList<>(blackP);
    }

    public List<Piece> getPieces(PlayerType player) {
        return getPs(player);
    }

    public boolean isSquareOccupied(int position) {
        if (position < 1 || position > 30) return false;
        return !cells[position].isEmpty();
    }
    

    public boolean isGameOver() {
        return isPlayerFinished(PlayerType.WHITE) ||
               isPlayerFinished(PlayerType.BLACK);
    }
    

    public boolean isPlayerFinished(PlayerType player) {
        List<Piece> pieces = getPs(player);
        for (Piece p : pieces) {
            if (!p.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public PlayerType getWinner() {
        if (isPlayerFinished(PlayerType.WHITE)) return PlayerType.WHITE;
        if (isPlayerFinished(PlayerType.BLACK)) return PlayerType.BLACK;
        return null;
    }

    public int countPiecesOnBoard(PlayerType player) {
        int count = 0;
        for (Piece p : getPs(player)) {
            if (p.isOnBoard()) count++;
        }
        return count;
    }

    public int calculateProgress(PlayerType player) {
        int progress = 0;
        for (Piece p : getPs(player)) {
            if (p.isFinished()) {
                progress += 35;
            } else if (p.isOnBoard()) {
                progress += p.getPosition();
            }
        }
        return progress;
    }


    public void printBoard() {
                            
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println(  "║                      Senet Game Board                        ║");
        System.out.println(  "╠══════════════════════════════════════════════════════════════╣");
        

        System.out.print("║ ");
        for (int i = 1; i <= 10; i++) {
            printCell(i);
        }
        System.out.println(" ║");
                         

        System.out.println(  "║                                                              ║");
        System.out.println(  "╠──────────────────────────────────────────────────────────────╣");
        

        System.out.print("║ ");
        for (int i = 20; i >= 11; i--) {
            printCell(i);
        }
        System.out.println(" ║");
        

       System.out.println(   "║                                                              ║");
        System.out.println(  "╠──────────────────────────────────────────────────────────────╣");
        

        System.out.print("║ ");
        for (int i = 21; i <=30; i++) {
            printCell(i);
        }
        System.out.println(" ║");
        

          System.out.println("║                                                              ║");
        
        System.out.println(  "╚══════════════════════════════════════════════════════════════╝");

        printStatistics();

    }
    
    private void printCell(int index) {
        Cell cell = cells[index];
        String content;
        
        if (cell.isEmpty()) {

            CellType ct = cell.getType();
            if (ct == CellType.REBIRTH) {
                content = " ♆ ";  // House of Rebirth (15)
            } else if (ct == CellType.HAPPINESS) {
                content = " ☺ ";  // House of Happiness (26)
            } else if (ct == CellType.WATER) {
                content = " ≈ ";  // House of Water (27)
            } else if (ct == CellType.THREE_TRUTHS) {
                content = " ⚔ ";  // House of Three Truths (28)
            } else if (ct == CellType.RE_ATOUM) {
                content = " ☥ ";  // House of Re-Atoum (29)
            } else if (ct == CellType.HORUS) {
                content = " ⊕ ";  // House of Horus (30)
            } else {
                content = " · ";
            }
        } else {
            Piece p = cell.getOccupant();
            String symbol = p.getOwner() == PlayerType.WHITE ? " ●" : " ○";
            content = symbol  ;
            if (content.length() < 4) content += " ";
        }
        
        System.out.print(" [" + content + "]");
    }
    
    private void printStatistics() {
        int whiteOnBoard = countPiecesOnBoard(PlayerType.WHITE);
        int blackOnBoard = countPiecesOnBoard(PlayerType.BLACK);
        int whiteFinished = 7 - whiteOnBoard;
        int blackFinished = 7 - blackOnBoard;
        int whiteProgress = calculateProgress(PlayerType.WHITE);
        int blackProgress = calculateProgress(PlayerType.BLACK);
                              
        System.out.println(  "\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println(    "│                         Statistics                          │");
        System.out.println(    "├─────────────────────────────────────────────────────────────┤");
        System.out.printf("│ White Player (●): %d on board, %d finished, progress: %d      │\n",
            whiteOnBoard, whiteFinished, whiteProgress);
        System.out.printf("│ Black Player (○): %d on board, %d finished, progress: %d      │\n",
            blackOnBoard, blackFinished, blackProgress);
        System.out.println(    "└─────────────────────────────────────────────────────────────┘");
    }

    public void printPiecesInfo(PlayerType player) {
        System.out.println("\n=== Piece Information for " + player.name() + " ===");
        List<Piece> pieces = getPs(player);
        for (Piece p : pieces) {
            System.out.println(p.getDetailedInfo());
        }
    }
}
