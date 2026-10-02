package core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import tileengine.TETile;
import tileengine.Tileset;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class World {
    private final TETile[][] dirty;
    private final TETile[][] darty;
    private final int width;
    private final int height;
    private int xa;
    private int ya;
    private boolean light;

    private static final int A = 15;
    private static final int B = 23;
    private int origin;
    private WeightedQuickUnionUF worldUnion;

    private class Room {
        private int l;
        private int h;
        private int x;
        private int y;
        public Room(int l, int h, int x, int y) {
            this.l = l;
            this.h = h;
            this.x = x;
            this.y = y;
            for (int i = x; i < l + x; i++) {
                for (int j = y; j < y + h; j++) {
                    dirty[i][j] = Tileset.FLOOR;
                    worldUnion.union(toInt(x, y), toInt(i, j));
                }
            }
        }
    }

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.light = true;
        dirty = new TETile[width][height];
        darty = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dirty[i][j] = Tileset.NOTHING;
                darty[i][j] = Tileset.NOTHING;
            }
        }
        worldUnion = new WeightedQuickUnionUF(width * height);
        Random random = new Random(seed);
        int num = random.nextInt(A, B);
        ArrayList<Room> roomlist = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int l = random.nextInt(4, 7);
            int w = random.nextInt(4, 7);
            int x = random.nextInt(2, width - l - 2);
            int y = random.nextInt(2, height - w - 2);
            Room r = new Room(l, w, x, y);
            if (i == 0) {
                origin = toInt(x, y);
            }
            if (!roomlist.isEmpty()) {
                int x1 = random.nextInt(x, x + l);
                int y1 = random.nextInt(y, y + w);
                int index = random.nextInt(roomlist.size());
                Room a = roomlist.get(index);
                int x2 = random.nextInt(a.x, a.x + a.l);
                int y2 = random.nextInt(a.y, a.y + a.h);
                connectRoom(x2, y2, a, x1, y1, r);
            }
            roomlist.add(r);
        }
        wall();
        int index = random.nextInt(roomlist.size() - 1);
        Room a = roomlist.get(index);
        xa = random.nextInt(a.x, a.x + a.l);
        ya = random.nextInt(a.y, a.y + a.h);
        dirty[xa][ya] = Tileset.AVATAR;
        darty[xa][ya] = Tileset.AVATAR;
    }

    public void connectRoom(int x1, int y1, Room otherRoom, int x2, int y2, Room thisRoom) {
        outerLoop:
        while (true) {
            for (int j = 0; j <= abs(y1 - y2); j++) {
                if (y2 > y1) {
                    connectHelperVert(x2, y2, -j, thisRoom);
                    if (worldUnion.connected(origin, toInt(thisRoom.x, thisRoom.y))) {
                        break outerLoop;
                    }
                } else {
                    connectHelperVert(x2, y2, j, thisRoom);
                    if (worldUnion.connected(origin, toInt(thisRoom.x, thisRoom.y))) {
                        break outerLoop;
                    }
                }
            }
            for (int i = 0; i < abs(x1 - x2); i++) {
                if (x2 > x1) {
                    connectHelperHor(x2, y1, -i - 1, thisRoom);
                    if (worldUnion.connected(origin, toInt(thisRoom.x, thisRoom.y))) {
                        break outerLoop;
                    }
                } else {
                    connectHelperHor(x2, y1, i + 1, thisRoom);
                    if (worldUnion.connected(origin, toInt(thisRoom.x, thisRoom.y))) {
                        break outerLoop;
                    }
                }
            }
        }
    }

    private void connectHelperVert(int x2, int y2, int j, Room thisRoom) {
        dirty[x2][y2 + j] = Tileset.FLOOR;
        if (dirty[x2 - 1][y2 + j] == Tileset.FLOOR) {
            worldUnion.union(toInt(x2 - 1, y2 + j), toInt(thisRoom.x, thisRoom.y));
        }
        if (dirty[x2 + 1][y2 + j] == Tileset.FLOOR) {
            worldUnion.union(toInt(x2 + 1, y2 + j), toInt(thisRoom.x, thisRoom.y));
        }
        if (j > 0) {
            if (dirty[x2][y2 + j + 1] == Tileset.FLOOR) {
                worldUnion.union(toInt(x2, y2 + j + 1), toInt(thisRoom.x, thisRoom.y));
            }
        } else if (j < 0) {
            if (dirty[x2][y2 + j - 1] == Tileset.FLOOR) {
                worldUnion.union(toInt(x2, y2 + j - 1), toInt(thisRoom.x, thisRoom.y));
            }
        }
        worldUnion.union(toInt(x2, y2 + j), toInt(thisRoom.x, thisRoom.y));
    }

    private void connectHelperHor(int x2, int y1, int i, Room thisRoom) {
        dirty[x2 + i][y1] = Tileset.FLOOR;
        if (dirty[x2 + i][y1 - 1] == Tileset.FLOOR) {
            worldUnion.union(toInt(x2 + i, y1 - 1), toInt(thisRoom.x, thisRoom.y));
        }
        if (dirty[x2 + i][y1 + 1] == Tileset.FLOOR) {
            worldUnion.union(toInt(x2 + i, y1 + 1), toInt(thisRoom.x, thisRoom.y));
        }
        worldUnion.union(toInt(x2 + i, y1), toInt(thisRoom.x, thisRoom.y));
    }
    public void moveleft() {
        if (dirty[xa - 1][ya] == Tileset.FLOOR) {
            dirty[xa - 1][ya] = Tileset.AVATAR;
            dirty[xa][ya] = Tileset.FLOOR;

            xa = xa - 1;
        }
    }
    public void moveright() {
        if (dirty[xa + 1][ya] == Tileset.FLOOR) {
            dirty[xa + 1][ya] = Tileset.AVATAR;
            dirty[xa][ya] = Tileset.FLOOR;

            xa = xa + 1;
        }
    }
    public void moveup() {
        if (dirty[xa][ya + 1] == Tileset.FLOOR) {
            dirty[xa][ya + 1] = Tileset.AVATAR;
            dirty[xa][ya] = Tileset.FLOOR;

            ya = ya + 1;
        }
    }
    public void movedown() {
        if (dirty[xa][ya - 1] == Tileset.FLOOR) {
            dirty[xa][ya - 1] = Tileset.AVATAR;
            dirty[xa][ya] = Tileset.FLOOR;

            ya = ya - 1;
        }
    }
    public String where(boolean lightStatus, int x, int y) {
        if (lightStatus) {
            return dirty[x][y].description();
        } else {
            return darty[x][y].description();
        }
    }

    public void updateDarty() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                darty[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = xa - 2; i <= xa + 2; i++) {
            for (int j = ya - 2; j <= ya + 2; j++) {
                darty[i][j] = dirty[i][j];
            }
        }
    }
    public void wall() {
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if (!(dirty[i][j].equals(Tileset.FLOOR) || dirty[i][j].equals(Tileset.AVATAR))) {
                    if (dirty[i - 1][j - 1].equals(Tileset.FLOOR)
                            || dirty[i - 1][j].equals(Tileset.FLOOR)
                            || dirty[i - 1][j + 1].equals(Tileset.FLOOR)
                            || dirty[i][j + 1].equals(Tileset.FLOOR)
                            || dirty[i][j - 1].equals(Tileset.FLOOR)
                            || dirty[i + 1][j - 1].equals(Tileset.FLOOR)
                            || dirty[i + 1][j].equals(Tileset.FLOOR)
                            || dirty[i + 1][j + 1].equals(Tileset.FLOOR)) {
                        dirty[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public boolean light() {
        return light;
    }

    public void changeLight() {
        light = !light;
    }

    public TETile[][] getDirty() {
        return dirty;
    }
    public TETile[][] getDarty() {
        updateDarty();
        return darty;
    }

    private int toInt(int w, int h) {
        return h * width + w;
    }
}

