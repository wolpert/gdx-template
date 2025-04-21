/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.codeheadsystems.aseprite.impl.AsepriteModel;

public class AsepriteModelLoader extends AsynchronousAssetLoader<AsepriteModel, AsepriteModelLoader.AspriteModelParameter> {

    private final Json json;
    private AsepriteModel asepriteModel;

    public AsepriteModelLoader(final FileHandleResolver resolver) {
        super(resolver);
        json = new Json();
        json.setIgnoreUnknownFields(true);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(final String filename,
                                                  final FileHandle fileHandle,
                                                  final AspriteModelParameter aspriteModelParameter) {
        return null;
    }

    @Override
    public void loadAsync(final AssetManager assetManager, final String filename, final FileHandle fileHandle, final AspriteModelParameter aspriteModelParameter) {
        asepriteModel = json.fromJson(AsepriteModel.class, fileHandle.readString());
    }

    @Override
    public AsepriteModel loadSync(final AssetManager assetManager, final String s, final FileHandle fileHandle, final AspriteModelParameter aspriteModelParameter) {
        AsepriteModel copy = asepriteModel;
        asepriteModel = null;
        return copy;
    }

    public static class AspriteModelParameter extends AssetLoaderParameters<AsepriteModel> {

    }
}
