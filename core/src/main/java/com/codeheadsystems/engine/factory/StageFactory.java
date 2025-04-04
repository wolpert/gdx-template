package com.codeheadsystems.engine.factory;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;
import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.codeheadsystems.engine.TopLevelApplication;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides the stages or the controls of the various screens.
 */
@Singleton
public class StageFactory {
    private final String TAG = getClass().getSimpleName();

    private final TopLevelApplication topLevelApplication;
    private final Skin skin;

    @Inject
    public StageFactory(final TopLevelApplication topLevelApplication,
                        final Skin skin) {
        log.debug(TAG, "constructor()");
        this.topLevelApplication = topLevelApplication;
        this.skin = skin;
    }

    public Stage buildLevelStage() {
        log.debug(TAG, "stage.level()");
        final Stage stage = new Stage(new ScreenViewport());
        final Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(center | top);
        table.setPosition(0, Gdx.graphics.getHeight());
        final TextButton titleButton = new TextButton("Title", skin);
        titleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                topLevelApplication.showTitle();
            }
        });
        final TextButton launcherButton = new TextButton("Launcher", skin);
        launcherButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                topLevelApplication.showNextLevel();
            }
        });
        table.padTop(30);
        table.add(titleButton).padBottom(30);
        table.row();
        table.add(launcherButton).padBottom(30);
        stage.addActor(table);
        return stage;
    }

    public Stage buildTitleStage() {
        final Stage stage = new Stage(new ScreenViewport());
        final Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(center | top);
        table.setPosition(0, Gdx.graphics.getHeight());
        final TextButton startButton = new TextButton("New Game", skin);
        final TextButton quitButton = new TextButton("Quit Game", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                topLevelApplication.exitGame();
            }
        });
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                topLevelApplication.showNextLevel();
            }
        });
        table.padTop(30);
        table.add(startButton).padBottom(30);
        table.row();
        table.add(quitButton);
        stage.addActor(table);
        return stage;
    }

}
