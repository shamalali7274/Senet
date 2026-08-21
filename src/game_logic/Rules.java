package game_logic;

import data_model.*;

import java.util.*;


public class Rules {

    public static List<Move> getValidMoves(Board board, PlayerType player, int diceValue) {
        List<Move> validMoves = new ArrayList<>();
        List<Piece> pieces = board.getPs(player);
        
        for (Piece piece : pieces) {

            if (piece.isFinished()) {
                continue;
            }
            
            Move move = canMovePiece(board, piece, diceValue);
            if (move != null) {
                validMoves.add(move);
            }
        }
        
        return validMoves;
    }



public static Move canMovePiece(Board board, Piece piece, int steps) {
    if (piece.isFinished()) {
        return null;
    }

    int currentPos = piece.getPosition();
    CellType currentCellType = board.getC(currentPos).getType();

    if (currentCellType == CellType.THREE_TRUTHS) {
        if (steps == 3) {

            return new Move(piece, currentPos, 31, steps, false, true);
        }

        return null;
    }


    if (currentCellType == CellType.RE_ATOUM) {
        if (steps == 2) {
            return new Move(piece, currentPos, 31, steps, false, true);
        }

        return null;
    }


    if (currentCellType == CellType.HORUS) {
        return new Move(piece, currentPos, 31, steps, false, true);
    }

    int targetPos = currentPos + steps;


    if (targetPos > 30) {
        if (currentPos >= 26) {
            return new Move(piece, currentPos, targetPos, steps, false, true);
        }

        return null;
    }

    if (currentPos < 26 && targetPos > 26) {
        return null;
    }

    if (targetPos < 1) {
        return null;
    }

    Cell targetCell = board.getC(targetPos);

    if (targetCell.isEmpty()) {

        return new Move(piece, currentPos, targetPos, steps, false, false);
    } else {
        Piece occupant = targetCell.getOccupant();

        if (occupant.getOwner() == piece.getOwner()) {

            return null;
        } else {

            return new Move(piece, currentPos, targetPos, steps, true, false);
        }
    }
}


 public static void checkAndPenalizeSpecialHouses(Board board, PlayerType player) {
     List<Piece> pieces = board.getPs(player);

     for (Piece piece : pieces) {
         if (piece.getNeedsExactRoll() != null) {

             int position = piece.getPosition();
             CellType cellType = board.getC(position).getType();

             if (cellType == CellType.THREE_TRUTHS ||
                 cellType == CellType.RE_ATOUM ||
                 cellType == CellType.HORUS) {

                 System.out.println("→ " + piece + " didn't get the correct roll, returning to rebirth house");
                 returnToRebirth(board, piece);
             }
         }
     }
 }

public static boolean applyMove(Board board, Move move, boolean silent) {
    if (move == null) return false;

    Piece piece = move.getPiece();
    int fromPos = move.getFromPosition();

    if (move.isExit()) {
        if (fromPos > 0 && fromPos <= 30) {
            board.getC(fromPos).setOccupant(null);
        }
        piece.setPosition(0);
        piece.setState(PieceState.FINISHED);
        piece.setNeedsExactRoll(null);
        
        if (!silent) {
            System.out.println("→ " + piece + " finished and exited the board!");
        }
        return true;
    }

    int toPos = move.getToPosition();

    if (move.isSwap()) {
        Piece opponent = board.getC(toPos).getOccupant();
        if (opponent == null) return false;

        board.getC(fromPos).setOccupant(null);
        board.getC(toPos).setOccupant(null);

        piece.setPosition(toPos);
        opponent.setPosition(fromPos);

        if (piece.getNeedsExactRoll() != null) {
            piece.setNeedsExactRoll(null);
            piece.setState(PieceState.ON_BOARD);
        }

        board.getC(toPos).setOccupant(piece);
        board.getC(fromPos).setOccupant(opponent);

        applySpecialCellRules(board, piece, toPos, silent);
        applySpecialCellRules(board, opponent, fromPos, silent);

        return true;
    }

    if (fromPos > 0 && fromPos <= 30) {
        board.getC(fromPos).setOccupant(null);
    }

    piece.setPosition(toPos);

    if (piece.getNeedsExactRoll() != null) {
        piece.setNeedsExactRoll(null);
        piece.setState(PieceState.ON_BOARD);
    }

    board.getC(toPos).setOccupant(piece);
    applySpecialCellRules(board, piece, toPos, silent);

    return true;
}


public static boolean applyMove(Board board, Move move) {
    return applyMove(board, move, true); // silent mode للمحاكاة
}

private static void applySpecialCellRules(Board board, Piece piece, int position, boolean silent) {
    CellType cellType = board.getC(position).getType();
    
    switch (cellType) {
        case WATER:

            returnToRebirth(board, piece);
             if (!silent) {
                System.out.println("→ " + piece + " fall in to the water, your block return to home");
             }
            break;
            
        case THREE_TRUTHS:
              piece.setNeedsExactRoll(3);
           
            piece.setState(PieceState.ON_BOARD);
            if (!silent) {
            System.out.println("→ " + piece + " inside the fact house, need three moves to finish");
            }
            break;
            
            case RE_ATOUM:

            piece.setNeedsExactRoll(2);
            piece.setState(PieceState.ON_BOARD);
            if (!silent) {
            System.out.println("→ " + piece + " inside atom house, need two moves to finish");
            }
            break;
            
         case HORUS:
            piece.setNeedsExactRoll(1);
            piece.setState(PieceState.ON_BOARD);
          if (!silent) {
            System.out.println("→ " + piece + " inside horus house, any move to finish");
          }
            break;
            
        case HAPPINESS:
        if (!silent) {
          System.out.println("→ " + piece + " inside happy house!");
         }
        break;
            
        case REBIRTH:
         if (!silent) {
            System.out.println("→ " + piece + " inside the rebirth house");
         }
            break;
            
        default:
            break;
    }
}


public static void checkSpecialHousePenalties(Board board, PlayerType player) {
    List<Piece> pieces = board.getPs(player);

    for (Piece piece : pieces) {
        if (
            piece.getNeedsExactRoll() != null) {

            int position = piece.getPosition();


            if (position == 28 || position == 29 || position == 30) {

                System.out.println("→ " + piece + " didn't move from special house, returning to rebirth");
                returnToRebirth(board, piece);
            }
        }
    }
}

