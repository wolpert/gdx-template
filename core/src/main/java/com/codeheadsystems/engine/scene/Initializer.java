package com.codeheadsystems.engine.scene;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.codeheadsystems.engine.TopLevelApplication;
import com.codeheadsystems.engine.module.DaggerGameComponent;
import com.codeheadsystems.engine.module.GameComponent;
import com.codeheadsystems.engine.module.GdxModule;
import com.codeheadsystems.engine.module.TopApplicationModule;
import java.util.Optional;

/**
 * Non-dagger class that shows a initial window and starts loading the game.
 */
public class Initializer extends ScreenAdapter {

    private final String TAG = getClass().getSimpleName();
    private final TopLevelApplication topLevelApplication;
    private final Rectangle progressBarBounds;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    private AssetManager assetManager;

    public Initializer(final TopLevelApplication topLevelApplication) {
        this.topLevelApplication = topLevelApplication;
        this.progressBarBounds = new Rectangle();
    }

    @Override
    public void show() {
        log.debug(TAG, "constructor()");
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("loading.png");
        assetManager = createAssetManager();
        getProgressBarBounds();
    }

    @Override
    public void render(final float delta) {
        if (!assetManager.update()) {
            log.debug(TAG, "Progress: " + assetManager.getProgress());
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.begin();
            batch.draw(image, 140, 210);
            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
            shapeRenderer.rect(progressBarBounds.x, progressBarBounds.y, progressBarBounds.width, progressBarBounds.height);
            shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);
            shapeRenderer.rect(progressBarBounds.x, progressBarBounds.y, progressBarBounds.width * assetManager.getProgress(), progressBarBounds.height);
            shapeRenderer.end();
        } else {
            log.debug(TAG, "Asset manager is done");
            final GdxModule gdxModule = new GdxModule(assetManager);
            final TopApplicationModule topApplicationModule = new TopApplicationModule(topLevelApplication);
            final GameComponent component = DaggerGameComponent.builder()
                .gdxModule(gdxModule)
                .topApplicationModule(topApplicationModule)
                .build();
            topLevelApplication.setGameComponent(component);
            topLevelApplication.showTitle();
            shapeRenderer.dispose();
        }
    }

    private void getProgressBarBounds() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float barWidthStart = screenWidth * 0.1f; // 10% in
        float barHeightStart = screenHeight * 0.1f; // 50% down
        float barWidth = screenWidth - (barWidthStart * 2);
        float barHeight = screenHeight * 0.05f;
        progressBarBounds.set(barWidthStart, barHeightStart, barWidth, barHeight);
    }

    @Override
    public void dispose() {
        log.debug(TAG, "Disposing of resources");
        batch.dispose();
        image.dispose();
    }

    private AssetManager createAssetManager() {
        final AssetManager newAssetManager = new AssetManager();
        newAssetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        final FileHandle fs = Gdx.files.internal("assets.txt");
        for (String file : fs.readString().split("\n")) {
            fromClassName(file).ifPresentOrElse(
                type -> {
                    log.debug(TAG, "Loading: " + file + " as: " + type);
                    newAssetManager.load(file, type);
                },
                () -> log.error(TAG, "Unknown type for: " + file));
        }
        return newAssetManager;
    }

    private Optional<Class<?>> fromClassName(final String file) {
        final String extension = file.substring(file.lastIndexOf(".") + 1);
        Class<?> clazz = switch (extension) {
            case "tmx" -> TiledMap.class;
            case "png", "jpg", "jpeg" -> Texture.class;
            case "ogg" -> Music.class;
            default -> null;
        };
        return Optional.ofNullable(clazz);
    }

}
