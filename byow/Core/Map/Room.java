package byow.Core.Map;

public class Room {
    public enum Side {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
    int x, y, width, height;
    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }
}