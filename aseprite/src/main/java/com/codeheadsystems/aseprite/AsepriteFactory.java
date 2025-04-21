/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite;

import com.badlogic.gdx.assets.AssetManager;
import com.codeheadsystems.aseprite.impl.AsepriteConfiguration;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AsepriteFactory {

    private final AssetManager assetManager;
    private final AsepriteConfiguration asepriteConfiguration;

    @Inject
    public AsepriteFactory(final AssetManager assetManager,
                           final AsepriteConfiguration asepriteConfiguration) {
        this.assetManager = assetManager;
        this.asepriteConfiguration = asepriteConfiguration;
    }

    /**
     * This is the name without the extension in the assets's aseprite directory.
     *
     * @param asepriteIdentifier like player.... which it converts to aseprite/player
     * @return the Aseprite if it exists.
     */
    public Aseprite getAseprite(String asepriteIdentifier) {
        final String asepriteAssetName = asepriteConfiguration.getAsepriteAssetName(asepriteIdentifier);
        return assetManager.get(asepriteAssetName, Aseprite.class);
    }

    public AnimatedSprite getAnimatedSprite(String animatedSpriteIdentifier) {
        return new AnimatedSprite(getAseprite(animatedSpriteIdentifier));
    }

}
