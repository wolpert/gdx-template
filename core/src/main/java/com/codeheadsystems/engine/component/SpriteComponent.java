package com.codeheadsystems.engine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class SpriteComponent implements Pool.Poolable, Component {

    private Sprite sprite;
    private float x;
    private float y;

    @Override
    public void reset() {
        x = 0;
        y = 0;
        sprite = null;
    }

    public SpriteComponent withSprite(Sprite sprite) {
        this.sprite = sprite;
        return this;
    }

    public SpriteComponent withPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public SpriteComponent withPosition(Rectangle bounds) {
        this.x = bounds.x;
        this.y = bounds.y;
        return this;
    }

    public Sprite sprite() {
        return sprite;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

}
