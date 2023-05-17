package byow.Core;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.*;
public class HUD {
    private int WIDTH;
    private int HEIGHT;
    private TETile[][] world;
    public int TEXT_SIZE = 30;
    public HUD (int width, int height, TETile[][] world) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.world = world;
    }
    public void hoveredTile() {
        StdDraw.setPenColor(Color.WHITE);
        Double mouseX = StdDraw.mouseX();
        Double mouseY = StdDraw.mouseY();
        int[] pos = findTile(mouseX, mouseY);
        if (pos[0] < 0 || pos[0] >= world.length || pos[1] < 0 || pos[1] >= world[0].length) {
            StdDraw.line(0, this.HEIGHT - 5, this.WIDTH, this.HEIGHT - 5);
            StdDraw.text(7, this.HEIGHT - 3, "out of bounds!");
        } else {
            TETile tile = world[pos[0]][pos[1]];
            StdDraw.line(0, this.HEIGHT - 5, this.WIDTH, this.HEIGHT - 5);
            StdDraw.text(5, this.HEIGHT - 3, tile.description());
        }
    }
    public void displayDateTime() {
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime rn = LocalDateTime.now();
        StdDraw.text(this.WIDTH - 10, this.HEIGHT - 3, dateTime.format(rn));
    }
    public void displayHoverTile() {
        StdDraw.setPenColor(Color.WHITE);
        Double mouseX = StdDraw.mouseX();
        Double mouseY = StdDraw.mouseY();
        int[] pos = findTile(mouseX, mouseY);
        StdDraw.filledSquare(pos[0] + 0.5, pos[1] + 0.5, 0.5);
    }
    public int[] findTile(Double mouseX, Double mouseY) {
        int[] ans = {(int) Math.floor(mouseX), (int) Math.floor(mouseY)};
        return ans;
    }
}
