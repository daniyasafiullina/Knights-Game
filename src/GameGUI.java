import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameGUI extends JFrame {
    private Board board;
    private List<Knight> whiteKnights;
    private List<Knight> blackKnights;
    private int boardSize;
    private JButton[][] buttons;
    private boolean whiteTurn;
    private Knight selectedKnight; // The currently selected knight
    private final Border cellBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
    private final Border highlightBorder = BorderFactory.createLineBorder(Color.YELLOW, 2);
    private final Border selectedBorder = BorderFactory.createLineBorder(Color.GREEN, 2);

    public GameGUI() {
        initializeGame();
    }

    private void initializeGame() {
        String[] options = {"4x4", "6x6", "8x8"};
        String choice = (String) JOptionPane.showInputDialog(null, "Choose board size:",
                "Board Size", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        boardSize = Integer.parseInt(choice.split("x")[0]);
        board = new Board(boardSize);

        whiteKnights = new ArrayList<>();
        blackKnights = new ArrayList<>();

        whiteKnights.add(new Knight(Color.WHITE, new Point(0, 0)));
        whiteKnights.add(new Knight(Color.WHITE, new Point(0, boardSize - 1)));
        blackKnights.add(new Knight(Color.BLACK, new Point(boardSize - 1, 0)));
        blackKnights.add(new Knight(Color.BLACK, new Point(boardSize - 1, boardSize - 1)));

        for (Knight knight : whiteKnights) {
            board.getField(knight.getPosition().x, knight.getPosition().y).setOccupied(true);
        }
        for (Knight knight : blackKnights) {
            board.getField(knight.getPosition().x, knight.getPosition().y).setOccupied(true);
        }

        buttons = new JButton[boardSize][boardSize];
        whiteTurn = true;
        selectedKnight = null;

        setLayout(new GridLayout(boardSize, boardSize));
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorder(cellBorder);
                buttons[i][j].setBackground(Color.GRAY);
                buttons[i][j].addActionListener(new CellClickListener(i, j));
                add(buttons[i][j]);
            }
        }

        setKnightPositions();

        setTitle("Knights Game");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setKnightPositions() {
        for (Knight knight : whiteKnights) {
            Point pos = knight.getPosition();
            buttons[pos.x][pos.y].setBackground(Color.WHITE);
            buttons[pos.x][pos.y].setBorder(highlightBorder);
        }
        for (Knight knight : blackKnights) {
            Point pos = knight.getPosition();
            buttons[pos.x][pos.y].setBackground(Color.BLACK);
            buttons[pos.x][pos.y].setBorder(highlightBorder);
        }
    }

    private Knight getKnightAt(int x, int y) {
        List<Knight> knights = whiteTurn ? whiteKnights : blackKnights;
        for (Knight knight : knights) {
            if (knight.getPosition().equals(new Point(x, y))) {
                return knight;
            }
        }
        return null;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!board.getField(i, j).isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWin(int x, int y, Color color) {
        return checkDirection(x, y, color, 1, 0)
                || checkDirection(x, y, color, 0, 1)
                || checkDirection(x, y, color, 1, 1)
                || checkDirection(x, y, color, 1, -1);
    }

    private boolean checkDirection(int x, int y, Color color, int dx, int dy) {
        int count = 1;

        for (int i = 1; i < 4; i++) {
            int nx = x + i * dx;
            int ny = y + i * dy;
            if (nx < 0 || ny < 0 || nx >= boardSize || ny >= boardSize) break;
            if (buttons[nx][ny].getBackground().equals(color)) {
                count++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 4; i++) {
            int nx = x - i * dx;
            int ny = y - i * dy;
            if (nx < 0 || ny < 0 || nx >= boardSize || ny >= boardSize) break;
            if (buttons[nx][ny].getBackground().equals(color)) {
                count++;
            } else {
                break;
            }
        }

        return count >= 4;
    }

    private class CellClickListener implements ActionListener {
        private int x, y;

        public CellClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Knight knight = getKnightAt(x, y);
            if (knight != null && (whiteTurn && knight.getColor() == Color.WHITE || !whiteTurn && knight.getColor() == Color.BLACK)) {
                if (selectedKnight != null) {
                    Point oldPos = selectedKnight.getPosition();
                    buttons[oldPos.x][oldPos.y].setBorder(highlightBorder);
                }
                selectedKnight = knight;
                buttons[x][y].setBorder(selectedBorder);
            } else if (selectedKnight != null && selectedKnight.canMoveTo(new Point(x, y), boardSize) && !board.getField(x, y).isOccupied()) {
                Point oldPos = selectedKnight.getPosition();
                buttons[oldPos.x][oldPos.y].setBackground(selectedKnight.getColor());
                buttons[oldPos.x][oldPos.y].setBorder(cellBorder);
                board.setFieldColor(oldPos.x, oldPos.y, selectedKnight.getColor());

                selectedKnight.setPosition(new Point(x, y));
                buttons[x][y].setBackground(selectedKnight.getColor());
                board.getField(x, y).setOccupied(true);
                buttons[x][y].setBorder(selectedBorder);

                if (checkWin(x, y, selectedKnight.getColor())) {
                    String winner = selectedKnight.getColor() == Color.WHITE ? "White" : "Black";
                    JOptionPane.showMessageDialog(null, winner + " Wins!");
                    System.exit(0);
                }

                if (isBoardFull()) {
                    JOptionPane.showMessageDialog(null, "It's a Draw!");
                    System.exit(0);
                }

                whiteTurn = !whiteTurn;
                for (Knight k : whiteKnights) {
                    Point pos = k.getPosition();
                    buttons[pos.x][pos.y].setBorder(highlightBorder);
                }
                for (Knight k : blackKnights) {
                    Point pos = k.getPosition();
                    buttons[pos.x][pos.y].setBorder(highlightBorder);
                }

                selectedKnight = null;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
