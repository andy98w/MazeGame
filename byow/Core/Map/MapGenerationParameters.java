package byow.Core.Map;

import java.util.Random;

public class MapGenerationParameters {
    MapGenerationParameters mgp;
    public int width;
    public int height;
    public static final int EIGHTY = 80;
    public static final int FIFTY = 50;
    Random rand;
    public MapGenerationParameters(int width, int height) {
        this.width = width;
        this.height = height;
        this.rand = new Random();
    }
    public static MapGenerationParameters defaultParameters() {
        MapGenerationParameters mgp = new MapGenerationParameters(80, 50);
        mgp.width = EIGHTY;
        mgp.height = FIFTY;
        mgp.rand = new Random();
        return mgp;
    }
}