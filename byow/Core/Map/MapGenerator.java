package byow.Core.Map;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class MapGenerator {
    private final int theHeight;
    private final int theWidth;
    private final Random rand;
    private final int numOfRooms;
    private final HashMap<ArrayList<Integer>, Room> hashRooms;
    TETile[][] world;
    ArrayList<Room> myRooms;
    public static final int MINROOMS = 100;
    public static final int MAXROOMS = 200;
    public static final int MINMEASURES = 4;
    public static final int MAXMEASURES = 15;
    public MapGenerator(long seed, MapGenerationParameters mgp) {
        this.rand = new Random(seed);
        theHeight = mgp.height;
        theWidth = mgp.width;
        myRooms = new ArrayList<>();
        hashRooms = new HashMap<>();
        world = new TETile[theWidth][theHeight];
        worldInitializer();
        numOfRooms = this.rand.nextInt(MINROOMS, MAXROOMS);
        roomGenerator();
        roomConnector();
    }
    private void worldInitializer() {
        for (int y = 0; y < theHeight; y++) {
            for (int x = 0; x < theWidth; x++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    private void roomGenerator() {
        for (int i = 0; i < numOfRooms; i++) {
            int xPos = this.rand.nextInt(theWidth);
            int yPos = this.rand.nextInt(theHeight);
            int rHeight = this.rand.nextInt(MINMEASURES, MAXMEASURES);
            int rWidth = this.rand.nextInt(MINMEASURES, MAXMEASURES);
            Room room = new Room(xPos, yPos, rWidth, rHeight);
            if (validate(room)) {
                roomPlacer(xPos, yPos, rWidth, rHeight);
                myRooms.add(room);
                for (int y = yPos; y <= yPos + rHeight; y++) {
                    for (int x = xPos; x <= xPos + rWidth; x++) {
                        ArrayList<Integer> cPos = new ArrayList<>(Arrays.asList(x, y));
                        hashRooms.put(cPos, room);
                    }
                }
            }
        }
    }
    private void roomConnector() {
        int initNum = this.rand.nextInt(myRooms.size());
        ArrayList<Room> queue = new ArrayList<>();
        HashSet<Room> checked = new HashSet<>();
        Room initRoom = myRooms.get(initNum);
        queue.add(initRoom);
        ArrayList<Room> under;
        checked.add(initRoom);
        while (!queue.isEmpty()) {
            Room cRoom = queue.remove(0);
            under = retUnder(cRoom, checked);
            for (Room nRoom : under) {
                hallwayCreator(cRoom, nRoom);
            }
            checked.add(cRoom);
            if (checked.size() == myRooms.size() - 1) {
                break;
            }
            queue = under;
        }
    }
    private void roomPlacer(int xPos, int yPos, int width, int height) {
        for (int y = yPos; y <= yPos + height; y++) {
            for (int x = xPos; x <= xPos + width; x++) {
                if (edge(x, y, xPos, yPos, width, height)) {
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }
    private void hallwayCreator(Room cRoom, Room nRoom) {
        int[] cRoomPos = inBetween(cRoom);
        int[] nRoomPos = inBetween(nRoom);
        int xCurr = cRoomPos[0];
        int yCurr = cRoomPos[1];
        int xLeng = nRoomPos[0] - xCurr;
        int yLeng = nRoomPos[1] - yCurr;
        int xDir = 1;
        int yDir = 1;
        if (xLeng < 0) {
            xDir *= -1;
        }
        if (yLeng < 0) {
            yDir *= -1;
        }
        int rando = this.rand.nextInt(2);
        if (rando == 0) {
            h(xCurr, yCurr, xLeng, xDir);
            c(nRoomPos[0], yCurr);
            v(xCurr + xLeng, yCurr + yDir, yLeng, yDir);

        }
        v(xCurr, yCurr, yLeng, yDir);
        c(xCurr, nRoomPos[1]);
        h(xCurr + xDir, yCurr + yLeng, xLeng, xDir);
    }
    private boolean edge(int x, int y, int xPos, int yPos, int width, int height) {
        if (y >= yPos && y <= yPos + height && x == xPos) {
            return true;
        }
        if (y >= yPos && y <= yPos + height && x == xPos + width) {
            return true;
        }
        if (y == yPos && x >= xPos && x <= xPos + width) {
            return true;
        }
        return y == yPos + height && x >= xPos && x <= xPos + width;
    }
    private void v(int x, int y, int length, int dir) {
        length = Math.abs(length);
        for (int i = 0; i < length; i++) {
            int i1 = i * dir;
            if (world[x][y + i1] == Tileset.FLOOR) {
                continue;
            }
            world[x][y + i1] = Tileset.FLOOR;
            world[x + 1][y + i1] = Tileset.WALL;
            world[x - 1][y + i1] = Tileset.WALL;
        }
    }
    private void h(int x, int y, int length, int dir) {
        length = Math.abs(length);
        for (int i = 0; i < length; i++) {
            int i1 = i * dir;
            if (world[x + i1][y] == Tileset.FLOOR) {
                continue;
            }
            world[x + i1][y] = Tileset.FLOOR;
            world[x + i1][y + 1] = Tileset.WALL;
            world[x + i1][y - 1] = Tileset.WALL;
        }
    }
    private void c(int x, int y) {
        world[x][y] = Tileset.FLOOR;
        int[][] dir = new int[][]{
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}
        };
        for (int[] i : dir) {
            int xPos = x + i[0];
            int yPos = y + i[1];
            if (xPos < theWidth && xPos >= 0 && yPos < theHeight && yPos >= 0 && world[xPos][yPos] == Tileset.NOTHING) {
                world[xPos][yPos] = Tileset.WALL;
            }
        }
    }
    private ArrayList<Room> retUnder(Room cRoom, HashSet<Room> checkedRooms) {
        ArrayList<Room> result = null;
        int temp = 1;
        ArrayList<Integer> initPos = new ArrayList<>(Arrays.asList(cRoom.x, cRoom.y));
        ArrayList<ArrayList<Integer>> myQ = new ArrayList<>(List.of(initPos));
        HashSet<ArrayList<Integer>> checked = new HashSet<>();
        ArrayList<Room> under = new ArrayList<>();
        HashSet<Room> currChecked = new HashSet<>();
        checked.add(initPos);
        ArrayList<ArrayList<Integer>> curr;
        int[][] dir = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        currChecked.add(cRoom);
        while (!myQ.isEmpty()) {
            curr = new ArrayList<>();
            for (ArrayList<Integer> single : myQ) {
                for (int[] i : dir) {
                    int xNew = single.get(0) + i[0];
                    int yNew = single.get(1) + i[1];
                    if (xNew >= 0 && xNew < theWidth && yNew >= 0 && yNew < theHeight) {
                        ArrayList<Integer> cPos = new ArrayList<>(Arrays.asList(xNew, yNew));
                        if (!checked.contains(cPos)) {
                            if (hashRooms.containsKey(cPos)) {
                                Room thisRoom = hashRooms.get(cPos);
                                if (!thisRoom.equals(cRoom)) {
                                    if (!currChecked.contains(thisRoom) && !checkedRooms.contains(thisRoom)) {
                                        temp -= 1;
                                        under.add(thisRoom);
                                        currChecked.add(thisRoom);
                                    }
                                }
                            }
                            checked.add(cPos);
                            curr.add(cPos);
                        }
                        if (temp == 0) {
                            result = under;
                            break;
                        }
                    }
                }
                if (result != null) {
                    break;
                }
            }
            if (result != null) {
                break;
            }
            myQ = curr;
        }
        if (result == null) {
            result = under;
        }
        return result;
    }
    private int[] inBetween(Room room) {
        int[] ans = {room.x + (room.width / 2), room.y + (room.height / 3)};
        return ans;
    }
    private boolean validate(Room myRoom) {
        int upperX = myRoom.x + myRoom.width;
        int upperY = myRoom.y + myRoom.height;
        if (upperX >= theWidth || myRoom.x < 0 || upperY >= theHeight || myRoom.y < 0) {
            return false;
        }
        for (int x = myRoom.x; x < upperX; x++) {
            for (int y = myRoom.y; y < upperY; y++) {
                if (world[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }
    public TETile[][] returnWorld() {
        return world;
    }
    public TETile getCell(int x, int y) {
        return world[x][y];
    }
}