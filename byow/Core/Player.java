package byow.Core;
import byow.TileEngine.Tileset;
import byow.TileEngine.TETile;
import byow.Core.Map.MapGenerator;
public class Player {
    private int x;
    private int y;
    protected TETile shape;
    public Player(TETile[][] world, int x, int y) {
        this.shape = Tileset.AVATAR;
        world[x][y] = this.shape;
        this.x = x;
        this.y = y;
    }
    public void move(MapGenerator world, String key) {
        switch (key) {
            case "W" -> {
                if(world.returnWorld()[x][y + 1] == Tileset.FLOOR) {
                    world.returnWorld()[x][y] = Tileset.FLOOR;
                    this.y = this.y + 1;
                    world.returnWorld()[x][y] = this.shape;
                }
            }
            case "A" -> {
                if(world.returnWorld()[x - 1][y] == Tileset.FLOOR) {
                    world.returnWorld()[x][y] = Tileset.FLOOR;
                    this.x = this.x - 1;
                    world.returnWorld()[x][y] = this.shape;
                }
            }
            case "S" -> {
                if(world.returnWorld()[x][y - 1] == Tileset.FLOOR) {
                    world.returnWorld()[x][y] = Tileset.FLOOR;
                    this.y = this.y - 1;
                    world.returnWorld()[x][y] = this.shape;
                }
            }
            case "D" -> {
                if(world.returnWorld()[x + 1][y] == Tileset.FLOOR) {
                    world.returnWorld()[x][y] = Tileset.FLOOR;
                    this.x = this.x + 1;
                    world.returnWorld()[x][y] = this.shape;
                }
            }
        }
    }
    public int[] position(){
        int[] ans = {this.x, this.y};
        return ans;
    }
}
