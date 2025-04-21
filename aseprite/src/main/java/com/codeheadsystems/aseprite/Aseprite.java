/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import java.util.Map;
import java.util.Set;

/**
 * An aseprite is essentially a set of animations.
 */
public class Aseprite extends Sprite implements Disposable {

    public static final String DEFAULT_ANIMATION = "default";
    private static final String TAG = "Aseprite";

    private final String name;
    private final Texture texture;
    private final Map<String, Animation<TextureRegion>> animations;
    private final float hightWidthRatio;
    private final float widthHightRatio;

    private Animation<TextureRegion> currentAnimation;
    private float currentTime;
    private float maxRuntime;

    public Aseprite(final String name,
                    final Texture texture,
                    final Map<String, Animation<TextureRegion>> animations) {
        super(animations.get(DEFAULT_ANIMATION).getKeyFrames()[0]);
        this.name = name;
        this.texture = texture;
        this.animations = animations;
        log.debug(TAG, "Aseprite(" + name + ")");
        animation(DEFAULT_ANIMATION);
        setFrame(0f);
        this.hightWidthRatio = (float) getRegionHeight() / (float) getRegionWidth();
        this.widthHightRatio = (float) getRegionWidth() / (float) getRegionHeight();
    }

    public float getHightWidthRatio() {
        return hightWidthRatio;
    }

    public float getWidthHightRatio() {
        return widthHightRatio;
    }

    public boolean animation(String animationName) {
        currentAnimation = animations.get(animationName);
        if (currentAnimation == null) {
            log.error(TAG, "No animation found for Aseprite: " + name + ":" + animationName);
            return false;
        }
        maxRuntime = currentAnimation.getAnimationDuration();
        return true;
    }

    /**
     * Call this to automatically update the frame and have the delta tracked here.
     * This assumes looping.
     *
     * @param delta the delta time since the last update
     */
    public void update(float delta) {
        if (currentAnimation == null) {
            log.error(TAG, "No current animation set for Aseprite: " + name);
            return;
        }
        currentTime += delta;
        while (currentTime > maxRuntime) {
            currentTime -= maxRuntime; // Reset time if it exceeds the max runtime
        }
        setFrame(currentTime);
    }

    /**
     * Call this to automatically get the frame you want. Does not update the stored time.
     *
     * @param stateTime
     */
    public void setFrame(float stateTime) {
        if (currentAnimation == null) {
            log.error(TAG, "No current animation set for Aseprite: " + name);
        }
        TextureRegion frame = currentAnimation.getKeyFrame(stateTime, true);
        setRegion(frame);
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
