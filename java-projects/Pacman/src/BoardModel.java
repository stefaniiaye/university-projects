import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BoardModel extends AbstractTableModel {
    int rows;
    int columns;
    Object[][] board;

    public BoardModel(int c, int r) {
        this.rows = r;
        this.columns = c;
        this.board = new Object[rows][columns];
        initBoard();
        removeDeadEnds();
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return board[rowIndex][columnIndex];
    }
    public String getColumnName(int column) {
        return "";
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        board[rowIndex][columnIndex] = aValue;
    }


    public void initBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = boardFigures.WALL;
            }
        }

        generateMaze((int)(Math.random() * rows), (int)(Math.random() * columns));
        for (int i = 0; i < rows; i++) {
            board[i][0] = boardFigures.WALL;
            board[i][columns-1] = boardFigures.WALL;
        }
        for (int i = 0; i < columns; i++) {
            board[0][i] = boardFigures.WALL;
            board[rows-1][i] = boardFigures.WALL;
        }
        int pacRow = rows / 2;
        int pacCol = columns / 2;
        board[pacRow][pacCol] = boardFigures.PACMAN;

        for (int i = 0; i < 4; i++) {
            int ghostRow;
            int ghostCol;

            do {
                ghostRow = (int)(Math.random() * rows);
                ghostCol = (int)(Math.random() * columns);
            } while (board[ghostRow][ghostCol] != boardFigures.EMPTY);

            board[ghostRow][ghostCol] = boardFigures.GHOST;
        }

        int fruitNum = 0;
        if(columns > 50 || rows > 50){
            fruitNum = (int)(Math.random()*5+6);
        }else fruitNum = (int)(Math.random()*4+3);

        for (int i = 0; i < fruitNum; i++) {
            int fruitRow;
            int fruitCol;
            do {
                fruitRow = (int)(Math.random() * rows);
                fruitCol = (int)(Math.random() * columns);
            } while (board[fruitRow][fruitCol] != boardFigures.EMPTY);

            board[fruitRow][fruitCol] = boardFigures.FRUIT;
        }
    }

    private void generateMaze(int row, int col) {
        board[row][col] = boardFigures.EMPTY;
        List<int[]> neighbors = getRandomNeighbors(row, col);


        while (!neighbors.isEmpty()) {
            int[] neighbor = neighbors.remove((int)(Math.random() * neighbors.size()));
            int neighborRow = neighbor[0];
            int neighborCol = neighbor[1];

            if (isValidWall(neighborRow, neighborCol)) {
                int passageRow = row + (neighborRow - row) / 2;
                int passageCol = col + (neighborCol - col) / 2;
                board[passageRow][passageCol] = boardFigures.EMPTY;
                generateMaze(neighborRow, neighborCol);
            }
        }

    }

    private List<int[]> getRandomNeighbors(int row, int col) {
        List<int[]> neighbors = new ArrayList<>();
        int[][] directions = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};

        for (int[] direction : directions) {
            int neighborRow = row + direction[0];
            int neighborCol = col + direction[1];

            if (isValidCell(neighborRow, neighborCol)) {
                neighbors.add(new int[]{neighborRow, neighborCol});
            }
        }
        return neighbors;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 1 && row < rows-1 && col >= 1 && col < columns-1;
    }

    private boolean isValidWall(int row, int col) {
        return isValidCell(row, col) && board[row][col] == boardFigures.WALL;
    }

    public void removeDeadEnds() {
        boolean removedDeadEnds = true;

        while (removedDeadEnds) {
            removedDeadEnds = false;

            for (int i = 1; i < rows - 1; i++) {
                for (int j = 1; j < columns - 1; j++) {
                    List<Integer[]> surroundingWallsList = new ArrayList<>();
                    int surroundingWalls = 0;

                    if (board[i - 1][j] == boardFigures.WALL) {
                        surroundingWalls++;
                        if( (i - 1) != 0) surroundingWallsList.add(new Integer[]{i - 1, j});
                    }

                    if (board[i + 1][j] == boardFigures.WALL) {
                        surroundingWalls++;
                        if((i + 1) != (rows - 1))surroundingWallsList.add(new Integer[]{i + 1, j});
                    }

                    if (board[i][j - 1] == boardFigures.WALL) {
                        surroundingWalls++;
                        if((j - 1) != 0)surroundingWallsList.add(new Integer[]{i, j - 1});
                    }

                    if (board[i][j + 1] == boardFigures.WALL) {
                        surroundingWalls++;
                        if((j + 1) != (columns - 1))surroundingWallsList.add(new Integer[]{i, j + 1});
                    }

                    if (surroundingWalls >= 3) {
                        Integer[] randomWallToDelete = surroundingWallsList.get((int) (Math.random() * surroundingWallsList.size()));
                        board[randomWallToDelete[0]][randomWallToDelete[1]] = boardFigures.EMPTY;
                        removedDeadEnds = true;
                    }
                }
            }
        }
    }


}
