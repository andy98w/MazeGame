package byow.Core.Map;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.HUD;

public class MapVisualTest {
    private static final int STARTX = 30;
    private static final int STARTY = 6;
    private static final int MAXW = 8;
    private static final int MAXH = 8;
    private static final int MAX_OFFSET = 3;
    private static final Room.Side ENTRY_SIDE = Room.Side.BOTTOM;
    private static final TETile ENTRY_TILE = Tileset.LOCKED_DOOR;
    private static final TETile EXIT_TILE = Tileset.FLOOR;
    public static final TERenderer ter = new TERenderer();
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        MapGenerationParameters mgp = MapGenerationParameters.defaultParameters();
        ter.initialize(mgp.width, mgp.height);
        MapGenerator wGen = new MapGenerator(98, mgp);
        TETile[][] ws = wGen.returnWorld();
        HUD hud = new HUD(mgp.width, mgp.height, wGen.returnWorld());
        ter.renderFrame(wGen.returnWorld(), hud);
    }
}