    private static void returnToRebirth(Board board, Piece piece) {
        int currentPos = piece.getPosition();
        

        if (currentPos > 0 && currentPos <= 30) {
            board.getC(currentPos).setOccupant(null);
        }
        

        int targetPos = 15;
        while (targetPos > 0 && !board.getC(targetPos).isEmpty()) {
            targetPos--;
        }
        
        if (targetPos > 0) {
            piece.setPosition(targetPos);
            piece.setState(PieceState.ON_BOARD);
            piece.setNeedsExactRoll(null);
            board.getC(targetPos).setOccupant(piece);
        } else {

            piece.setPosition(1);
            piece.setState(PieceState.ON_BOARD);
            piece.setNeedsExactRoll(null);
            board.getC(1).setOccupant(piece);
        }
    }
    

    public static boolean isValidMove(Board board, Move move) {
        if (move == null) return false;

        Piece piece = move.getPiece();
        List<Move> validMoves = getValidMoves(board, piece.getOwner(), move.getSteps());

        for (Move valid : validMoves) {
            if (valid.getPiece().getId() == piece.getId() &&
                valid.getFromPosition() == move.getFromPosition() &&
                valid.getToPosition() == move.getToPosition()) {
                return true;
            }
        }

        return false;
    }


    public static Board cloneBoard(Board board) {
        return new Board(board);
    }

public static Board applyMoveCloned(Board board, Move move) {
    Board newBoard = cloneBoard(board);

    Piece clonedPiece = newBoard.findP(
            move.getPiece().getId(),
            move.getPiece().getOwner()
    );

    Move clonedMove = new Move(
            clonedPiece,
            move.getFromPosition(),
            move.getToPosition(),
            move.getSteps(),
            move.isSwap(),
            move.isExit()
    );

    applyMove(newBoard, clonedMove, true); // true = silent mode
    return newBoard;
}
 
    

    public static void printValidMoves(List<Move> moves) {
        if (moves.isEmpty()) {
            System.out.println("their isn't any avilable moves");
            return;
        }
        
        System.out.println("\n=== possible moves ===");
        for (int i = 0; i < moves.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, moves.get(i).getDetailedDescription());
        }
    }


public static void penalizeSpecialHouses(Board board, PlayerType player, Move appliedMove,boolean silent) {
    List<Piece> pieces = board.getPs(player);
    
    for (Piece piece : pieces) {
        if (piece.isFinished()) {
            continue;
        }
        

        if (appliedMove != null && piece.getId() == appliedMove.getPiece().getId()) {
            continue;
        }
        
        int position = piece.getPosition();
        CellType cellType = board.getC(position).getType();
        

        if (cellType == CellType.THREE_TRUTHS || 
            cellType == CellType.RE_ATOUM || 
            cellType == CellType.HORUS) {
            
    if (!silent) {
       System.out.println("→ " + piece + " didn't move from special house, returning to rebirth");
        }
           returnToRebirth(board, piece);
        }
    }
}
}