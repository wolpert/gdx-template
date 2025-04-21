/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite.impl;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.codeheadsystems.aseprite.Aseprite;
import com.codeheadsystems.aseprite.loader.AsepriteLoader;
import com.codeheadsystems.aseprite.loader.AsepriteModelLoader;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AsepriteConfiguration {

    private static final String ASEPRITE_DIR = "aseprite/";

    @Inject
    public AsepriteConfiguration() {

    }

    public AssetManager configureAssetManager() {
        return configureAssetManager(new AssetManager());
    }

    public AssetManager configureAssetManager(final AssetManager assetManager) {
        return configureAssetManager(assetManager, assetManager.getFileHandleResolver());
    }

    public AssetManager configureAssetManager(final AssetManager assetManager,
                                              final FileHandleResolver resolver) {
        assetManager.setLoader(AsepriteModel.class, new AsepriteModelLoader(resolver));
        assetManager.setLoader(Aseprite.class, new AsepriteLoader(resolver));
        return assetManager;
    }

    /**
     * The name in the asset manager.
     *
     * @param aespriteIdentifier as used within gdx. Like player.
     * @return the name to be used by the asset manager. Like aseprite/player
     */
    public String getAsepriteAssetName(String aespriteIdentifier) {
        return ASEPRITE_DIR + aespriteIdentifier;
    }

    /**
     * Used to store the asset in the asset manager, just lops off the extension.
     *
     * @param assertFileName as seen in assert.txt
     * @return the name to be used by the asset manager.
     */
    public String getAssetManagerBasename(String assertFileName) {
        return assertFileName.substring(0, assertFileName.lastIndexOf('.'));
    }

    /**
     * Is this raw filename (like in assets.txt) an aseprite bundle?
     *
     * @param fileName the name of the file.
     * @return true if it is an aseprite bundle.
     */
    public boolean isAsepriteBundle(String fileName) {
        return fileName.startsWith(ASEPRITE_DIR);
    }

}
