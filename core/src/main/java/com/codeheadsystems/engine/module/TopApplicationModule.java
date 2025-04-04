package com.codeheadsystems.engine.module;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.codeheadsystems.engine.TopLevelApplication;
import com.codeheadsystems.engine.model.AshleyGameConfiguration;
import com.codeheadsystems.engine.model.GameConfig;
import dagger.Module;
import dagger.Provides;
import java.util.Set;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class TopApplicationModule {

    public static final String ENTITY_SYSTEM_RUNNABLE_ARRAY = "entitySystemRunnable";
    private static final String TAG = TopApplicationModule.class.getSimpleName();

    private final TopLevelApplication topLevelApplication;

    public TopApplicationModule(final TopLevelApplication topLevelApplication) {
        this.topLevelApplication = topLevelApplication;
    }

    @Provides
    @Singleton
    public TopLevelApplication topLevelApplication() {
        return topLevelApplication;
    }

    @Provides
    @Singleton
    public GameConfig gameConfig() {
        final FileHandle fs = Gdx.files.internal("config.json");
        return new Json().fromJson(GameConfig.class, fs);
    }

    @Provides
    @Singleton
    public GameConfig.Globals globals(GameConfig gameConfig) {
        return gameConfig.globals;
    }

    @Provides
    @Singleton
    public Skin skin() {
        return new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

    @Provides
    @Singleton
    public BitmapFont bitmapFont(Skin skin) {
        return skin.getFont("default");
    }

    /**
     * Pooled engine pooled engine.
     *
     * @param configuration the configuration
     * @return the pooled engine
     */
    @Provides
    @Singleton
    public PooledEngine pooledEngine(final AshleyGameConfiguration configuration,
                                     final Set<EntitySystem> entitySystems) {
        final PooledEngine pooledEngine = new PooledEngine(
            configuration.entityPoolInitialSize,
            configuration.entityPoolMaxSize,
            configuration.componentPoolInitialSize,
            configuration.componentPoolMaxSize);
        for (EntitySystem entitySystem : entitySystems) {
            log.debug(TAG, "Adding entity system: " + entitySystem.getClass().getSimpleName());
            pooledEngine.addSystem(entitySystem);
        }

        return pooledEngine;
    }

    /**
     * Ashley game configuration ashley game configuration.
     *
     * @param fileHandleResolver the file handle resolver
     * @param json               the json
     * @return the ashley game configuration
     */
    @Provides
    @Singleton
    public AshleyGameConfiguration ashleyGameConfiguration(final FileHandleResolver fileHandleResolver,
                                                           final Json json) {
        final FileHandle fileHandle = fileHandleResolver.resolve("ashleyGameConfiguration.json");
        return json.fromJson(AshleyGameConfiguration.class, fileHandle);
    }

    @Provides
    @Singleton
    @Named(ENTITY_SYSTEM_RUNNABLE_ARRAY)
    public Array<Runnable> entitySystemRunnables() {
        return new Array<>();
    }

}
