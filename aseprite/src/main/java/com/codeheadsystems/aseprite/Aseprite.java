/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import java.util.Map;
import java.util.Set;

/**
 * An aseprite is essentially a set of animations.
 */
public class Aseprite implements Disposable {

    public static final String DEFAULT_ANIMATION = "default";
    private static final String TAG = "Aseprite";

    private final String name;
    private final Texture texture;
    private final Map<String, Animation<TextureRegion>> animations;

    public Aseprite(final String name,
                    final Texture texture,
                    final Map<String, Animation<TextureRegion>> animations) {
        this.name = name;
        this.texture = texture;
        this.animations = animations;
        log.debug(TAG, "Aseprite("+name+")");
    }

    @Override
    public void dispose() {
        texture.dispose();
        animations.clear();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the animation of the full set.
     *
     * @return the set.
     */
    public Animation<TextureRegion> get() {
        return animations.get(DEFAULT_ANIMATION);
    }

    public boolean hasAnimation(final String name) {
        return animations.containsKey(name);
    }

    public Set<String> getAnimationNames() {
        return animations.keySet();
    }

    public Animation<TextureRegion> get(final String name) {
        return animations.get(name);
    }

}
