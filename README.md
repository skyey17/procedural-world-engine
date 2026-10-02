# Java World Generator

This is my original Java world exploration game. Enter a seed to generate a tile world, move through its rooms and hallways, switch the visibility mode, and save or load a session.

The gameplay code in `src/core` is my original, human-written Java implementation; no AI-generated gameplay logic was added. `World.java` generates the map and handles movement; `Main.java` handles the keyboard interface and saves. `AutograderBuddy.java` is the original input-string interface. The `tileengine` and `utils` packages contain supporting components included to keep the project structure intact.

## Run

Use Java 17 or later. Add Princeton's [algs4.jar](https://algs4.cs.princeton.edu/code/) to the classpath. Open this directory as a Java project, mark `src` as the source root, and run `core.Main` with this directory as the working directory. The repository does not include the external JAR.

- `N`: new world. Type a numeric seed and press `S`.
- `L`: load a saved world.
- `W A S D`: move.
- `T`: toggle visibility.
- `:Q`: save and quit.

Save files are local runtime data and are excluded from the repository. The original Java implementation has been preserved, apart from the title shown on the start screen. I could not run it in the current workspace because no Java runtime is installed.
