package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import utils.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class Main {
    private static final int C = 496;
    private static final int D = 35;
    private static final int E = 31;
    private static final int F = 15;
    private static final int J = 11;
    private static final int I = 25;
    private static final int L = 7;
    private static final int M = 20;
    private static final int N = 80;
    private static final int O = 50;
    private static final int P = 49;
    private static final int Q = 40;
    private static String filetext = "N";



    public Main() {
    }
    private static void save() {
        try {
            File savefile = new File("src/core/saveFile.txt");
            savefile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileUtils.writeFile("src/core/saveFile.txt", filetext);
        System.exit(0);
    }

    private static World load() {
        World w = null;
        try {
            String input = FileUtils.readFile("src/core/saveFile.txt");
            filetext = input;
            w = getWorldFromInput(input);
        } catch (IllegalArgumentException e) {
            System.out.println("no file");
            System.exit(0);
        }
        return w;
    }

    private static World getWorldFromInput(String input) {
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
        World w = new World(N, O, seed);
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

    private static void userInterface() {
        StdDraw.setCanvasSize(C, C);
        Font font = new Font("Time New Roman", Font.BOLD, D);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, E);
        StdDraw.setYscale(0, E);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(F, I, "WORLD EXPLORER");
        Font font2 = new Font("Time New Roman", Font.BOLD, M);
        StdDraw.setFont(font2);
        StdDraw.text(F, F, "NEW GAME (N)");
        StdDraw.text(F, J, "LOAD GAME (L)");
        StdDraw.text(F, L, "QUIT (Q)");
        StdDraw.show();
    }

    private static long seedTyping() {
        StdDraw.setCanvasSize(C, C);
        Font fonttt = new Font("Time New Roman", Font.BOLD, D);
        StdDraw.setFont(fonttt);
        StdDraw.setXscale(0, E);
        StdDraw.setYscale(0, E);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(F, M, "PLEASE INPUT SEED:");
        StdDraw.show();
        java.util.List<String> num = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        boolean over = false;
        String seed = "";
        while (!over) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 's' || key == 'S') {
                    over = true;

                } else if (num.contains(Character.toString(key))) {
                    seed += Character.toString(key);
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(F, M, "PLEASE INPUT SEED:");
                    StdDraw.text(F, J, seed);
                    StdDraw.show();
                }
            }
        }
        filetext += seed + "s";
        return Long.parseLong(seed);
    }
    public static void interactwinput() {
        userInterface();
        boolean typed = false;
        boolean seedType = false;
        while (!typed) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'N' || key == 'n') {
                    typed = true;
                    seedType = true;
                } else if (key == 'L' || key == 'l') {
                    typed = true;
                }
            }
        }
        World w;
        if (seedType) {
            long s = seedTyping();
            w = new World(N, O, s);
        } else {
            w = load();
        }
        TERenderer render = new TERenderer();
        render.initialize(N, O);
        boolean colonTyped = false;
        while (true) {
            StdDraw.clear(new Color(0, 0, 0));
            if (w.light()) {
                render.render(w.getDirty());
            } else {
                render.render(w.getDarty());
            }
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (colonTyped) {
                    if ('q' == key || 'Q' == key) {
                        save();
                    } else {
                        colonTyped = false;
                    }
                } else if ('a' == key || 'A' == key) {
                    w.moveleft();
                    filetext += key;
                } else if ('s' == key || 'S' == key) {
                    w.movedown();
                    filetext += key;
                } else if ('d' == key || 'D' == key) {
                    w.moveright();
                    filetext += key;
                } else if ('w' == key || 'W' == key) {
                    w.moveup();
                    filetext += key;
                } else if ('t' == key || 'T' == key) {
                    filetext += key;
                    w.changeLight();
                } else if (':' == key) {
                    colonTyped = true;
                }
            }
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if (x >= 0 && x < w.getDirty().length) {
                if (y >= 0 && y < w.getDirty()[0].length) {
                    String where = w.where(w.light(), x, y);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(4, P, "Tile: " + where);
                }
            }
            displaytime();
            StdDraw.show();
        }

    }
    private static void displaytime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // month is zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        StdDraw.text(Q, P, String.format("%04d-%02d-%02d", year, month, day)
                + " "
                + String.format("%02d:%02d:%02d", hours, minutes, seconds));

    }
    public static void main(String[] args) {
        Main.interactwinput();
    }
}
