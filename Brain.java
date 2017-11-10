// Brain.java -- the interface for Tetris brains

public interface Brain {
    public static class Weights {
        public double maxHeightWeight;
        public double heightDiffWeight;
        public double avgHeightWeight;
        public double numHolesWeight;
        public double numTilesWeight;
        public double aggrHeightWeight;
        public double compLinesWeight;
        public double bumpinessWeight;
        
        public void setWeights(double w1, double w2, double w3, double w4, double w5, double w6, double w7, double w8) {
            this.maxHeightWeight = w1;
            this.heightDiffWeight = w2;
            this.avgHeightWeight = w3;
            this.numHolesWeight = w4;
            this.numTilesWeight = w5;
            this.aggrHeightWeight = w6;
            this.compLinesWeight = w7;
            this.bumpinessWeight = w8;
        }
    }
    

    // Move is used as a struct to store a single Move
    // ("static" here means it does not have a pointer to an
    // enclosing Brain object, it's just in the Brain namespace.)
    public static class Move {
        public int x;
        public int y;
        public Piece piece;
        public double score; // lower scores are better
    }

    /**
    Given a piece and a board, returns a move object that represents
    the best play for that piece, or returns null if no play is possible.
    The board should be in the committed state when this is called.
    "limitHeight" is the bottom section of the board that where pieces must
    come to rest -- typically 20.
    If the passed in move is non-null, it is used to hold the result
    (just to save the memory allocation).
     */
    public Brain.Move bestMove(Board board, Piece piece, int limitHeight, Brain.Move move);
}

