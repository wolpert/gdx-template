package com.codeheadsystems.engine;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.codeheadsystems.engine.module.GameComponent;
import com.codeheadsystems.engine.scene.Initializer;

/**
 * This class is the head-waters for the game. It basically has the following responsibilities:
 * <ul>
 *     <li>Initialize the game (dagger)</li>
 *     <li> Gives the game screen management</li>
 *     <li> passes the game loop events and sends to the active screen</li>
 *     <li> Handles the exit of the game</li>
 * </ul>
 * We have to use this class to manage the screens which are built by the dagger work in the
 * initializer. If the screen manager is in the dagger injection library, then we have circular
 * dependencies. That can be solved by using 'lazy' but this removes that issue.
 * <p>
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class TopLevelApplication implements ApplicationListener {

    private final String TAG = TopLevelApplication.class.getSimpleName();
    protected Screen screen;
    private GameComponent gameComponent;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG); // TODO do this on configuration
        log.info(TAG, "Initiating Application");
        setScreen(new Initializer(this));
    }

    @Override
    public void dispose() {
        if (screen != null) screen.hide();
        log.info(TAG, "Wait.... what?");
        gameComponent.disposables().forEach(disposable -> {
            log.info(TAG, "Disposing: " + disposable.getClass().getSimpleName());
            disposable.dispose();
        });
    }

    @Override
    public void pause() {
        log.info(TAG, "Pausing: " + screen);
        if (screen != null) screen.pause();
    }

    @Override
    public void resume() {
        log.info(TAG, "Resuming: " + screen);
        if (screen != null) screen.resume();
    }

    @Override
    public void render() {
        if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        if (screen != null) screen.resize(width, height);
    }

    public void setGameComponent(GameComponent gameComponent) {
        this.gameComponent = gameComponent;
    }

    /**
     * @return the currently active {@link Screen}.
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
     * screen, if any.
     *
     * @param screen may be {@code null}
     */
    public void setScreen(Screen screen) {
        log.info(TAG, "Enabling: " + screen);
        if (this.screen != null) {
            this.screen.hide();
        }
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public void showTitle() {
        gameComponent.levelManager().cleanup();
        setScreen(gameComponent.titleScene());
    }

    public void showNextLevel() {
        gameComponent.levelManager().cleanup();
        setScreen(gameComponent.launcher());
    }

    public void exitGame() {
        log.debug(TAG, "exitGame()");
        Gdx.app.exit();
    }
}
