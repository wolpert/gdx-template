package com.codeheadsystems.engine.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Octagon implements Pool.Poolable {

    private final float[] vertices = new float[16];

    @Override
    public void reset() {

    }

    public Octagon set(Vector2 center, float length) {
        float aThird = length / 3f;
        float x = center.x - length / 2f;
        float y = center.y - length / 2f;
        vertices[0] = x + aThird;
        vertices[1] = y;
        vertices[2] = x + aThird + aThird;
        vertices[3] = y;
        vertices[4] = x + length;
        vertices[5] = y + aThird;
        vertices[6] = x + length;
        vertices[7] = y + aThird + aThird;
        vertices[8] = x + aThird + aThird;
        vertices[9] = y + length;
        vertices[10] = x + aThird;
        vertices[11] = y + length;
        vertices[12] = x;
        vertices[13] = y + aThird + aThird;
        vertices[14] = x;
        vertices[15] = y + aThird;
        return this;
    }

    public float[] vertices() {
        return vertices;
    }
}
