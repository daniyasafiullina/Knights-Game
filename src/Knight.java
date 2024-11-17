import java.awt.Color;
import java.awt.Point;

public class Knight {
    private Color color;
    private Point position;

    public Knight(Color color, Point position) {
        this.color = color;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean canMoveTo(Point newPosition, int boardSize) {
        int dx = Math.abs(newPosition.x - position.x);
        int dy = Math.abs(newPosition.y - position.y);
        return (dx == 2 && dy == 1 || dx == 1 && dy == 2) &&
                newPosition.x >= 0 && newPosition.x < boardSize &&
                newPosition.y >= 0 && newPosition.y < boardSize;
    }
}
