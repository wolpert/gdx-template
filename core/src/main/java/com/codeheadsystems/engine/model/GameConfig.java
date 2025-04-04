package com.codeheadsystems.engine.model;

import java.util.ArrayList;

/**
 * Provides for the game configuration and all the levels in a format t hat is compatible with the JSON loader.
 */
public class GameConfig {

    public Globals globals;
    public ArrayList<LevelConfig> levels;

    public static class Globals {
        public boolean debug;
        public boolean debugStateMachine;
    }

}
