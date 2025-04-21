/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An aseprite is essentially a set of animations.
 */
public class AnimatedSprite extends Sprite {

    public static final String DEFAULT_ANIMATION = "default";
    private static final String TAG = "Aseprite";

    private final Aseprite aseprite;
    private final float hightWidthRatio;
    private final float widthHightRatio;

    private Animation<TextureRegion> currentAnimation;
    private float currentTime;
    private float maxRuntime;

    public AnimatedSprite(final Aseprite aseprite) {
        super(aseprite.get().getKeyFrames()[0]);
        this.aseprite = aseprite;
        log.debug(TAG, "AnimatedSprite(" + aseprite.getName() + ")");
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
        currentAnimation = aseprite.get(animationName);
        if (currentAnimation == null) {
            log.error(TAG, "No animation found for Aseprite: " + aseprite.getName() + ":" + animationName);
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
            log.error(TAG, "No current animation set for Aseprite: " + aseprite.getName());
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
            log.error(TAG, "No current animation set for Aseprite: " + aseprite.getName());
        }
        TextureRegion frame = currentAnimation.getKeyFrame(stateTime, true);
        setRegion(frame);
    }

}
