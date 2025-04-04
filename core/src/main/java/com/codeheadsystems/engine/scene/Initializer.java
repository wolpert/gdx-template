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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
    private SpriteBatch batch;
    private Texture image;
    private AssetManager assetManager;

    public Initializer(final TopLevelApplication topLevelApplication) {
        this.topLevelApplication = topLevelApplication;
    }

    @Override
    public void show() {
        log.debug(TAG, "constructor()");
        batch = new SpriteBatch();
        image = new Texture("loading.png");
        assetManager = createAssetManager();
    }

    @Override
    public void render(final float delta) {
        if (!assetManager.update()) {
            log.debug(TAG, "Progress: " + assetManager.getProgress());
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.begin();
            batch.draw(image, 140, 210);
            batch.end();
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
        }
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
