package byow.Core;

import byow.Core.Map.MapGenerationParameters;
import byow.Core.Map.MapGenerator;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import static byow.Core.Gamestate.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Engine {
    TERenderer ter = new TERenderer();
    private static final int MENUHEIGHT = 55;
    private static final int MENUWIDTH = 80;
    private boolean isPlaying = true;
    private Gamestate gameState = Menu;
    private MapGenerationParameters mgp = MapGenerationParameters.defaultParameters();
    public static final int MENUSCREENHELPER = 13;
    public static final double MENUSCREENHELPER2 = 0.66;
    public static final double MENUSCREENHELPER3 = 0.40;
    public static final double MENUSCREENHELPER4 = 0.35;
    public static final double MENUSCREENHELPER5 = 0.30;
    public static final double TWO = 2.0;
    public static final int CELLSIZE = 16;
    private Player player;
    private String seed = "";
    Random rand;
    private MapGenerator world;
    private boolean lights = false;
    private String moveTracker;
    private HUD hud;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        displayMenu();
        KeyboardInputSource inputSource = new KeyboardInputSource();
        while (isPlaying) {
            if (inputSource.possibleNextInput()) {
                char key = inputSource.getNextKey();
                key(String.valueOf(key));
            }

        }
    }
    private void key(String key) {
        if (gameState == Menu) {
            if (key.equals("N")) {
                gameState = Seed;
                StdDraw.clear();
                displaySeedMenu();
            } else if (key.equals("L")) {
                String[] load = loadGame();
                assert load != null;
                seed = load[0];
                String moves = load[1];
                ter.initialize(MENUWIDTH, MENUHEIGHT);
                world = new MapGenerator(Long.parseLong(seed), mgp);
                String[] moveList = moves.split("");
                ArrayList<String> playerMoves = new ArrayList<String>(Arrays.asList(moveList));
                rand = new Random(Long.parseLong(seed));
                int x = RandomUtils.uniform(rand, 0, mgp.width);
                int y = RandomUtils.uniform(rand, 0, mgp.height);
                while (world.returnWorld()[x][y] != Tileset.FLOOR) {
                    x = RandomUtils.uniform(rand, 0, mgp.width);
                    y = RandomUtils.uniform(rand, 0, mgp.height);
                }
                player = new Player(world.returnWorld(), x, y);
                hud = new HUD(MENUWIDTH, MENUHEIGHT, world.returnWorld());
                for (String s : playerMoves) {
                    player.move(world, s);
                }
                int[] playerPos = player.position();
                int xStart = playerPos[0] - 5;
                int yStart = playerPos[1] - 5;
                int xEnd = playerPos[0] + 5;
                int yEnd = playerPos[1] + 5;
                ter.renderFrame(world.returnWorld(), hud, xStart, yStart, xEnd, yEnd);
                gameState = Play;
            } else if (key.equals("Q")) {
                isPlaying = false;
                gameState = Quit;
            }
        } else if (gameState == Seed) {
            if (key.equals("S")) {
                gameState = Play;
                initializeGame();
            } else {
                StdDraw.clear();
                seed = seed + key;
                displaySeedMenu();
            }
        } else if (gameState == Play) {
            if (key.equals(":")) {
                gameState = almostQuit;
            }
            if (key.equals("L")) {
                lights = !lights;
            }
            player.move(world, key);
            moveTracker += key;
            if (!lights) {
                int[] playerPos = player.position();
                int xStart = playerPos[0] - 5;
                int yStart = playerPos[1] - 5;
                int xEnd = playerPos[0] + 5;
                int yEnd = playerPos[1] + 5;
                ter.renderFrame(world.returnWorld(), hud, xStart, yStart, xEnd, yEnd);
            } else {
                ter.renderFrame(world.returnWorld(), hud);
            }
        } else if (gameState == almostQuit) {
            if (key.equals("Q")) {
                saveGame(seed, moveTracker);
                isPlaying = false;
                System.exit(0);
            } else {
                gameState = Play;
            }
        }
    }
    private void initializeGame() {
        rand = new Random(Long.parseLong(seed));
        world = new MapGenerator(Long.parseLong(seed), mgp);
        ter.initialize(MENUWIDTH, MENUHEIGHT);
        int x = RandomUtils.uniform(rand, 0, mgp.width);
        int y = RandomUtils.uniform(rand, 0, mgp.height);
        while (world.returnWorld()[x][y] != Tileset.FLOOR) {
            x = RandomUtils.uniform(rand, 0, mgp.width);
            y = RandomUtils.uniform(rand, 0, mgp.height);
        }
        hud = new HUD(MENUWIDTH, MENUHEIGHT, world.returnWorld());
        player = new Player(world.returnWorld(), x, y);
        int[] playerPos = player.position();
        int xStart = playerPos[0] - 5;
        int yStart = playerPos[1] - 5;
        int xEnd = playerPos[0] + 5;
        int yEnd = playerPos[1] + 5;
        ter.renderFrame(world.returnWorld(), hud, xStart, yStart, xEnd, yEnd);
    }
    private String[] loadGame() {
        In in = new In("Save.txt");
        String line = null;
        while (in.hasNextLine()) {
            if (in.isEmpty()) {
                break;
            }
            line = in.readLine();
        }
        if (line == null) {
            return null;
        }
        return line.split("[+]+");
    }
    private void saveGame(String seedString, String moves) {
        Out out = new Out("Save.txt");
        In in = new In("Save.txt");
        while (in.hasNextLine()) {
            if (in.isEmpty()) {
                break;
            }
            in.readLine();
        }
        out.println(seedString + "+" + moves);
    }
    private void displaySeedMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(mgp.width / TWO, mgp.height - MENUSCREENHELPER + 3, "Enter a number: ");
        StdDraw.text(mgp.width / TWO, mgp.height - MENUSCREENHELPER, seed);
        StdDraw.text(mgp.width / TWO, mgp.height - MENUSCREENHELPER - 3, "Then press 'S' to start");
        StdDraw.show();
    }
    private void displayMenu() {
        StdDraw.setCanvasSize(mgp.width * CELLSIZE, mgp.height * CELLSIZE);
        StdDraw.setXscale(0, mgp.width);
        StdDraw.setYscale(0, mgp.height);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.clear(Color.BLACK);
        StdDraw.text(mgp.width / TWO, mgp.height * MENUSCREENHELPER2, "MAZE GAME");
        StdDraw.text(mgp.width / TWO, mgp.height * MENUSCREENHELPER3, "New Game (N)");
        StdDraw.text(mgp.width / TWO, mgp.height * MENUSCREENHELPER4, "Load Game (L)");
        StdDraw.text(mgp.width / TWO, mgp.height * MENUSCREENHELPER5, "Quit (Q)");
        StdDraw.show();
    }
    public TETile[][] interactWithInputString(String input) {
        StringBuilder seedString = new StringBuilder();
        String temp = Character.toString(input.charAt(0)).toLowerCase();
        if (temp.equals("n")) {
            for (int i = 1; i < input.length(); i++) {
                String lol = Character.toString(input.charAt(i)).toLowerCase();
                if (lol.equals("s")) {
                    break;
                }
                seedString.append(input.charAt(i));
            }
        }
        StringBuilder strMoves = new StringBuilder();
        String seed1 = seedString.toString();
        if (temp.equals("l")) {
            String[] info = loadGame();
            if (info == null) {
                System.exit(0);
            }
            seed1 = info[0];
            String moves = info[1];
            for (int idx = 0; idx < moves.length(); idx++) {
                strMoves.append(moves.charAt(idx));
            }
        }
        for (int j = seedString.length(); j < input.length(); j++) {
            String move = Character.toString(input.charAt(j)).toLowerCase();
            if (move.equals(":") && j <= input.length() - 2) {
                String next = Character.toString(input.charAt(j + 1));
                if (next.equals("q") || next.equals("Q")) {
                    String moves = strMoves.toString();
                    saveGame(seed1, moves);
                    break;
                }
            } else if (move.equals("w") || move.equals("a") || move.equals("s") || move.equals("d")) {
                strMoves.append(move);
            }
        }
        long seed2 = Long.parseLong(seed1);
        ter.initialize(MENUWIDTH, MENUHEIGHT);
        mgp = MapGenerationParameters.defaultParameters();
        world = new MapGenerator(seed2, mgp);
        ArrayList<String> array = new ArrayList<String>(List.of(strMoves.toString()));
        rand = new Random(seed2);
        int x = RandomUtils.uniform(rand, 0, mgp.width);
        int y = RandomUtils.uniform(rand, 0, mgp.height);
        while (world.returnWorld()[x][y] != Tileset.FLOOR) {
            x = RandomUtils.uniform(rand, 0, mgp.width);
            y = RandomUtils.uniform(rand, 0, mgp.height);
        }
        player = new Player(world.returnWorld(), x, y);
        for (String s : array) {
            player.move(world, s);
        }
        hud = new HUD(MENUWIDTH, MENUHEIGHT, world.returnWorld());
        ter.renderFrame(world.returnWorld(), hud);
        return world.returnWorld();
    }
}
