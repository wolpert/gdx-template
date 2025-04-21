/*
 * Copyright (c) 2025. Ned Wolpert
 */

package com.codeheadsystems.aseprite.impl;

import java.util.LinkedHashMap;

/**
 * This is read with the JSON parser so I'm just keeping it like a struct for now. I don't want the pojo bloat for
 * this.
 */
public class AsepriteModel {
    public LinkedHashMap<String, AsepriteFrame> frames; // linked list is needed for the order
    public AsepriteMeta meta;

    public static class AsepriteMeta {
        public Size size;
        public String image;
        public FrameTags[] frameTags;
    }

    public static class FrameTags {
        public String name;
        public int from;
        public int to;
        public String direction;
        public String repeat;
    }

    public static class AsepriteFrame {
        public Frame frame;
        public Size sourceSize;
        public int duration;
    }

    public static class Frame {
        public int x, y, w, h;
    }

    public static class Size {
        public int w, h;
    }
}
