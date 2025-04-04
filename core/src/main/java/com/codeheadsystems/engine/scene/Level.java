package com.codeheadsystems.engine.scene;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.codeheadsystems.engine.factory.StageFactory;
import com.codeheadsystems.engine.inputadapter.HexInputProcessor;
import com.codeheadsystems.engine.model.ActiveLevelDetailsHolder;
import com.codeheadsystems.engine.model.LevelConfig;
import javax.inject.Inject;
import javax.inject.Singleton;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * This class sets up the game board and executes all the actions in order during the game.
 */
@Singleton
public class Level extends ScreenAdapter {

    private final String TAG = getClass().getSimpleName();
    private final OrthographicCamera orthographicCamera;
    private final ShapeDrawer shapeDrawer;
    private final SpriteBatch spriteBatch;
    private final InputProcessor inputProcessor;
    private final PooledEngine pooledEngine;
    private final ActiveLevelDetailsHolder holder;
    private final Texture backgroundTexture;

    private final Stage stage;
    private LevelConfig levelConfig;
    private Sprite backgroundSprite;

    @Inject
    public Level(final HexInputProcessor hexInputProcessor,
                 final OrthographicCamera orthographicCamera,
                 final AssetManager assetManager,
                 final ShapeDrawer shapeDrawer,
                 final SpriteBatch spriteBatch,
                 final PooledEngine pooledEngine,
                 final StageFactory stageFactory,
                 final ActiveLevelDetailsHolder holder) {
        log.debug(TAG, "constructor()");
        this.stage = stageFactory.buildLevelStage();
        this.holder = holder;
        this.orthographicCamera = orthographicCamera;
        this.spriteBatch = spriteBatch;
        this.shapeDrawer = shapeDrawer;
        this.inputProcessor = hexInputProcessor.inputProcessor();
        this.pooledEngine = pooledEngine;
        this.backgroundTexture = assetManager.get("level.png", Texture.class);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public void show() {
        log.debug(TAG, "show() " + this);
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setCenter((float) Gdx.graphics.getWidth() / 2f, (float) Gdx.graphics.getHeight() / 2f);
        levelConfig = holder.get().levelConfig();
        final LevelConfig.Location startView = levelConfig.startView;
        orthographicCamera.setToOrtho(false, (float) Gdx.graphics.getWidth() / 2f, (float) Gdx.graphics.getHeight() / 2f);
        orthographicCamera.position.set(startView.x, startView.y, startView.z);
        final InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, inputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
        log.debug(TAG, "show() level " + levelConfig.level);
    }

    @Override
    public void hide() {
        log.debug(TAG, "hide() " + this);
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(final float deltaTime) {
        orthographicCamera.update();
        shapeDrawer.getBatch().setProjectionMatrix(orthographicCamera.combined);
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        shapeDrawer.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        backgroundSprite.draw(spriteBatch);
        spriteBatch.end();
        pooledEngine.update(deltaTime);
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void dispose() {
        log.debug(TAG, "dispose()");
        stage.dispose();
    }

}
