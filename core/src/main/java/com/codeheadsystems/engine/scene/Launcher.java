package com.codeheadsystems.engine.scene;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.ScreenAdapter;
import com.codeheadsystems.engine.TopLevelApplication;
import com.codeheadsystems.engine.manager.LevelManager;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class acts as a buffer between the title and the first level, and between levels. If we need to do fadeouts
 * we can use here.
 */
@Singleton
public class Launcher extends ScreenAdapter {

    private final String TAG = getClass().getSimpleName();
    private final TopLevelApplication topLevelApplication;
    private final LevelManager levelManager;
    private final Level level;

    @Inject
    public Launcher(final TopLevelApplication topLevelApplication,
                    final LevelManager levelManager,
                    final Level level) {
        this.levelManager = levelManager;
        this.level = level;
        log.debug(TAG, "constructor()");
        this.topLevelApplication = topLevelApplication;
    }

    @Override
    public void show() {
        log.debug(TAG, "show()");
        levelManager.createNextLevel();
        topLevelApplication.setScreen(level);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
