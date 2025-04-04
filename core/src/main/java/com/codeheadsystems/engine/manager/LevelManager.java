package com.codeheadsystems.engine.manager;

import static com.codeheadsystems.engine.utility.Log.log;

import com.codeheadsystems.engine.model.ActiveLevelDetails;
import com.codeheadsystems.engine.model.ActiveLevelDetailsHolder;
import com.codeheadsystems.engine.model.ActiveLevelDetailsRecordBuilder;
import com.codeheadsystems.engine.model.GameConfig;
import com.codeheadsystems.engine.model.LevelConfig;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The level manager is responsible for getting level details on the game. It manages the available levels as
 * the game progresses, like opening up new levels, showing what's available be deselected, etc. Think of him
 * as an orchestrator of the levels creation/cleanup process.
 */
@Singleton
public class LevelManager {
    private static final String TAG = LevelManager.class.getSimpleName();

    private final GameConfig gameConfig;
    private final ActiveLevelDetailsHolder activeLevelDetailsHolder;

    private int levelNumber;

    @Inject
    public LevelManager(final GameConfig gameConfig,
                        final ActiveLevelDetailsHolder activeLevelDetailsHolder) {
        this.gameConfig = gameConfig;
        this.activeLevelDetailsHolder = activeLevelDetailsHolder;
        levelNumber = 0;
        log.debug(TAG, "LevelManager()");
    }

    public void createNextLevel() {
        final LevelConfig levelConfig = getLevelConfig();
        final ActiveLevelDetails activeLevelDetails = ActiveLevelDetailsRecordBuilder.builder()
            .levelConfig(levelConfig)
            .build();
        activeLevelDetailsHolder.set(activeLevelDetails);
        log.debug(TAG, "Created level " + levelConfig.level);
    }

    public void cleanup() {
        activeLevelDetailsHolder.ifSet(activeLevelDetails -> {
            activeLevelDetailsHolder.set(null);
        });
    }

    private LevelConfig getLevelConfig() {
        final LevelConfig levelConfig = gameConfig.levels.get(levelNumber);
        levelConfig.level = levelNumber; // set it based on reality.
        levelNumber++;
        if (levelNumber >= gameConfig.levels.size()) {
            levelNumber = 0;
        }
        return levelConfig;
    }

}
