package game_logic;
import java.util.List;
import data_model.Move;
import data_model.Piece;
import data_model.PlayerType;


public class Cost {
    
   
    public static int calculateTotalCost(Board board, PlayerType player) {
        int totalCost = 0;
        List<Piece> pieces = board.getPieces(player);
        
        for (Piece piece : pieces) {
            totalCost += calculatePieceCost(piece);
        }
        
        return totalCost;
    }
   
    public static int calculatePieceCost(Piece piece) {
        if (piece.isFinished()) {
           
            return 2000;  
        }
        
        if (!piece.isOnBoard()) {
            return 0; 
        }
        
       
        int position = piece.getPosition();
        
        int startPosition = getInitialPosition(piece);
        
        return position - startPosition ;
    }
    
 
    private static int getInitialPosition(Piece piece) {
        
        
        int id = piece.getId();  
        
        if (piece.getOwner() == PlayerType.WHITE) {
            return 2 * id - 1;  
        } else {
            return 2 * id;      
        }
    }
    
  
    public static int calculateMoveCost(Move move) {
        return move.getSteps();
    }
    
   
    public static double calculateCumulativeCost(GameState state, PlayerType player) {
        Board board = state.getBoard();
        
        double baseCost = calculateTotalCost(board, player);
        
        double turnCost = state.getTurnNumber() * 0.5;
        
        return baseCost + turnCost;
    }
    
    public static double calculateAverageCost(Board board, PlayerType player) {
        List<Piece> pieces = board.getPieces(player);
        if (pieces.isEmpty()) return 0;
        
        int totalCost = calculateTotalCost(board, player);
        return (double) totalCost / pieces.size();
    }
    
   
    public static void printCostDetails(GameState state, PlayerType player) {
        Board board = state.getBoard();
        
        System.out.println("\n┌──────────────────────────────────────────────────┐");
        System.out.println(  "│                  Cost Details                    │");
        System.out.println(  "├──────────────────────────────────────────────────┤");
        System.out.printf("│    Player: %-37s │\n", player.name());
        System.out.printf("│     Total Cost: %-32d │\n", 
            calculateTotalCost(board, player));
        System.out.printf("│  Average Cost: %-33.2f │\n", 
            calculateAverageCost(board, player));
        System.out.printf("│   Turn Number: %-33d │\n", 
            state.getTurnNumber());
        System.out.printf("│  Cumulative Cost: %-30.2f │\n", 
            calculateCumulativeCost(state, player));
        System.out.println("└──────────────────────────────────────────────────┘");
        
        // تفاصيل كل قطعة
        System.out.println("\nCost per Piece:");
        List<Piece> pieces = board.getPieces(player);
        for (Piece p : pieces) {
            int cost = calculatePieceCost(p);
            System.out.printf("  %s: Cost = %d (Position: %d)\n", 
                p.toString(), cost, p.getPosition());
        }
    }
}
