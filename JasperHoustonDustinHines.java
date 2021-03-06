/**
A Brain implementation created by Jasper Houston and Dustin Hines.
bestMove() iterates through all the possible x values
and rotations to play a particular piece (there are only
around 10-30 ways to play a piece).

For each play, it uses the rateBoard() message to rate how
good the resulting board is and it just remembers the
play with the lowest score. Undo() is used to back-out
each play before trying the next. To experiment with writing your own
brain -- just subclass off LameBrain and override rateBoard().
 */

import static java.lang.Math.*;

public class JasperHoustonDustinHines implements Brain {
    /**
    Given a piece and a board, returns a move object that represents
    the best play for that piece, or returns null if no play is possible.
    See the Brain interface for details.
     */

    public static Brain.Weights weights = new Brain.Weights();

    public Brain.Move bestMove(Board board, Piece piece, int limitHeight, Brain.Move move) {
        // Allocate a move object if necessary
        if (move==null) move = new Brain.Move();

        double bestScore = 1e20;
        int bestX = 0;
        int bestY = 0;
        Piece bestPiece = null;
        Piece current = piece;

        // loop through all the rotations
        while (true) {
            final int yBound = limitHeight - current.getHeight()+1;
            final int xBound = board.getWidth() - current.getWidth()+1;

            // For current rotation, try all the possible columns
            for (int x = 0; x<xBound; x++) {
                int y = board.dropHeight(current, x);
                if (y<yBound) { // piece does not stick up too far
                    int result = board.place(current, x, y);
                    if (result <= Board.PLACE_ROW_FILLED) {
                        if (result == Board.PLACE_ROW_FILLED) board.clearRows();

                        double score = rateBoard(board);

                        if (score<bestScore) {
                            bestScore = score;
                            bestX = x;
                            bestY = y;
                            bestPiece = current;
                        }
                    }

                    board.undo(); // back out that play, loop around for the next
                }
            }

            current = current.nextRotation();
            if (current == piece) break; // break if back to original rotation
        }

        if (bestPiece == null) return(null); // could not find a play at all!
        else {
            move.x=bestX;
            move.y=bestY;
            move.piece=bestPiece;
            move.score = bestScore;
            return(move);
        }
    }

    /*
    A simple brain function.
    Given a board, produce a number that rates
    that board position -- larger numbers for worse boards.
    This version just counts the height
    and the number of "holes" in the board.
    See Tetris-Architecture.html for brain ideas.
     */
    public double rateBoard(Board board) {
        final int width = board.getWidth();
        final int maxHeight = board.getMaxHeight();

        int aggrHeight = 0;
        int numHoles = 0;
        int minHeight = board.getHeight();

        // Count the holes, and sum up the heights
        for (int x = 0; x < width; x++) {
            final int colHeight = board.getColumnHeight(x);
            aggrHeight += colHeight;

            if (colHeight < minHeight) {
                minHeight = colHeight;
            }

            int y = colHeight - 2; // addr of first possible hole
            while (y>=0) {
                if (!board.getGrid(x,y)) {
                    numHoles++;
                }
                y--;
            }
        }

        // calculates the number of complete lines
        int compLines = 0;
        for (int i = 0; i < board.getColumnHeight(0); i++) {
            int flag = 1;
            for (int col = 0; col < width; col++) {
                if (!board.getGrid(col,i)) {
                    flag = 0;
                }
            }
            if (flag == 1) {
                compLines++;
            }
        }

        int bumpiness = 0;
        for (int x = 0; x < width - 1; x++) {
            int colHeight = board.getColumnHeight(x);
            int nextColHeight = board.getColumnHeight(x+1);
            int diff = abs(colHeight - nextColHeight);
            bumpiness += diff;
        }

        int heightDiff = maxHeight - minHeight;

        /*
         * avg to 869.76 from PSO
         * weights.maxHeightWeight = -0.41398136482995906;
         * weights.numHolesWeight = 0.6054612115863476;
         * weights.heightDiffWeight = 0.16080502183459777;
         * weights.aggrHeightWeight = 0.6490862772559587;
         * weights.compLinesWeight = 0.1582071590848;
         * weights.bumpinessWeight = 0.31052851683032534;
         * 
         * After running Hill Climbing for a while
         * weights.maxHeightWeight = -223981;
         * weights.numHolesWeight = 855461;
         * weights.heightDiffWeight = 210805;
         * weights.aggrHeightWeight = 679086;
         * weights.compLinesWeight = -248207;
         * weights.bumpinessWeight = 300528;
         * 
         * just hill climbing
         * 
        */
       
        weights.maxHeightWeight = -223981;
        weights.numHolesWeight = 855461;
        weights.heightDiffWeight = 210805;
        weights.aggrHeightWeight = 679086;
        weights.compLinesWeight = -248207;
        weights.bumpinessWeight = 300528;

        // Add up the counts to make an overall score
        double boardRating = (weights.maxHeightWeight*maxHeight + weights.numHolesWeight*numHoles +
                weights.heightDiffWeight*heightDiff + weights.aggrHeightWeight*aggrHeight +
                weights.compLinesWeight*compLines + weights.bumpinessWeight*bumpiness);
        return (boardRating);

    }
}