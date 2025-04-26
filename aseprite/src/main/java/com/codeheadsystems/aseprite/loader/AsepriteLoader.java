/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite.loader;

import static com.codeheadsystems.aseprite.Aseprite.DEFAULT_ANIMATION;
import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.codeheadsystems.aseprite.Aseprite;
import com.codeheadsystems.aseprite.impl.AsepriteModel;
import java.util.LinkedHashMap;
import java.util.Map;

public class AsepriteLoader extends AsynchronousAssetLoader<Aseprite, AsepriteLoader.AsepriteParameter> {

    private static final String TAG = "Aseprite";
    private Aseprite aseprite;

    public AsepriteLoader(final FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(final String filename,
                                                  final FileHandle fileHandle,
                                                  final AsepriteParameter asepriteParameter) {
        return Array.with(
            new AssetDescriptor(fileHandle.pathWithoutExtension() + ".png", Texture.class),
            new AssetDescriptor(fileHandle.pathWithoutExtension() + ".json", AsepriteModel.class)
        );
    }

    @Override
    public void loadAsync(final AssetManager assetManager,
                          final String filename,
                          final FileHandle fileHandle,
                          final AsepriteParameter asepriteParameter) {
        log.debug(TAG, "loadAsync Aseprite: " + filename);
        final String pathWithoutExtension = fileHandle.pathWithoutExtension();
        final Texture texture = assetManager.get(pathWithoutExtension + ".png", Texture.class);
        final AsepriteModel asepriteModel = assetManager.get(pathWithoutExtension + ".json", AsepriteModel.class);

        // meta/image is the filename within the directory, so no need to remove it.
        final String name = asepriteModel.meta.image.contains(".") ? asepriteModel.meta.image.substring(0, asepriteModel.meta.image.lastIndexOf('.')) : asepriteModel.meta.image;
        final int frameDurationMs = asepriteModel.frames.values().stream().findFirst().orElseThrow().duration;
        final float frameDurationSeconds = (float) frameDurationMs / 1000f;
        final TextureRegion[] textureRegions = buildTextureRegionsFrom(asepriteModel, texture);
        final Map<String, Animation<TextureRegion>> animations =
            buildAnimationsFrom(frameDurationSeconds, textureRegions, asepriteModel.meta.frameTags);
        final Map<String, Rectangle> slices = extractSlices(asepriteModel.meta.slices);
        aseprite = new Aseprite(name, texture, animations, slices);
    }

    private Map<String, Rectangle> extractSlices(final AsepriteModel.Slice[] slices) {
        final Map<String, Rectangle> slicesMap = new LinkedHashMap<>();
        if (slices != null) {
            for (AsepriteModel.Slice slice : slices) {
                final AsepriteModel.Frame frame = slice.keys[0].bounds;
                final Rectangle rectangle = new Rectangle(frame.x, frame.y, frame.w, frame.h);
                slicesMap.put(slice.name, rectangle);
            }
        }
        return slicesMap;
    }

    @Override
    public Aseprite loadSync(final AssetManager assetManager,
                             final String filename,
                             final FileHandle fileHandle,
                             final AsepriteParameter asepriteParameter) {
        final Aseprite copy = aseprite;
        aseprite = null;
        return copy;
    }

    private Map<String, Animation<TextureRegion>> buildAnimationsFrom(
        final float frameDurationSeconds,
        final TextureRegion[] textureRegions,
        final AsepriteModel.FrameTags[] frameTags) {
        final Map<String, Animation<TextureRegion>> animations = new LinkedHashMap<>();
        animations.put(DEFAULT_ANIMATION, animationFrom(frameDurationSeconds, textureRegions));
        if (frameTags != null) {
            for (AsepriteModel.FrameTags frameTag : frameTags) {
                final Animation<TextureRegion> animation = animationFrom(frameDurationSeconds, textureRegions, frameTag);
                animations.put(frameTag.name, animation);
            }
        }
        return animations;
    }

    private TextureRegion[] buildTextureRegionsFrom(final AsepriteModel asepriteModel,
                                                    final Texture texture) {
        final TextureRegion[] regions = new TextureRegion[asepriteModel.frames.size()];
        int i = 0;
        for (AsepriteModel.AsepriteFrame frame : asepriteModel.frames.values()) {
            AsepriteModel.Frame f = frame.frame; // names suck
            regions[i] = new TextureRegion(texture, f.x, f.y, f.w, f.h);
            i++;
        }
        return regions;
    }


    private Animation<TextureRegion> animationFrom(final float frameDurationSeconds,
                                                   final TextureRegion[] allFrames) {
        return new Animation<>(frameDurationSeconds, allFrames);
    }

    private Animation<TextureRegion> animationFrom(final float frameDurationSeconds,
                                                   final TextureRegion[] allFrames,
                                                   final AsepriteModel.FrameTags frameTags) {
        final TextureRegion[] frames = new TextureRegion[frameTags.to - frameTags.from + 1];
        int i = 0;
        for (int j = frameTags.from; j <= frameTags.to; j++) {
            frames[i] = allFrames[j];
            i++;
        }
        return new Animation<>(frameDurationSeconds, frames);
    }

    public class AsepriteParameter extends AssetLoaderParameters<Aseprite> {

    }

}
