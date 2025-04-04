package com.codeheadsystems.engine.scene;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.codeheadsystems.engine.factory.StageFactory;
import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Purpose of this screen is to show the title, maybe some animation.
 * <p>
 * From here you can either play the game, go to the options page, or
 * quit.
 */
@Singleton
public class Title extends ScreenAdapter {
    private final String TAG = getClass().getSimpleName();
    private final Texture image;
    private final StageFactory stageFactory;

    private SpriteBatch batch;
    private Stage stage;
    private Sprite sprite;

    @Inject
    public Title(final AssetManager assetManager,
                 final StageFactory stageFactory) {
        this.stageFactory = stageFactory;
        log.debug(TAG, "constructor()");
        image = assetManager.get("newgame.png", Texture.class);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = stageFactory.buildTitleStage();
        batch = new SpriteBatch();
        sprite = new Sprite(image);
        sprite.setCenter((float) Gdx.graphics.getWidth() / 2f, (float) Gdx.graphics.getHeight() / 2f);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        stage = null;
        batch.dispose();
        batch = null;
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
