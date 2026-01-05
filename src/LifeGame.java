public class LifeGame {
    private char[][] board;
    int unit = 0, gen = 0;

    public LifeGame(int row, int col) {
        if (row <= 0 || col <= 0) throw new IllegalArgumentException("Dimensions must be positive");
        board = new char[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                board[i][j] = ' ';
    }

    public LifeGame(char[][] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Input array is empty");
        
        int rows = arr.length;
        int cols = arr[0].length;
        board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            if (arr[i] == null) throw new IllegalArgumentException("Row " + i + " is null");
            // Defensive copying and validation for jagged arrays
            int lengthToCopy = Math.min(arr[i].length, cols);
            System.arraycopy(arr[i], 0, board[i], 0, lengthToCopy);
            
            // Fill remaining if row was shorter
            for (int k = lengthToCopy; k < cols; k++) {
                board[i][k] = ' ';
            }
        }
        numberOfUnit();
    }

    public void set(int i, int j, char val) {
        if (i >= 0 && i < board.length && j >= 0 && j < board[0].length) {
            board[i][j] = val;
        }
    }

    public void nextGeneration() {
        gen++;
        char[][] nextBoard = new char[board.length][board[0].length];
        for (int i = 0; i < nextBoard.length; i++)
            for (int j = 0; j < nextBoard[0].length; j++)
                nextBoard[i][j] = ' ';
                
        int[] dx = {1, 1, -1, -1, -1, 0, 1, 0};
        int[] dy = {-1, 1, -1, 1, 0, -1, 0, 1};
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int cnt = 0;
                for (int k = 0; k < dx.length; k++) {
                    int ni = i + dy[k];
                    int nj = j + dx[k];
                    
                    if (ni >= 0 && ni < board.length && nj >= 0 && nj < board[0].length) {
                        if (board[ni][nj] == '@') cnt++;
                    }
                }
                
                if (cnt == 3) nextBoard[i][j] = '@';
                else if (cnt == 2) nextBoard[i][j] = board[i][j];
                else nextBoard[i][j] = ' ';
            }
        }
        board = nextBoard;
        numberOfUnit();
    }

    private void numberOfUnit() {
        unit = 0;
        for (char[] chars : board)
            for (char i : chars)
                if (i == '@') unit++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("generation: ").append(gen).append("\n");
        result.append("number of unit: ").append(unit).append("\n");
        result.append("-".repeat(board[0].length)).append("\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
                result.append(board[i][j]);
            if (i < board.length - 1) result.append("\n");
        }
        result.append("\n").append("-".repeat(board[0].length));
        return result.toString();
    }
}