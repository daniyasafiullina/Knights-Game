import java.awt.*;

public class Board {
    private Field[][] fields;
    private int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        fields = new Field[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                fields[i][j] = new Field();
            }
        }
    }

    public Field getField(int x, int y) {
        return fields[x][y];
    }

    public void setFieldColor(int x, int y, Color color) {
        if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            fields[x][y].setColor(color);
        }
    }


    public int getBoardSize() {
        return boardSize;
    }
}
