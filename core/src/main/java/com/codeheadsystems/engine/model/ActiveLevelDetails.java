package com.codeheadsystems.engine.model;

import com.badlogic.gdx.math.Rectangle;
import io.soabase.recordbuilder.core.RecordInterface;

@RecordInterface
public interface ActiveLevelDetails {
    LevelConfig levelConfig();

    Sizes sizes();

    record Sizes(Rectangle boundingRectangle,
                 float extraSmall,
                 float extraSmallReduced,
                 float smallReduced,
                 float small,
                 float mediumReduced,
                 float medium,
                 float large,
                 float extraLarge,
                 float lineWidth,
                 float lineWidthReduced) {
        public Sizes(Rectangle boundingRectangle) {
            this(boundingRectangle,
                boundingRectangle.width * 0.075f,
                boundingRectangle.width * 0.1f,
                boundingRectangle.width * 0.175f,
                boundingRectangle.width * 0.2f,
                boundingRectangle.width * 0.275f,
                boundingRectangle.width * 0.3f,
                boundingRectangle.width * 0.4f,
                boundingRectangle.width * 0.5f,
                boundingRectangle.width * 0.05f,
                boundingRectangle.width * 0.025f);
        }
    }
}
