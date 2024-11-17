import java.awt.Color;

public class Field {
    private Color color;
    private boolean isOccupied;

    public Field() {
        color = Color.GRAY;
        isOccupied = false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }
}
