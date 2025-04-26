/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import java.util.function.Supplier;

public class SlicedCollisionSupplier implements Supplier<Rectangle[]> {

    private final Sprite sprite;
    private final Rectangle[] slices;
    private final Rectangle[] collisions;
    private final float regionHeight;
    private final float regionWidth;

    public SlicedCollisionSupplier(final Sprite sprite,
                                   final Rectangle[] slices) {
        this.sprite = sprite;
        this.slices = slices;
        this.collisions = new Rectangle[slices.length];
        for (int i = 0; i < slices.length; i++) {
            this.collisions[i] = new Rectangle();
        }
        this.regionHeight = sprite.getRegionHeight();
        this.regionWidth = sprite.getRegionWidth();
    }

    @Override
    public Rectangle[] get() {
        Rectangle boundingRectangle = sprite.getBoundingRectangle();
        float scaleX = sprite.getWidth() / regionWidth;
        float scaleY = sprite.getHeight() / regionHeight;
        for (int i = 0; i < slices.length; i++) {
            collisions[i].set(boundingRectangle);
            collisions[i].width = slices[i].width * scaleX;
            collisions[i].height = slices[i].height * scaleY;
            collisions[i].x += slices[i].x * scaleX;
            // take curren ty, go down from the region height, and up from the slice height. Remember we draw 0,0 in upper left.
            collisions[i].y -= (slices[i].y - regionHeight + slices[i].height) * scaleY;
        }
        return collisions;
    }
}
