package com.codeheadsystems.engine.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Triangle implements Pool.Poolable {
    public static float COS_120D = -0.5f;
    public static float SIN_120D = 0.866f;
    public static float COS_240D = -0.5f;
    public static float SIN_240D = -0.866f;

    public static float COS_140 = -0.766f;
    public static float SIN_140 = 0.643f;
    public static float COS_220 = -0.766f;
    public static float SIN_220 = -0.643f;

    public final Vector2 point1;
    public final Vector2 point2;
    public final Vector2 point3;


    public Triangle() {
        point1 = new Vector2();
        point2 = new Vector2();
        point3 = new Vector2();
    }

    @Override
    public void reset() {
        point1.set(0, 0);
        point2.set(0, 0);
        point3.set(0, 0);
    }

    public Triangle pointy(float length) {
        return getTriangle(length, COS_140, SIN_140, COS_220, SIN_220);
    }

    public Triangle equilateral(float length) {
        return getTriangle(length, COS_120D, SIN_120D, COS_240D, SIN_240D);
    }

    private Triangle getTriangle(final float length,
                                 final float cosFirstDegree,
                                 final float sinFirstDegree,
                                 final float cosSecondDegree,
                                 final float sinSecondDegree) {
        float cx = 0;
        float cy = length;
        point1.set(cx, cy);
        point2.set(cx * cosFirstDegree - cy * sinFirstDegree, cx * sinFirstDegree + cy * cosFirstDegree);
        point3.set(cx * cosSecondDegree - cy * sinSecondDegree, cx * sinSecondDegree + cy * cosSecondDegree);
        return this;
    }

    public Triangle rotate(float degree) {
        rotateOrigin(point1, degree);
        rotateOrigin(point2, degree);
        rotateOrigin(point3, degree);
        return this;
    }

    private void rotateOrigin(Vector2 point, float degree) {
        float radians = (float) Math.toRadians(degree);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        float x = point.x;
        float y = point.y;
        point.x = x * cos - y * sin;
        point.y = x * sin + y * cos;
    }


    public Triangle add(Vector2 center) {
        point1.add(center);
        point2.add(center);
        point3.add(center);
        return this;
    }

}
