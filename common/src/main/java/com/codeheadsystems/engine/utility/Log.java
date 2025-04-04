package com.codeheadsystems.engine.utility;

import com.badlogic.gdx.Gdx;

/**
 * Simplified logger for games.
 * It just wraps the Gdx calls directly to reduce object creation.
 */
public enum Log {

    log;

    public void debug(String tag, String message) {
        Gdx.app.debug(tag, message);
    }

    public void debug(String tag, String message, Exception exception) {
        Gdx.app.debug(tag, message, exception);
    }

    public void info(String tag, String message) {
        Gdx.app.log(tag, message);
    }

    public void info(String tag, String message, Exception exception) {
        Gdx.app.log(tag, message, exception);
    }

    public void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }

    public void error(String tag, String message, Throwable exception) {
        Gdx.app.error(tag, message, exception);
    }

}
