/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import java.util.function.Supplier;

public class DefaultCollisionSupplier implements Supplier<Rectangle[]> {

    private final Sprite sprite;
    private final Rectangle[] collisions;

    public DefaultCollisionSupplier(final Sprite sprite) {
        this.sprite = sprite;
        this.collisions = new Rectangle[1];
        collisions[0] = new Rectangle();
    }

    @Override
    public Rectangle[] get() {
        collisions[0].set(sprite.getBoundingRectangle());
        return collisions;
    }
}
