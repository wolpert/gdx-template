package com.codeheadsystems.engine.module;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import javax.inject.Named;
import javax.inject.Singleton;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Contains standard GDX resources. What needs setup before the game is passed in via the
 * constructor. Everything else we build and provide.
 */
@Module(includes = GdxModule.Disposables.class)
public class GdxModule {

    private final AssetManager assetManager;

    public GdxModule(final AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Provides
    @Singleton
    public AssetManager assetManager() {
        return assetManager;
    }

    @Provides
    @Singleton
    public Json json() {
        return new Json();
    }

    @Provides
    @Singleton
    public SpriteBatch spriteBatch() {
        return new SpriteBatch();
    }

    @Provides
    @Singleton
    public FileHandleResolver fileHandleResolver() {
        return new InternalFileHandleResolver();
    }

    @Provides
    @Singleton
    public OrthographicCamera orthographicCamera() {
        return new OrthographicCamera();
    }

    @Provides
    @Singleton
    public PolygonSpriteBatch polygonSpriteBatch() {
        return new PolygonSpriteBatch();
    }

    @Provides
    @Singleton
    public ShapeDrawer shapeDrawer(PolygonSpriteBatch polygonSpriteBatch,
                                   @Named("whitePixel") TextureRegion whitePixel) {
        return new ShapeDrawer(polygonSpriteBatch, whitePixel);
    }

    /**
     * To create a ShapeDrawer you just need a Batch and a TextureRegion.
     * Typically this is a single white pixel so that you can easily colour it,
     * and this is best packed into an atlas with your other textures.
     * However for testing purposes you can make one programmatically:
     *
     * @return texture region for a white pixel
     */
    @Provides
    @Singleton
    @Named("whitePixel")
    public TextureRegion whitePixel(@Named("whitePixelTexture") Texture texture) {
        return new TextureRegion(texture, 0, 0, 1, 1);
    }


    @Provides
    @Singleton
    @Named("whitePixelTexture")
    public Texture whitePixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();
        return texture;
    }

    /**
     * List of things we want to dispose when we close the app.
     */
    @Module
    public interface Disposables {

        @Binds
        @IntoSet
        Disposable bindAssetManager(AssetManager assetManager);

        @Binds
        @IntoSet
        Disposable bindTexture(@Named("whitePixelTexture") Texture texture);

        @Binds
        @IntoSet
        Disposable bindSpriteBatch(SpriteBatch spriteBatch);

        @Binds
        @IntoSet
        Disposable bindPolygonSpriteBatch(PolygonSpriteBatch polygonSpriteBatch);

    }
}
