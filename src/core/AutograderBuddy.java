package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.io.File;
import java.io.IOException;
import utils.FileUtils;

public class AutograderBuddy {
    private static final int TILE_HEIGHT = 50;
    private static final int TILE_WIDTH = 80;

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public static TETile[][] getWorldFromInput(String input) {
        boolean loadd = false;
        String seedstring = "1";
        int start = 0;
        String action = "n";
        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            seedstring = "";
            for (int i = 1; i < input.length(); i++) {
                if ((input.charAt(i) == 'S') || (input.charAt(i) == 's')) {
                    start = i + 1;
                    break;
                }
                seedstring += input.charAt(i);
            }
            action += seedstring + "s";
        } else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            loadd = true;
            start = 1;
        }
        World w;
        long seed = Long.parseLong(seedstring);
        if (!loadd) {
            w = new World(TILE_WIDTH, TILE_HEIGHT, seed);
        } else {
            action = getAction();
            w = getFromInput(action);
        }
        boolean colonTyped = false;
        for (int i = start; i < input.length(); i++) {
            if (colonTyped) {
                if ('q' == input.charAt(i) || 'Q' == input.charAt(i)) {
                    save(action);
                } else {
                    colonTyped = false;
                }
            }
            if ('a' == input.charAt(i) || 'A' == input.charAt(i)) {
                w.moveleft();
                action += input.charAt(i);
            } else if ('s' == input.charAt(i) || 'S' == input.charAt(i)) {
                w.movedown();
                action += input.charAt(i);
            } else if ('d' == input.charAt(i) || 'D' == input.charAt(i)) {
                w.moveright();
                action += input.charAt(i);
            } else if ('w' == input.charAt(i) || 'W' == input.charAt(i)) {
                w.moveup();
                action += input.charAt(i);
            } else if ('t' == input.charAt(i) || 'T' == input.charAt(i)) {
                w.changeLight();
                action += input.charAt(i);
            } else if (':' == input.charAt(i)) {
                colonTyped = true;
            }
        }
        return w.getDirty();
    }

    private static void save(String action) {
        try {
            File savefile = new File("saveFilee.txt");
            savefile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileUtils.writeFile("saveFilee.txt", action);
    }
    private static String getAction() {
        String action = "";
        try {
            action = FileUtils.readFile("saveFilee.txt");
        } catch (IllegalArgumentException e) {
            System.out.println("no file");
            System.exit(0);
        }
        return action;
    }

    private static World getFromInput(String input) {
        String seedstring = "";
        int start = 0;
        for (int i = 1; i < input.length(); i++) {
            if ((input.charAt(i) == 'S') || (input.charAt(i) == 's')) {
                start = i + 1;
                break;
            }
            seedstring += input.charAt(i);
        }
        long seed = Long.parseLong(seedstring);
        World w = new World(TILE_WIDTH, TILE_HEIGHT, seed);
        for (int i = start; i < input.length(); i++) {
            if ('a' == input.charAt(i) || 'A' == input.charAt(i)) {
                w.moveleft();
            } else if ('s' == input.charAt(i) || 'S' == input.charAt(i)) {
                w.movedown();
            } else if ('d' == input.charAt(i) || 'D' == input.charAt(i)) {
                w.moveright();
            } else if ('w' == input.charAt(i) || 'W' == input.charAt(i)) {
                w.moveup();
            } else if ('t' == input.charAt(i) || 'T' == input.charAt(i)) {
                w.changeLight();
            }
        }
        return w;
    }
    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }

    public static void main(String[] args) {
        TETile[][] w = getWorldFromInput("n520swwsssssaadaad");
        TETile[][] w2 = getWorldFromInput("n520swwsss:q");
        TETile[][] w3 = getWorldFromInput("lssaadaad");
        TETile[][] w4 = getWorldFromInput("n520swwsss");
        TETile[][] w5 = getWorldFromInput("n520swwsss");
        TETile[][] w6 = getWorldFromInput("N999SDDDWWWDDD");
        getWorldFromInput("N999SDDD:Q");
        TETile[][] w7 = getWorldFromInput("LWWWDDD");
        getWorldFromInput("N999SDDD:Q");
        getWorldFromInput("LWWW:Q");
        TETile[][] w8 = getWorldFromInput("LDDD:Q");
        getWorldFromInput("N999SDDD:Q");
        getWorldFromInput("L:Q");
        getWorldFromInput("L:Q");
        TETile[][] w9 = getWorldFromInput("LWWWDDD");
        if (tiles2DArrayEquals(w6, w7)) {
            System.out.println(1);
        }
        if (tiles2DArrayEquals(w7, w8)) {
            System.out.println(2);
        }
        if (tiles2DArrayEquals(w8, w9)) {
            System.out.println(3);
        }
        if (tiles2DArrayEquals(w, w3)) {
            System.out.println(4);
        }
        if (tiles2DArrayEquals(w2, w4)) {
            System.out.println(5);
        }
    }

    public static boolean tiles2DArrayEquals(TETile[][] tiles1, TETile[][] tiles2) {
        for (int i = 0; i < tiles1.length; i++) {
            for (int j = 0; j < tiles1[0].length; j++) {
                if (!tiles1[i][j].equals(tiles2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
